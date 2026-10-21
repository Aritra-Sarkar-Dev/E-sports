package com.example.core.data.firestore

import android.content.Context
import android.util.Log
import com.example.core.data.MockTournamentRepositoryImpl
import com.example.core.data.TournamentRepository
import com.example.core.model.DivisionTier
import com.example.core.model.GameMode
import com.example.core.model.LeaderboardEntry
import com.example.core.model.LeaderboardTab
import com.example.core.model.MatchRecord
import com.example.core.model.Tournament
import com.example.core.model.TournamentStatus
import com.example.core.model.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Firestore-backed TournamentRepository.
 * Manages esports tournament listings, real-time participant registrations,
 * and room credential access using Cloud Firestore.
 * Seamlessly falls back to local tournament mock data when running offline
 * or in unprovisioned environments.
 */
class FirestoreTournamentRepository(
    private val context: Context,
    private val firestore: FirebaseFirestore? = try {
        FirebaseFirestore.getInstance()
    } catch (e: Exception) {
        Log.w("FirestoreTournamentRepo", "Firestore not initialized or missing google-services: ${e.message}")
        null
    },
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : TournamentRepository {

    private val fallbackRepo = MockTournamentRepositoryImpl()

    private val _tournamentsFlow = MutableStateFlow<List<Tournament>>(fallbackRepo.tournamentsFlow.value)
    override val tournamentsFlow: StateFlow<List<Tournament>> = _tournamentsFlow.asStateFlow()

    private val _myMatchesFlow = MutableStateFlow<List<MatchRecord>>(fallbackRepo.myMatchesFlow.value)
    override val myMatchesFlow: StateFlow<List<MatchRecord>> = _myMatchesFlow.asStateFlow()

    private var snapshotListener: ListenerRegistration? = null

    init {
        startObservingTournaments()
    }

    /**
     * Attaches a real-time snapshot listener on the 'tournaments' collection in Firestore.
     */
    fun startObservingTournaments(userId: String? = null) {
        val db = firestore ?: return
        try {
            snapshotListener?.remove()
            snapshotListener = db.collection("tournaments")
                .orderBy("startTimestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("FirestoreTournamentRepo", "Listen failed for tournaments: ${error.message}", error)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        val tournaments = snapshot.documents.mapNotNull { doc ->
                            try {
                                doc.toObject(FirestoreTournament::class.java)?.toDomain(currentUserId = userId)
                            } catch (e: Exception) {
                                Log.e("FirestoreTournamentRepo", "Failed to deserialize doc ${doc.id}: ${e.message}")
                                null
                            }
                        }
                        if (tournaments.isNotEmpty()) {
                            _tournamentsFlow.value = tournaments
                        }
                    } else if (snapshot != null && snapshot.isEmpty) {
                        // Seed default tournament roster if collection is completely fresh
                        seedInitialTournamentsIfEmpty()
                    }
                }
        } catch (e: Exception) {
            Log.w("FirestoreTournamentRepo", "Could not attach Firestore listener: ${e.message}")
        }
    }

    override fun getTournamentById(id: String): Tournament? {
        return _tournamentsFlow.value.find { it.id == id }
            ?: fallbackRepo.getTournamentById(id)
    }

    override fun joinTournament(tournamentId: String): Result<Tournament> {
        val currentList = _tournamentsFlow.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == tournamentId }
        val tournament = if (index != -1) currentList[index] else fallbackRepo.getTournamentById(tournamentId)
            ?: return Result.failure(IllegalArgumentException("Tournament not found"))

        if (tournament.isJoined) {
            return Result.success(tournament)
        }
        if (tournament.spotsLeft <= 0) {
            return Result.failure(IllegalStateException("Tournament slots are completely full!"))
        }

        val updated = tournament.copy(
            joinedSlots = tournament.joinedSlots + 1,
            isJoined = true
        )

        if (index != -1) {
            currentList[index] = updated
            _tournamentsFlow.value = currentList
        }

        // Add to My Matches
        val newMatch = MatchRecord(
            id = "match_fs_${System.currentTimeMillis() % 10000}",
            tournamentId = updated.id,
            tournamentTitle = updated.title,
            mode = updated.mode,
            division = updated.division,
            matchTimeMillis = updated.startTimestamp,
            matchTimeFormatted = updated.startTimeFormatted,
            rank = null,
            kills = null,
            prizeWon = null,
            status = TournamentStatus.UPCOMING,
            roomId = updated.roomId,
            roomPassword = updated.roomPassword,
            proofVerified = true
        )
        val updatedMatches = _myMatchesFlow.value.toMutableList()
        updatedMatches.add(0, newMatch)
        _myMatchesFlow.value = updatedMatches

        // Write registration to Firestore asynchronously
        firestore?.let { db ->
            scope.launch {
                try {
                    val tournamentDoc = db.collection("tournaments").document(tournamentId)
                    tournamentDoc.update(
                        "joinedSlots", FieldValue.increment(1)
                    ).await()

                    val regDoc = db.collection("registrations").document()
                    val registration = FirestoreRegistration(
                        id = regDoc.id,
                        tournamentId = tournamentId,
                        userId = "usr_001",
                        inGameName = "⚡ViperSniper⚡",
                        ffUid = "839472819"
                    )
                    regDoc.set(registration).await()
                    Log.d("FirestoreTournamentRepo", "Registration synced to Firestore for tournament $tournamentId")
                } catch (e: Exception) {
                    Log.w("FirestoreTournamentRepo", "Async registration write skipped or failed: ${e.message}")
                }
            }
        }

        return Result.success(updated)
    }

    override fun getLeaderboard(tab: LeaderboardTab): List<LeaderboardEntry> {
        return fallbackRepo.getLeaderboard(tab)
    }

    override fun getHeroTournament(): Tournament {
        return _tournamentsFlow.value.firstOrNull() ?: fallbackRepo.getHeroTournament()
    }

    /**
     * Seeds initial Free Fire tournaments into Firestore if empty
     */
    fun seedInitialTournamentsIfEmpty() {
        val db = firestore ?: return
        scope.launch {
            try {
                val batch = db.batch()
                fallbackRepo.tournamentsFlow.value.forEach { t ->
                    val docRef = db.collection("tournaments").document(t.id)
                    val fsTournament = FirestoreTournament.fromDomain(t)
                    batch.set(docRef, fsTournament)
                }
                batch.commit().await()
                Log.d("FirestoreTournamentRepo", "Successfully seeded default esports tournaments to Firestore.")
            } catch (e: Exception) {
                Log.w("FirestoreTournamentRepo", "Seeding skipped: ${e.message}")
            }
        }
    }

    fun cleanUp() {
        snapshotListener?.remove()
    }
}
