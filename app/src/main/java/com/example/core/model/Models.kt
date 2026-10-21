package com.example.core.model

enum class GameMode(val label: String) {
    ALL("All"),
    SOLO("Solo"),
    DUO("Duo"),
    SQUAD("Squad"),
    FREE("Free")
}

enum class DivisionTier(val label: String, val badgeColorHex: Long) {
    ROOKIE("ROOKIE", 0xFF00E5FF),
    MID("MID", 0xFF7B5CFF),
    PRO("PRO", 0xFFFFB300),
    OPEN("OPEN", 0xFFC8FF3D)
}

enum class TournamentStatus {
    UPCOMING,
    LIVE,
    COMPLETED
}

data class User(
    val id: String = "usr_001",
    val name: String = "Arjun Verma",
    val ffUid: String = "839472819",
    val inGameName: String = "⚡ViperSniper⚡",
    val phone: String = "+91 98765 43210",
    val email: String = "arjun.esports@arena.in",
    val avatarIndex: Int = 1,
    val level: Int = 42,
    val xp: Int = 6850,
    val xpNextLevel: Int = 10000,
    val currentStreak: Int = 5,
    val streakClaimedToday: Boolean = false,
    val matchesPlayed: Int = 138,
    val wins: Int = 47,
    val kills: Int = 612,
    val winRate: Float = 34.1f, // 34.1%
    val sponsorEarningsBalance: Double = 3450.0 // Sponsor prize winnings
)

data class Tournament(
    val id: String,
    val title: String,
    val subtitle: String = "Official Sponsor Cup",
    val mode: GameMode,
    val division: DivisionTier,
    val startTimestamp: Long,
    val startTimeFormatted: String,
    val prizePool: Int,
    val entryFee: Int = 0, // Always 0 unless enablePaidEntry is true
    val totalSlots: Int,
    val joinedSlots: Int,
    val status: TournamentStatus = TournamentStatus.UPCOMING,
    val rules: List<String>,
    val sponsor: String = "Monster Energy & Asus ROG",
    val mapName: String = "Bermuda Remastered",
    val roomId: String = "893241",
    val roomPassword: String = "ffa99",
    val isJoined: Boolean = false
) {
    val spotsLeft: Int get() = (totalSlots - joinedSlots).coerceAtLeast(0)
    val progress: Float get() = (joinedSlots.toFloat() / totalSlots.toFloat()).coerceIn(0f, 1f)
}

data class MatchRecord(
    val id: String,
    val tournamentId: String,
    val tournamentTitle: String,
    val mode: GameMode,
    val division: DivisionTier,
    val matchTimeMillis: Long,
    val matchTimeFormatted: String,
    val rank: Int?, // null if upcoming
    val kills: Int?, // null if upcoming
    val prizeWon: Int?, // null if upcoming
    val status: TournamentStatus,
    val roomId: String = "893241",
    val roomPassword: String = "ffa99",
    val proofVerified: Boolean = true,
    val proofSummary: String = "Verified by Chief Esports Referee • Match ID #FF-90218"
)

enum class LeaderboardTab {
    WEEKLY,
    SEASON,
    ALL_TIME
}

data class LeaderboardEntry(
    val rank: Int,
    val playerName: String,
    val inGameName: String,
    val avatarIndex: Int,
    val points: Int,
    val kills: Int,
    val wins: Int,
    val winRate: String,
    val isCurrentUser: Boolean = false
)

data class WalletTransaction(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val isCredit: Boolean,
    val dateFormatted: String,
    val status: String = "Success"
)
