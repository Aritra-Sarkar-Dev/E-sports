package com.example.ui.model

data class EsportsMatch(
    val id: String,
    val title: String,
    val mode: String, // Solo, Duo, Squad
    val division: String, // Rookie, Mid, Pro, Open
    val map: String, // Bermuda, Purgatory, Kalahari, Alpine
    val startTimeText: String,
    val prizePool: Int,
    val perKillXp: Int = 10,
    val perKillPoints: Int = 2,
    val minLevel: Int = 30,
    val teamSize: String = "4 Players",
    val maxSlots: Int,
    val joinedSlots: Int,
    val status: String, // Upcoming, Live, Completed
    val sponsor: String,
    val isJoined: Boolean = false,
    val roomId: String? = null,
    val roomPass: String? = null,
    val isRoomRevealed: Boolean = false,
    val myRank: Int? = null,
    val myKills: Int? = null,
    val prizesWon: Int? = null,
    val rulesText: String = "Mobile device only. Emulators strictly prohibited. No hacks, teaming, or third-party scripts. Minimum Free Fire Level 30 required."
)

data class UserProfile(
    val name: String,
    val ign: String,
    val ffUid: String,
    val level: Int,
    val xp: Int,
    val maxXp: Int,
    val rankTier: String,
    val matches: Int,
    val wins: Int,
    val kills: Int,
    val kdRatio: Float,
    val winRate: Float,
    val prizesWon: Int,
    val referralCode: String,
    val role: String = "player" // player, organizer, admin
)

data class MissionRecord(
    val id: String,
    val title: String,
    val current: Int,
    val target: Int,
    val rewardCoins: Int,
    val isClaimed: Boolean,
    val type: String // Daily, Weekly
)

data class StoreRewardItem(
    val id: String,
    val title: String,
    val category: String,
    val coins: Int,
    val description: String,
    val isPurchased: Boolean = false
)

data class LeaderboardRow(
    val rank: Int,
    val name: String,
    val ign: String,
    val points: Int,
    val prizesWon: Int,
    val wins: Int,
    val kills: Int
)

data class InAppNotification(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val type: String, // room, system, reward, match
    val isRead: Boolean
)

data class TournamentRegistration(
    val id: String,
    val tournamentId: String,
    val tournamentTitle: String,
    val ign: String,
    val ffUid: String,
    val registeredAt: String = "Just now",
    val status: String = "Confirmed" // Confirmed, Approved, Removed
)

data class PublishedResult(
    val tournamentId: String,
    val tournamentTitle: String = "Tournament",
    val firstIgn: String,
    val firstUid: String,
    val firstKills: Int,
    val firstPrize: String, // e.g. "Trophy + Kit (Sponsor value ₹5,000)"
    val secondIgn: String,
    val secondUid: String,
    val secondKills: Int,
    val secondPrize: String,
    val thirdIgn: String,
    val thirdUid: String,
    val thirdKills: Int,
    val thirdPrize: String,
    val publishedAt: String = "Just now"
)

data class PrizeRecord(
    val id: String,
    val tournamentTitle: String,
    val rank: String,
    val sponsorName: String,
    val sponsorPrize: String, // e.g. "Pro Gaming Headset (Sponsor value ₹3,000)"
    val date: String,
    val status: String // Recorded, Sponsor notified, Delivered by sponsor
)

data class SupportTicketRecord(
    val id: String,
    val userIgn: String,
    val subject: String,
    val message: String,
    val status: String, // Open, Closed
    val reply: String? = null
)

data class DisputeReportRecord(
    val id: String,
    val tournamentTitle: String,
    val reporterIgn: String,
    val reportedIgn: String,
    val reason: String,
    val timestamp: String,
    val status: String = "Pending", // Pending, Resolved, Dismissed
    val details: String
)

data class AuditLogRecord(
    val id: String,
    val action: String,
    val adminName: String,
    val timestamp: String
)

data class UserManagementRecord(
    val id: String,
    val ign: String,
    val ffUid: String,
    val status: String, // Active, Warned, Banned
    val warningsCount: Int = 0
)
