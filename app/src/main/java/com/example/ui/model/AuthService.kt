package com.example.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object AuthService {
    // Current user session
    var currentUser by mutableStateOf<UserProfile?>(MockData.defaultProfile)
    var isLoggedIn by mutableStateOf(true)

    // Demo Accounts
    val demoAccounts = mapOf(
        "admin@demo.com" to Triple("admin123", "Super Administrator", "admin"),
        "organizer@demo.com" to Triple("demo123", "Apex Esports Organizer", "organizer"),
        "aritra@ffarena.com" to Triple("neon99", "Aritra Sarkar", "player")
    )

    fun login(email: String, pass: String): Result<UserProfile> {
        val cleanEmail = email.trim().lowercase()
        val match = demoAccounts[cleanEmail]
        if (match != null && match.first == pass) {
            val user = when (match.third) {
                "admin" -> UserProfile(
                    name = "Platform Administrator",
                    ign = "ADMIN_OVERWATCH",
                    ffUid = "100000001",
                    level = 75,
                    xp = 9999,
                    maxXp = 10000,
                    rankTier = "Grandmaster",
                    matches = 250,
                    wins = 120,
                    kills = 1200,
                    kdRatio = 6.5f,
                    winRate = 48.0f,
                    prizesWon = 25,
                    referralCode = "ADMIN_PRO",
                    role = "admin"
                )
                "organizer" -> UserProfile(
                    name = "Official Tournament Host",
                    ign = "HOST_APEX",
                    ffUid = "200000002",
                    level = 60,
                    xp = 7500,
                    maxXp = 8000,
                    rankTier = "Master",
                    matches = 140,
                    wins = 52,
                    kills = 580,
                    kdRatio = 4.8f,
                    winRate = 37.1f,
                    prizesWon = 12,
                    referralCode = "HOST99",
                    role = "organizer"
                )
                else -> MockData.defaultProfile
            }
            currentUser = user
            isLoggedIn = true
            return Result.success(user)
        } else if (cleanEmail.isNotEmpty() && pass.length >= 6) {
            // General player mock login
            val user = MockData.defaultProfile.copy(
                name = cleanEmail.substringBefore("@").replace(".", " ").capitalize(),
                role = "player"
            )
            currentUser = user
            isLoggedIn = true
            return Result.success(user)
        }
        return Result.failure(Exception("Invalid email or password. Use demo account or min 6 chars password."))
    }

    fun register(
        name: String,
        email: String,
        pass: String,
        ign: String,
        ffUid: String,
        is18Plus: Boolean,
        acceptedTerms: Boolean
    ): Result<UserProfile> {
        if (name.isBlank()) return Result.failure(Exception("Please enter your full name."))
        if (!email.contains("@") || !email.contains(".")) return Result.failure(Exception("Enter a valid email address."))
        if (pass.length < 6) return Result.failure(Exception("Password must be at least 6 characters."))
        if (ign.isBlank()) return Result.failure(Exception("Please enter your in-game name (IGN)."))
        if (!ffUid.matches(Regex("^[0-9]{8,12}$"))) {
            return Result.failure(Exception("Free Fire UID must be between 8 and 12 numeric digits."))
        }
        if (!is18Plus) return Result.failure(Exception("You must certify that you are 18 years or older."))
        if (!acceptedTerms) return Result.failure(Exception("You must accept the Fair Play & Esports Terms."))

        val newUser = UserProfile(
            name = name.trim(),
            ign = ign.trim(),
            ffUid = ffUid.trim(),
            level = 30,
            xp = 100,
            maxXp = 1000,
            rankTier = "Platinum",
            matches = 0,
            wins = 0,
            kills = 0,
            kdRatio = 0f,
            winRate = 0f,
            prizesWon = 0,
            referralCode = ign.take(4).uppercase() + (10..99).random(),
            role = "player"
        )
        currentUser = newUser
        isLoggedIn = true
        return Result.success(newUser)
    }

    fun logout() {
        isLoggedIn = false
        currentUser = null
    }
}
