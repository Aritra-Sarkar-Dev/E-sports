package com.example.ui.model

object MockData {
    val defaultProfile = UserProfile(
        name = "Aritra Sarkar",
        ign = "⚡NEON_SOUL⚡",
        ffUid = "994821034",
        level = 42,
        xp = 3450,
        maxXp = 5000,
        rankTier = "Heroic",
        matches = 48,
        wins = 14,
        kills = 182,
        kdRatio = 4.2f,
        winRate = 29.2f,
        totalEarnings = 8450,
        referralCode = "NEON99",
        linkedUpi = "aritra@oksbi",
        isKycVerified = true
    )

    val defaultWallet = WalletInfo(
        deposit = 120,
        winnings = 450,
        bonus = 80
    )

    val matchesList = listOf(
        EsportsMatch(
            id = "m1",
            title = "CYBERFRAY 2077 GRAND FINALS",
            mode = "Squad",
            division = "Pro",
            map = "Bermuda",
            startTimeText = "Today, 08:30 PM",
            entryFee = 50,
            prizePool = 25000,
            perKillPrize = 25,
            maxSlots = 48,
            joinedSlots = 44,
            status = "Upcoming",
            sponsor = "Asus ROG Gaming",
            isJoined = true,
            roomId = "884910",
            roomPass = "rog99",
            isRoomRevealed = true
        ),
        EsportsMatch(
            id = "m2",
            title = "Daily Purgatory Rush Solo",
            mode = "Solo",
            division = "Open",
            map = "Purgatory",
            startTimeText = "Today, 09:15 PM",
            entryFee = 20,
            prizePool = 5000,
            perKillPrize = 15,
            maxSlots = 48,
            joinedSlots = 38,
            status = "Upcoming",
            sponsor = "Monster Energy",
            isJoined = false
        ),
        EsportsMatch(
            id = "m3",
            title = "Kalahari Duo Masters Arena",
            mode = "Duo",
            division = "Mid",
            map = "Kalahari",
            startTimeText = "LIVE NOW",
            entryFee = 30,
            prizePool = 8000,
            perKillPrize = 20,
            maxSlots = 24,
            joinedSlots = 24,
            status = "Live",
            sponsor = "Red Bull Esports",
            isJoined = true,
            roomId = "661002",
            roomPass = "duo88",
            isRoomRevealed = true
        ),
        EsportsMatch(
            id = "m4",
            title = "Rookie Welcome Cup [FREE ENTRY]",
            mode = "Solo",
            division = "Rookie",
            map = "Bermuda",
            startTimeText = "Tomorrow, 11:00 AM",
            entryFee = 0,
            prizePool = 1500,
            perKillPrize = 5,
            maxSlots = 48,
            joinedSlots = 48,
            status = "Upcoming",
            sponsor = "FF Arena Academy",
            isJoined = false
        ),
        EsportsMatch(
            id = "m5",
            title = "Alpine Sniper Night Clan Wars",
            mode = "Squad",
            division = "Pro",
            map = "Alpine",
            startTimeText = "Tomorrow, 07:00 PM",
            entryFee = 100,
            prizePool = 50000,
            perKillPrize = 50,
            maxSlots = 48,
            joinedSlots = 28,
            status = "Upcoming",
            sponsor = "HyperX India",
            isJoined = false
        ),
        EsportsMatch(
            id = "m6",
            title = "Lone Wolf 1v1 Gladiator Bout",
            mode = "Solo",
            division = "Open",
            map = "Bermuda",
            startTimeText = "Tomorrow, 09:30 PM",
            entryFee = 25,
            prizePool = 3000,
            perKillPrize = 0,
            maxSlots = 16,
            joinedSlots = 12,
            status = "Upcoming",
            sponsor = "SteelSeries",
            isJoined = false
        ),
        EsportsMatch(
            id = "m7",
            title = "Midnight Blitz Champions League",
            mode = "Squad",
            division = "Pro",
            map = "Bermuda",
            startTimeText = "Yesterday, 11:30 PM",
            entryFee = 50,
            prizePool = 20000,
            perKillPrize = 20,
            maxSlots = 48,
            joinedSlots = 48,
            status = "Completed",
            sponsor = "OnePlus Gaming",
            isJoined = true,
            myRank = 2,
            myKills = 6,
            myWinnings = 1620
        ),
        EsportsMatch(
            id = "m8",
            title = "Kalahari Survival Clash Weekend",
            mode = "Solo",
            division = "Mid",
            map = "Kalahari",
            startTimeText = "22 Oct, 10:00 PM",
            entryFee = 30,
            prizePool = 7500,
            perKillPrize = 15,
            maxSlots = 48,
            joinedSlots = 48,
            status = "Completed",
            sponsor = "Cosmic Byte",
            isJoined = true,
            myRank = 1,
            myKills = 8,
            myWinnings = 3920
        )
    )

    val transactionsList = listOf(
        TransactionRecord("t1", "Prize Winnings: Midnight Blitz (Rank 2)", "winning", 1620, "Today, 02:45 AM", true),
        TransactionRecord("t2", "Match Entry: CYBERFRAY 2077 Finals", "entry_fee", 50, "Yesterday, 08:12 PM", false),
        TransactionRecord("t3", "UPI Add Money (Google Pay)", "deposit", 200, "Yesterday, 05:30 PM", true),
        TransactionRecord("t4", "Booyah! Kalahari Survival (Rank 1)", "winning", 3920, "22 Oct, 11:20 PM", true),
        TransactionRecord("t5", "Instant UPI Payout to aritra@oksbi", "withdrawal", 2500, "21 Oct, 04:10 PM", false),
        TransactionRecord("t6", "7-Day Check-in Streak Cash Bonus", "bonus", 50, "20 Oct, 09:00 AM", true),
        TransactionRecord("t7", "Match Entry: Kalahari Duo Masters", "entry_fee", 30, "19 Oct, 07:45 PM", false),
        TransactionRecord("t8", "UPI Add Money (PhonePe)", "deposit", 500, "18 Oct, 02:15 PM", true),
        TransactionRecord("t9", "Instant UPI Payout to aritra@oksbi", "withdrawal", 1200, "16 Oct, 10:05 PM", false),
        TransactionRecord("t10", "Match Entry: Alpine Sniper Night", "entry_fee", 100, "15 Oct, 06:30 PM", false)
    )

    val leaderboardList = listOf(
        LeaderboardRow(1, "Kabir Verma", "👑SOUL_IMMORTAL👑", 48500, 34, 412),
        LeaderboardRow(2, "Rohan Deshmukh", "⚡VIPER_STRIKE⚡", 34200, 26, 320),
        LeaderboardRow(3, "Sameer Khan", "🔥BLAZE_KID🔥", 28900, 21, 284),
        LeaderboardRow(4, "Aritra Sarkar (You)", "⚡NEON_SOUL⚡", 8450, 14, 182),
        LeaderboardRow(5, "Deepak Rao", "💀PHANTOM_KILL💀", 7200, 11, 154),
        LeaderboardRow(6, "Manish Rawat", "🎯HEADSHOT_PRO🎯", 6400, 9, 139),
        LeaderboardRow(7, "Aniket Sen", "🚀ROCKET_BOY🚀", 5800, 8, 122),
        LeaderboardRow(8, "Tanya Roy", "🌸VALKYRIE_FF🌸", 5100, 7, 108),
        LeaderboardRow(9, "Kunal Patil", "⚡TITAN_THUNDER⚡", 4500, 6, 95),
        LeaderboardRow(10, "Aryan Mehta", "🛡️APEX_SHIELD🛡️", 4100, 5, 88)
    )

    val missionsList = listOf(
        MissionRecord("m1", "Play 3 Battle Royale Matches", 2, 3, 50, false, "Daily"),
        MissionRecord("m2", "Achieve 1 Booyah! Victory", 1, 1, 150, false, "Daily"),
        MissionRecord("m3", "Accumulate 10 Headshot Kills", 7, 10, 80, false, "Daily"),
        MissionRecord("m4", "Participate in 2 Squad Tournaments", 2, 2, 200, true, "Weekly"),
        MissionRecord("m5", "Invite 1 Friend with Verified Level 30 UID", 0, 1, 250, false, "Weekly")
    )

    val storeList = listOf(
        StoreRewardItem("s1", "Neon Flame Animated Frame", "frame", 400, "Electrifying animated cyber frame for your profile."),
        StoreRewardItem("s2", "Viper Booyah Champion Badge", "badge", 250, "Gold-plated badge displayed next to your IGN."),
        StoreRewardItem("s3", "Free Entry Pass: ₹50 Match Ticket", "ticket", 600, "Redeem for 100% discount on any ₹50 tournament."),
        StoreRewardItem("s4", "Cyberpunk HUD Neon Theme", "theme", 800, "Custom UI color scheme with neon violet gradients.")
    )

    val notificationsList = listOf(
        InAppNotification("n1", "Room ID & Password Revealed! 🔑", "CYBERFRAY 2077 Grand Finals credentials are ready. Room ID: 884910 | Pass: rog99", "5m ago", "room", false),
        InAppNotification("n2", "₹1,620 Credited to Winnings! 💰", "Your Rank 2 prize for Midnight Blitz Champions League has been processed.", "2h ago", "wallet", false),
        InAppNotification("n3", "Day 5 Login Streak Claimed! 🔥", "+50 Loyalty Coins added to your rewards pouch. 2 days left to Day 7 mega bonus.", "1d ago", "reward", true),
        InAppNotification("n4", "Anti-Cheat Radar Update 🛡️", "Fair play protocol v4.2 activated. 42 rogue accounts banned in yesterday's audit.", "2d ago", "system", true)
    )
}
