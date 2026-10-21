package com.example.core.data

import android.content.Context
import android.content.SharedPreferences
import com.example.core.config.AppConfig
import com.example.core.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface UserRepository {
    val currentUserFlow: StateFlow<User?>
    fun getCurrentUser(): User?
    fun login(identifier: String, password: String): Result<User>
    fun loginWithGoogle(): Result<User>
    fun signUp(ffUid: String, ign: String, phone: String, password: String): Result<User>
    fun logout()
    fun claimDailyStreak(): Result<Int>
    fun updateProfile(name: String, ign: String, phone: String): Result<User>
    fun incrementMatchStats(won: Boolean, killsAdded: Int)
}

class MockUserRepositoryImpl(context: Context) : UserRepository {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("ff_arena_user_prefs", Context.MODE_PRIVATE)

    private val _currentUserFlow = MutableStateFlow<User?>(null)
    override val currentUserFlow: StateFlow<User?> = _currentUserFlow.asStateFlow()

    init {
        // Restore persisted session if exists
        val isLoggedIn = prefs.getBoolean("key_logged_in", true) // Default true for instant preview testing
        if (isLoggedIn) {
            val savedUid = prefs.getString("key_ff_uid", "839472819") ?: "839472819"
            val savedIgn = prefs.getString("key_ign", "⚡ViperSniper⚡") ?: "⚡ViperSniper⚡"
            val savedName = prefs.getString("key_name", "Arjun Verma") ?: "Arjun Verma"
            val savedPhone = prefs.getString("key_phone", "+91 98765 43210") ?: "+91 98765 43210"
            val savedStreak = prefs.getInt("key_streak", 5)
            val streakClaimed = prefs.getBoolean("key_streak_claimed", false)
            val matches = prefs.getInt("key_matches", 138)
            val wins = prefs.getInt("key_wins", 47)
            val kills = prefs.getInt("key_kills", 612)

            _currentUserFlow.value = User(
                id = "usr_001",
                name = savedName,
                ffUid = savedUid,
                inGameName = savedIgn,
                phone = savedPhone,
                email = "arjun.esports@arena.in",
                avatarIndex = 1,
                level = 42,
                xp = 6850,
                xpNextLevel = 10000,
                currentStreak = savedStreak,
                streakClaimedToday = streakClaimed,
                matchesPlayed = matches,
                wins = wins,
                kills = kills,
                winRate = if (matches > 0) ((wins.toFloat() / matches.toFloat()) * 100f) else 0f,
                sponsorEarningsBalance = 3450.0
            )
        }
    }

    override fun getCurrentUser(): User? = _currentUserFlow.value

    override fun login(identifier: String, password: String): Result<User> {
        if (identifier.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Please enter your email/phone and password"))
        }
        val user = User(
            id = "usr_001",
            name = if (identifier.contains("@")) identifier.substringBefore("@").replaceFirstChar { it.uppercase() } else "Arjun Verma",
            ffUid = "839472819",
            inGameName = "⚡ViperSniper⚡",
            phone = if (!identifier.contains("@")) identifier else "+91 98765 43210",
            email = if (identifier.contains("@")) identifier else "arjun.esports@arena.in"
        )
        persistUser(user)
        _currentUserFlow.value = user
        return Result.success(user)
    }

    override fun loginWithGoogle(): Result<User> {
        val user = User(
            id = "usr_google_01",
            name = "Aritra Sarkar",
            ffUid = "994012845",
            inGameName = "★DarkKratos★",
            phone = "+91 98301 23456",
            email = "aritrasarkar509@gmail.com"
        )
        persistUser(user)
        _currentUserFlow.value = user
        return Result.success(user)
    }

    override fun signUp(ffUid: String, ign: String, phone: String, password: String): Result<User> {
        if (ffUid.length < 8) {
            return Result.failure(IllegalArgumentException("Free Fire UID must be at least 8 digits"))
        }
        if (ign.isBlank()) {
            return Result.failure(IllegalArgumentException("In-game name cannot be empty"))
        }
        if (phone.length < 10) {
            return Result.failure(IllegalArgumentException("Enter a valid 10-digit mobile number"))
        }

        val newUser = User(
            id = "usr_${System.currentTimeMillis() % 10000}",
            name = ign.replace(Regex("[^A-Za-z0-9 ]"), "").trim().ifBlank { "Pro Player" },
            ffUid = ffUid.trim(),
            inGameName = ign.trim(),
            phone = phone.trim(),
            email = "${ffUid.trim()}@player.arena.in",
            avatarIndex = 2,
            level = 1,
            xp = 100,
            xpNextLevel = 1000,
            currentStreak = 1,
            streakClaimedToday = true,
            matchesPlayed = 0,
            wins = 0,
            kills = 0,
            winRate = 0f,
            sponsorEarningsBalance = 100.0 // Welcome bonus from sponsor
        )
        persistUser(newUser)
        _currentUserFlow.value = newUser
        return Result.success(newUser)
    }

    override fun logout() {
        prefs.edit().putBoolean("key_logged_in", false).apply()
        _currentUserFlow.value = null
    }

    override fun claimDailyStreak(): Result<Int> {
        val current = _currentUserFlow.value ?: return Result.failure(IllegalStateException("Not logged in"))
        if (current.streakClaimedToday) {
            return Result.failure(IllegalStateException("Daily streak already claimed today! Check back tomorrow."))
        }
        val updatedStreak = current.currentStreak + 1
        val updated = current.copy(
            currentStreak = updatedStreak,
            streakClaimedToday = true,
            xp = current.xp + 250,
            sponsorEarningsBalance = current.sponsorEarningsBalance + 25.0
        )
        persistUser(updated)
        _currentUserFlow.value = updated
        return Result.success(updatedStreak)
    }

    override fun updateProfile(name: String, ign: String, phone: String): Result<User> {
        val current = _currentUserFlow.value ?: return Result.failure(IllegalStateException("Not logged in"))
        val updated = current.copy(
            name = name.ifBlank { current.name },
            inGameName = ign.ifBlank { current.inGameName },
            phone = phone.ifBlank { current.phone }
        )
        persistUser(updated)
        _currentUserFlow.value = updated
        return Result.success(updated)
    }

    override fun incrementMatchStats(won: Boolean, killsAdded: Int) {
        val current = _currentUserFlow.value ?: return
        val newMatches = current.matchesPlayed + 1
        val newWins = if (won) current.wins + 1 else current.wins
        val newKills = current.kills + killsAdded
        val updated = current.copy(
            matchesPlayed = newMatches,
            wins = newWins,
            kills = newKills,
            winRate = ((newWins.toFloat() / newMatches.toFloat()) * 100f)
        )
        persistUser(updated)
        _currentUserFlow.value = updated
    }

    private fun persistUser(user: User) {
        prefs.edit()
            .putBoolean("key_logged_in", true)
            .putString("key_ff_uid", user.ffUid)
            .putString("key_ign", user.inGameName)
            .putString("key_name", user.name)
            .putString("key_phone", user.phone)
            .putInt("key_streak", user.currentStreak)
            .putBoolean("key_streak_claimed", user.streakClaimedToday)
            .putInt("key_matches", user.matchesPlayed)
            .putInt("key_wins", user.wins)
            .putInt("key_kills", user.kills)
            .apply()
    }
}
