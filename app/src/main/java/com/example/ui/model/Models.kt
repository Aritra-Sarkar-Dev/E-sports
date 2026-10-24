package com.example.ui.model

data class EsportsMatch(
    val id: String,
    val title: String,
    val mode: String, // Solo, Duo, Squad
    val division: String, // Rookie, Mid, Pro, Open
    val map: String, // Bermuda, Purgatory, Kalahari, Alpine
    val startTimeText: String,
    val entryFee: Int,
    val prizePool: Int,
    val perKillPrize: Int,
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
    val myWinnings: Int? = null
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
    val totalEarnings: Int,
    val referralCode: String,
    val linkedUpi: String,
    val isKycVerified: Boolean
)

data class WalletInfo(
    val deposit: Int,
    val winnings: Int,
    val bonus: Int,
    val total: Int = deposit + winnings + bonus
)

data class TransactionRecord(
    val id: String,
    val title: String,
    val type: String, // deposit, winning, entry_fee, withdrawal, bonus
    val amount: Int,
    val timeText: String,
    val isPositive: Boolean
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
    val description: String
)

data class LeaderboardRow(
    val rank: Int,
    val name: String,
    val ign: String,
    val earnings: Int,
    val wins: Int,
    val kills: Int
)

data class InAppNotification(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val type: String,
    val isRead: Boolean
)
