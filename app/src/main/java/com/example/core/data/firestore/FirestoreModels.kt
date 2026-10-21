package com.example.core.data.firestore

import com.example.core.model.DivisionTier
import com.example.core.model.GameMode
import com.example.core.model.Tournament
import com.example.core.model.TournamentStatus
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreTournament(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val mode: String = "SQUAD",
    val division: String = "OPEN",
    val startTimestamp: Long = 0L,
    val startTimeFormatted: String = "",
    val prizePool: Long = 0L,
    val entryFee: Long = 0L,
    val totalSlots: Long = 48L,
    val joinedSlots: Long = 0L,
    val status: String = "UPCOMING",
    val rules: List<String> = emptyList(),
    val sponsor: String = "FF Arena Sponsor",
    val mapName: String = "Bermuda",
    val roomId: String = "",
    val roomPassword: String = "",
    val participantUids: List<String> = emptyList(),
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val updatedAt: Timestamp? = null
) {
    fun toDomain(currentUserId: String? = null): Tournament {
        val gameMode = try {
            GameMode.valueOf(mode.uppercase())
        } catch (_: Exception) {
            GameMode.SQUAD
        }
        val divisionTier = try {
            DivisionTier.valueOf(division.uppercase())
        } catch (_: Exception) {
            DivisionTier.OPEN
        }
        val tStatus = try {
            TournamentStatus.valueOf(status.uppercase())
        } catch (_: Exception) {
            TournamentStatus.UPCOMING
        }

        return Tournament(
            id = id,
            title = title,
            subtitle = subtitle,
            mode = gameMode,
            division = divisionTier,
            startTimestamp = if (startTimestamp > 0) startTimestamp else (createdAt?.seconds ?: 0) * 1000L,
            startTimeFormatted = startTimeFormatted.ifBlank { "TBD" },
            prizePool = prizePool.toInt(),
            entryFee = entryFee.toInt(),
            totalSlots = totalSlots.toInt().coerceAtLeast(1),
            joinedSlots = joinedSlots.toInt().coerceAtLeast(0),
            status = tStatus,
            rules = rules.ifEmpty { listOf("Standard Free Fire Fair Play esports rules apply.") },
            sponsor = sponsor,
            mapName = mapName,
            roomId = roomId,
            roomPassword = roomPassword,
            isJoined = currentUserId != null && participantUids.contains(currentUserId)
        )
    }

    companion object {
        fun fromDomain(domain: Tournament, participantUids: List<String> = emptyList()): FirestoreTournament {
            return FirestoreTournament(
                id = domain.id,
                title = domain.title,
                subtitle = domain.subtitle,
                mode = domain.mode.name,
                division = domain.division.name,
                startTimestamp = domain.startTimestamp,
                startTimeFormatted = domain.startTimeFormatted,
                prizePool = domain.prizePool.toLong(),
                entryFee = domain.entryFee.toLong(),
                totalSlots = domain.totalSlots.toLong(),
                joinedSlots = domain.joinedSlots.toLong(),
                status = domain.status.name,
                rules = domain.rules,
                sponsor = domain.sponsor,
                mapName = domain.mapName,
                roomId = domain.roomId,
                roomPassword = domain.roomPassword,
                participantUids = participantUids
            )
        }
    }
}

@IgnoreExtraProperties
data class FirestoreRegistration(
    @DocumentId
    val id: String = "",
    val tournamentId: String = "",
    val userId: String = "",
    val inGameName: String = "",
    val ffUid: String = "",
    val status: String = "REGISTERED",
    @ServerTimestamp
    val registeredAt: Timestamp? = null
)
