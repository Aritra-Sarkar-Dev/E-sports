package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.config.AppConfig
import com.example.core.data.MockTournamentRepositoryImpl
import com.example.core.data.MockUserRepositoryImpl
import com.example.core.data.TournamentRepository
import com.example.core.data.UserRepository
import com.example.core.model.GameMode
import com.example.core.model.LeaderboardEntry
import com.example.core.model.LeaderboardTab
import com.example.core.model.MatchRecord
import com.example.core.model.Tournament
import com.example.core.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AuthScreen {
    LOGIN,
    SIGN_UP,
    FORGOT_PASSWORD,
    PHONE_OTP
}

enum class BottomTab(val title: String) {
    HOME("Home"),
    COMPETE("Compete"),
    MATCHES("Matches"),
    RANKS("Ranks"),
    PROFILE("Profile")
}

enum class MatchesSubTab {
    UPCOMING,
    COMPLETED
}

class FFArenaViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepo: UserRepository = MockUserRepositoryImpl(application)
    private val tournamentRepo: TournamentRepository = com.example.core.data.firestore.FirestoreTournamentRepository(application)

    val currentUser: StateFlow<User?> = userRepo.currentUserFlow
    val tournaments: StateFlow<List<Tournament>> = tournamentRepo.tournamentsFlow
    val myMatches: StateFlow<List<MatchRecord>> = tournamentRepo.myMatchesFlow

    // Navigation & Auth Flow
    private val _authScreen = MutableStateFlow(AuthScreen.LOGIN)
    val authScreen: StateFlow<AuthScreen> = _authScreen.asStateFlow()

    private val _currentBottomTab = MutableStateFlow(BottomTab.HOME)
    val currentBottomTab: StateFlow<BottomTab> = _currentBottomTab.asStateFlow()

    // Filters & Tabs
    private val _selectedFilterMode = MutableStateFlow(GameMode.ALL)
    val selectedFilterMode: StateFlow<GameMode> = _selectedFilterMode.asStateFlow()

    private val _matchesSubTab = MutableStateFlow(MatchesSubTab.UPCOMING)
    val matchesSubTab: StateFlow<MatchesSubTab> = _matchesSubTab.asStateFlow()

    private val _leaderboardTab = MutableStateFlow(LeaderboardTab.WEEKLY)
    val leaderboardTab: StateFlow<LeaderboardTab> = _leaderboardTab.asStateFlow()

    // Modals & Sheets
    private val _selectedTournamentDetails = MutableStateFlow<Tournament?>(null)
    val selectedTournamentDetails: StateFlow<Tournament?> = _selectedTournamentDetails.asStateFlow()

    private val _selectedProofMatch = MutableStateFlow<MatchRecord?>(null)
    val selectedProofMatch: StateFlow<MatchRecord?> = _selectedProofMatch.asStateFlow()

    private val _showEditProfileDialog = MutableStateFlow(false)
    val showEditProfileDialog: StateFlow<Boolean> = _showEditProfileDialog.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    // Config flag for paid entry compliance:
    private val _paidEntryEnabled = MutableStateFlow(AppConfig.enablePaidEntry)
    val paidEntryEnabled: StateFlow<Boolean> = _paidEntryEnabled.asStateFlow()

    // Filtered tournaments
    val filteredTournaments: StateFlow<List<Tournament>> = combine(
        tournaments,
        _selectedFilterMode
    ) { list, mode ->
        when (mode) {
            GameMode.ALL -> list
            GameMode.FREE -> list.filter { it.entryFee == 0 }
            else -> list.filter { it.mode == mode }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered matches
    val upcomingMatches: StateFlow<List<MatchRecord>> = myMatches.combine(_matchesSubTab) { list, _ ->
        list.filter { it.status == com.example.core.model.TournamentStatus.UPCOMING }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedMatches: StateFlow<List<MatchRecord>> = myMatches.combine(_matchesSubTab) { list, _ ->
        list.filter { it.status == com.example.core.model.TournamentStatus.COMPLETED }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun navigateAuth(screen: AuthScreen) {
        _authScreen.value = screen
    }

    fun setBottomTab(tab: BottomTab) {
        _currentBottomTab.value = tab
    }

    fun setFilterMode(mode: GameMode) {
        _selectedFilterMode.value = mode
    }

    fun setMatchesSubTab(tab: MatchesSubTab) {
        _matchesSubTab.value = tab
    }

    fun setLeaderboardTab(tab: LeaderboardTab) {
        _leaderboardTab.value = tab
    }

    fun openTournamentDetails(tournament: Tournament) {
        _selectedTournamentDetails.value = tournament
    }

    fun closeTournamentDetails() {
        _selectedTournamentDetails.value = null
    }

    fun openProof(match: MatchRecord) {
        _selectedProofMatch.value = match
    }

    fun closeProof() {
        _selectedProofMatch.value = null
    }

    fun openEditProfile() {
        _showEditProfileDialog.value = true
    }

    fun closeEditProfile() {
        _showEditProfileDialog.value = false
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun showSnackbar(msg: String) {
        _snackbarMessage.value = msg
    }

    fun login(id: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val result = userRepo.login(id, pass)
        result.fold(
            onSuccess = {
                _snackbarMessage.value = "Welcome back, ${it.inGameName}!"
                onSuccess()
            },
            onFailure = {
                onError(it.localizedMessage ?: "Login failed")
            }
        )
    }

    fun loginWithGoogle(onSuccess: () -> Unit) {
        val result = userRepo.loginWithGoogle()
        result.fold(
            onSuccess = {
                _snackbarMessage.value = "Connected as ${it.inGameName}"
                onSuccess()
            },
            onFailure = {
                _snackbarMessage.value = "Google sign-in error"
            }
        )
    }

    fun signUp(
        ffUid: String,
        ign: String,
        phone: String,
        pass: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val result = userRepo.signUp(ffUid, ign, phone, pass)
        result.fold(
            onSuccess = {
                _snackbarMessage.value = "Account created! Welcome to FF Arena."
                onSuccess()
            },
            onFailure = {
                onError(it.localizedMessage ?: "Sign up failed")
            }
        )
    }

    fun logout() {
        userRepo.logout()
        _currentBottomTab.value = BottomTab.HOME
        _authScreen.value = AuthScreen.LOGIN
        _snackbarMessage.value = "Logged out successfully"
    }

    fun claimStreak() {
        val res = userRepo.claimDailyStreak()
        res.fold(
            onSuccess = { newStreak ->
                _snackbarMessage.value = "🔥 Day $newStreak Streak Claimed! +250 XP earned."
            },
            onFailure = {
                _snackbarMessage.value = it.localizedMessage ?: "Streak already claimed today"
            }
        )
    }

    fun joinTournament(tournamentId: String) {
        val res = tournamentRepo.joinTournament(tournamentId)
        res.fold(
            onSuccess = { t ->
                // Also update local selected detail state if open
                _selectedTournamentDetails.value = t
                // Update user profile stats immediately
                userRepo.incrementMatchStats(won = false, killsAdded = 0)
                _snackbarMessage.value = "Registered for ${t.title}! Check 'Matches' tab."
            },
            onFailure = {
                _snackbarMessage.value = it.localizedMessage ?: "Failed to join tournament"
            }
        )
    }

    fun updateProfile(name: String, ign: String, phone: String) {
        val res = userRepo.updateProfile(name, ign, phone)
        res.fold(
            onSuccess = {
                _showEditProfileDialog.value = false
                _snackbarMessage.value = "Profile updated successfully"
            },
            onFailure = {
                _snackbarMessage.value = it.localizedMessage ?: "Failed to update profile"
            }
        )
    }

    fun getLeaderboardEntries(): List<LeaderboardEntry> {
        return tournamentRepo.getLeaderboard(_leaderboardTab.value)
    }

    fun togglePaidEntryForPreview() {
        _paidEntryEnabled.value = !_paidEntryEnabled.value
        _snackbarMessage.value = if (_paidEntryEnabled.value) {
            "Testing Mode: Paid Entry & Wallet features enabled"
        } else {
            "India Compliance: Paid entry disabled (Free-to-enter tournaments only)"
        }
    }
}
