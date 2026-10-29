package com.example.ui.model

object MockData {
    val defaultProfile = UserProfile(
        name = "Aritra Sarkar",
        ign = "NEON_SOUL",
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
        prizesWon = 3,
        referralCode = "NEON99",
        role = "player"
    )

    val matchesList = listOf(
        EsportsMatch(
            id = "m1",
            title = "CYBERFRAY 2077 GRAND FINALS",
            mode = "Squad",
            division = "Pro",
            map = "Bermuda",
            startTimeText = "Today, 08:30 PM",
            prizePool = 50000,
            perKillXp = 10,
            perKillPoints = 2,
            minLevel = 30,
            teamSize = "4 Players",
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
            prizePool = 5000,
            perKillXp = 10,
            perKillPoints = 2,
            minLevel = 25,
            teamSize = "1 Player",
            maxSlots = 48,
            joinedSlots = 45, // < 10% left! (3 slots left)
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
            prizePool = 12000,
            perKillXp = 10,
            perKillPoints = 2,
            minLevel = 30,
            teamSize = "2 Players",
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
            prizePool = 3000,
            perKillXp = 10,
            perKillPoints = 1,
            minLevel = 20,
            teamSize = "1 Player",
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
            prizePool = 25000,
            perKillXp = 10,
            perKillPoints = 2,
            minLevel = 35,
            teamSize = "4 Players",
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
            prizePool = 5000,
            perKillXp = 10,
            perKillPoints = 2,
            minLevel = 25,
            teamSize = "1 Player",
            maxSlots = 16,
            joinedSlots = 15, // 1 slot left!
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
            prizePool = 20000,
            perKillXp = 10,
            perKillPoints = 2,
            minLevel = 30,
            teamSize = "4 Players",
            maxSlots = 48,
            joinedSlots = 48,
            status = "Completed",
            sponsor = "OnePlus Gaming",
            isJoined = true,
            myRank = 2,
            myKills = 6,
            prizesWon = 1
        ),
        EsportsMatch(
            id = "m8",
            title = "Kalahari Survival Clash Weekend",
            mode = "Solo",
            division = "Mid",
            map = "Kalahari",
            startTimeText = "22 Oct, 10:00 PM",
            prizePool = 10000,
            perKillXp = 10,
            perKillPoints = 2,
            minLevel = 30,
            teamSize = "1 Player",
            maxSlots = 48,
            joinedSlots = 48,
            status = "Completed",
            sponsor = "Cosmic Byte",
            isJoined = true,
            myRank = 1,
            myKills = 8,
            prizesWon = 1
        )
    )

    val leaderboardList = listOf(
        LeaderboardRow(1, "Kabir Verma", "SOUL_IMMORTAL", 1450, 12, 34, 412),
        LeaderboardRow(2, "Rohan Deshmukh", "VIPER_STRIKE", 1120, 8, 26, 320),
        LeaderboardRow(3, "Sameer Khan", "BLAZE_KID", 940, 6, 21, 284),
        LeaderboardRow(4, "Aritra Sarkar (You)", "NEON_SOUL", 680, 4, 14, 182),
        LeaderboardRow(5, "Deepak Rao", "PHANTOM_KILL", 550, 3, 11, 154),
        LeaderboardRow(6, "Manish Rawat", "HEADSHOT_PRO", 480, 3, 9, 139),
        LeaderboardRow(7, "Aniket Sen", "ROCKET_BOY", 410, 2, 8, 122),
        LeaderboardRow(8, "Tanya Roy", "VALKYRIE_FF", 360, 2, 7, 108),
        LeaderboardRow(9, "Kunal Patil", "TITAN_THUNDER", 310, 1, 6, 95),
        LeaderboardRow(10, "Aryan Mehta", "APEX_SHIELD", 270, 1, 5, 88)
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
        StoreRewardItem("s3", "VIP Bonus Ticket", "ticket", 500, "Bonus VIP entry ticket for exclusive sponsor showdowns."),
        StoreRewardItem("s4", "Cyberpunk HUD Neon Theme", "theme", 800, "Custom UI color scheme with neon violet gradients.")
    )

    val notificationsList = listOf(
        InAppNotification("n1", "Room ID & Password Revealed! 🔑", "CYBERFRAY 2077 Grand Finals credentials are ready. Room ID: 884910 | Pass: rog99", "5m ago", "room", false),
        InAppNotification("n2", "Sponsor Trophy Awarded! 🏆", "You earned a 2nd Place Sponsor Trophy for Midnight Blitz Champions League.", "2h ago", "match", false),
        InAppNotification("n3", "Day 5 Login Streak Claimed! 🔥", "+50 Loyalty Coins added to your pouch. 2 days left to Day 7 mega bonus.", "1d ago", "reward", true),
        InAppNotification("n4", "Anti-Cheat Radar Update 🛡️", "Fair play protocol v4.2 activated. 42 rogue accounts banned in yesterday's audit.", "2d ago", "system", true)
    )
}
