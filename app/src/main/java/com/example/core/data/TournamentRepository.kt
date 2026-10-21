package com.example.core.data

import com.example.core.model.DivisionTier
import com.example.core.model.GameMode
import com.example.core.model.LeaderboardEntry
import com.example.core.model.LeaderboardTab
import com.example.core.model.MatchRecord
import com.example.core.model.Tournament
import com.example.core.model.TournamentStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface TournamentRepository {
    val tournamentsFlow: StateFlow<List<Tournament>>
    val myMatchesFlow: StateFlow<List<MatchRecord>>
    fun getTournamentById(id: String): Tournament?
    fun joinTournament(tournamentId: String): Result<Tournament>
    fun getLeaderboard(tab: LeaderboardTab): List<LeaderboardEntry>
    fun getHeroTournament(): Tournament
}

class MockTournamentRepositoryImpl : TournamentRepository {

    private val initialTournaments = listOf(
        Tournament(
            id = "tourn_hero_01",
            title = "CYBERFRAY 2077 GRAND FINALS",
            subtitle = "India Championship Cup • Sponsored by ROG",
            mode = GameMode.SQUAD,
            division = DivisionTier.PRO,
            startTimestamp = System.currentTimeMillis() + (42 * 60 * 1000), // in 42 mins
            startTimeFormatted = "Today, 08:30 PM IST",
            prizePool = 50000,
            entryFee = 0,
            totalSlots = 48,
            joinedSlots = 42,
            status = TournamentStatus.UPCOMING,
            sponsor = "Asus ROG & Monster Energy",
            mapName = "Bermuda Remastered",
            roomId = "982341",
            roomPassword = "rog99",
            isJoined = false,
            rules = listOf(
                "Emulators, iPads, and triggers strictly banned. Mobile phones only.",
                "All team members must have Free Fire Level 30+ with verified UID.",
                "Grenade spam limit: Max 2 active nades per team member.",
                "Room ID & Password unlocks 10 minutes prior to match start.",
                "Screen recording proof mandatory for Top 3 teams to claim prize."
            )
        ),
        Tournament(
            id = "tourn_02",
            title = "Bermuda Blitz Solo Showdown",
            subtitle = "Daily Fast-Paced Battle Royale",
            mode = GameMode.SOLO,
            division = DivisionTier.OPEN,
            startTimestamp = System.currentTimeMillis() + (8 * 60 * 1000), // in 8 mins (Room Unlocked!)
            startTimeFormatted = "Today, 08:00 PM IST",
            prizePool = 12000,
            entryFee = 0,
            totalSlots = 48,
            joinedSlots = 46,
            status = TournamentStatus.UPCOMING,
            sponsor = "Red Bull India",
            mapName = "Bermuda",
            roomId = "441209",
            roomPassword = "ffsolo",
            isJoined = true, // already joined to demonstrate room credentials flow!
            rules = listOf(
                "Solo mode only. Teaming up results in instant permanent disqualification.",
                "Gun skins attributes: OFF (Competitive balance).",
                "Proof required for kill claims above 10."
            )
        ),
        Tournament(
            id = "tourn_03",
            title = "Kalahari Duo Masters Cup",
            subtitle = "High Desert Tactical Duos",
            mode = GameMode.DUO,
            division = DivisionTier.MID,
            startTimestamp = System.currentTimeMillis() + (120 * 60 * 1000), // 2 hrs
            startTimeFormatted = "Today, 10:00 PM IST",
            prizePool = 20000,
            entryFee = 0,
            totalSlots = 24,
            joinedSlots = 18,
            status = TournamentStatus.UPCOMING,
            sponsor = "JioGames & Poco",
            mapName = "Kalahari",
            roomId = "773190",
            roomPassword = "duo99",
            isJoined = false,
            rules = listOf(
                "Duo teammates must register with matching guild tags.",
                "Standard esports competitive circle shrink speeds.",
                "Prizes distributed via UPI to team captain within 2 hours."
            )
        ),
        Tournament(
            id = "tourn_04",
            title = "Purgatory Rookies Championship",
            subtitle = "Dedicated tournament for emerging talent",
            mode = GameMode.SQUAD,
            division = DivisionTier.ROOKIE,
            startTimestamp = System.currentTimeMillis() + (240 * 60 * 1000), // 4 hrs
            startTimeFormatted = "Tomorrow, 02:00 PM IST",
            prizePool = 8000,
            entryFee = 0,
            totalSlots = 48,
            joinedSlots = 29,
            status = TournamentStatus.UPCOMING,
            sponsor = "Boat Lifestyle",
            mapName = "Purgatory",
            roomId = "651980",
            roomPassword = "rookie1",
            isJoined = false,
            rules = listOf(
                "Tier cap: Platinum IV and below only.",
                "Free for all players. Great for boosting your win rate.",
                "Top 5 receive official FF Arena Rookie Trophies."
            )
        ),
        Tournament(
            id = "tourn_05",
            title = "Clash Squad 4v4 Invitational",
            subtitle = "High adrenaline fast rounds",
            mode = GameMode.SQUAD,
            division = DivisionTier.PRO,
            startTimestamp = System.currentTimeMillis() + (360 * 60 * 1000),
            startTimeFormatted = "Tomorrow, 06:00 PM IST",
            prizePool = 25000,
            entryFee = 0,
            totalSlots = 16,
            joinedSlots = 14,
            status = TournamentStatus.UPCOMING,
            sponsor = "OnePlus Gaming",
            mapName = "CS Bermuda",
            roomId = "319488",
            roomPassword = "csgod",
            isJoined = false,
            rules = listOf(
                "First to 7 rounds wins the set.",
                "Limited ammo: OFF, Fall damage: ON.",
                "Double sniper tactic restricted to 1 player per squad."
            )
        )
    )

    private val _tournamentsFlow = MutableStateFlow(initialTournaments)
    override val tournamentsFlow: StateFlow<List<Tournament>> = _tournamentsFlow.asStateFlow()

    private val initialMatches = mutableListOf(
        // Upcoming match (Room unlocked because it starts in 8 minutes!)
        MatchRecord(
            id = "match_up_01",
            tournamentId = "tourn_02",
            tournamentTitle = "Bermuda Blitz Solo Showdown",
            mode = GameMode.SOLO,
            division = DivisionTier.OPEN,
            matchTimeMillis = System.currentTimeMillis() + (8 * 60 * 1000),
            matchTimeFormatted = "Today, 08:00 PM IST",
            rank = null,
            kills = null,
            prizeWon = null,
            status = TournamentStatus.UPCOMING,
            roomId = "441209",
            roomPassword = "ffsolo",
            proofVerified = true
        ),
        // Completed match 1
        MatchRecord(
            id = "match_comp_01",
            tournamentId = "tourn_past_01",
            tournamentTitle = "ROG Titan Invitational #44",
            mode = GameMode.SQUAD,
            division = DivisionTier.PRO,
            matchTimeMillis = System.currentTimeMillis() - (24 * 3600 * 1000),
            matchTimeFormatted = "Yesterday, 07:30 PM",
            rank = 1, // Booyah!
            kills = 9,
            prizeWon = 2500,
            status = TournamentStatus.COMPLETED,
            roomId = "112948",
            roomPassword = "gg1",
            proofVerified = true,
            proofSummary = "Rank #1 Booyah Verified • 9 Kills • Referee: Team ROG Audit"
        ),
        // Completed match 2
        MatchRecord(
            id = "match_comp_02",
            tournamentId = "tourn_past_02",
            tournamentTitle = "Monster Energy Duo Brawl",
            mode = GameMode.DUO,
            division = DivisionTier.MID,
            matchTimeMillis = System.currentTimeMillis() - (72 * 3600 * 1000),
            matchTimeFormatted = "3 Days Ago, 09:00 PM",
            rank = 3,
            kills = 5,
            prizeWon = 800,
            status = TournamentStatus.COMPLETED,
            roomId = "550192",
            roomPassword = "brawl",
            proofVerified = true,
            proofSummary = "Rank #3 Podium Finish • 5 Kills • Sponsor Disbursement Complete"
        ),
        // Completed match 3
        MatchRecord(
            id = "match_comp_03",
            tournamentId = "tourn_past_03",
            tournamentTitle = "Free Fire Rookie Cup #12",
            mode = GameMode.SOLO,
            division = DivisionTier.ROOKIE,
            matchTimeMillis = System.currentTimeMillis() - (120 * 3600 * 1000),
            matchTimeFormatted = "5 Days Ago, 04:00 PM",
            rank = 7,
            kills = 4,
            prizeWon = 150,
            status = TournamentStatus.COMPLETED,
            roomId = "998124",
            roomPassword = "fastff",
            proofVerified = true,
            proofSummary = "Top 10 Finish • 4 Kills • Certified by FF Arena Admin"
        )
    )

    private val _myMatchesFlow = MutableStateFlow<List<MatchRecord>>(initialMatches)
    override val myMatchesFlow: StateFlow<List<MatchRecord>> = _myMatchesFlow.asStateFlow()

    override fun getTournamentById(id: String): Tournament? {
        return _tournamentsFlow.value.find { it.id == id }
    }

    override fun joinTournament(tournamentId: String): Result<Tournament> {
        val currentList = _tournamentsFlow.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == tournamentId }
        if (index == -1) return Result.failure(IllegalArgumentException("Tournament not found"))

        val t = currentList[index]
        if (t.isJoined) {
            return Result.success(t)
        }
        if (t.spotsLeft <= 0) {
            return Result.failure(IllegalStateException("Tournament slots are completely full!"))
        }

        val updated = t.copy(
            joinedSlots = t.joinedSlots + 1,
            isJoined = true
        )
        currentList[index] = updated
        _tournamentsFlow.value = currentList

        // Immediately add to My Matches as an upcoming match!
        val newMatch = MatchRecord(
            id = "match_up_${System.currentTimeMillis() % 10000}",
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

        return Result.success(updated)
    }

    override fun getHeroTournament(): Tournament {
        return _tournamentsFlow.value.first()
    }

    override fun getLeaderboard(tab: LeaderboardTab): List<LeaderboardEntry> {
        return when (tab) {
            LeaderboardTab.WEEKLY -> listOf(
                LeaderboardEntry(1, "Kabir Singh", "★RAVANA_99★", 1, 2840, 94, 18, "52.8%"),
                LeaderboardEntry(2, "Rohan Sharma", "☠️HeadHunter☠️", 2, 2690, 88, 15, "48.2%"),
                LeaderboardEntry(3, "Dev Malhotra", "亗_SHADOW_亗", 3, 2410, 79, 14, "44.1%"),
                LeaderboardEntry(4, "Arjun Verma", "⚡ViperSniper⚡", 4, 2180, 68, 12, "34.1%", isCurrentUser = true),
                LeaderboardEntry(5, "Priya Nair", "✿QUEEN_BEE✿", 5, 2050, 61, 10, "32.0%"),
                LeaderboardEntry(6, "Aryan Joshi", "⚡THUNDERBOLT⚡", 6, 1890, 54, 9, "29.5%"),
                LeaderboardEntry(7, "Vikram Patel", "⚔️GLADIATOR⚔️", 7, 1750, 48, 8, "28.0%"),
                LeaderboardEntry(8, "Sameer Khan", "★SNIPER_KING★", 8, 1620, 45, 7, "26.4%"),
                LeaderboardEntry(9, "Karan Mehta", "☬DEMON_SLAYER☬", 9, 1510, 41, 6, "25.0%"),
                LeaderboardEntry(10, "Aakash Roy", "亗_PHOENIX_亗", 10, 1400, 38, 5, "23.8%")
            )
            LeaderboardTab.SEASON -> listOf(
                LeaderboardEntry(1, "Rohan Sharma", "☠️HeadHunter☠️", 2, 14890, 482, 92, "51.2%"),
                LeaderboardEntry(2, "Kabir Singh", "★RAVANA_99★", 1, 14210, 460, 88, "49.0%"),
                LeaderboardEntry(3, "Dev Malhotra", "亗_SHADOW_亗", 3, 13100, 415, 76, "45.3%"),
                LeaderboardEntry(4, "Sameer Khan", "★SNIPER_KING★", 8, 12050, 390, 70, "42.1%"),
                LeaderboardEntry(5, "Priya Nair", "✿QUEEN_BEE✿", 5, 11480, 365, 64, "39.8%"),
                LeaderboardEntry(6, "Arjun Verma", "⚡ViperSniper⚡", 4, 10890, 342, 59, "34.1%", isCurrentUser = true),
                LeaderboardEntry(7, "Aryan Joshi", "⚡THUNDERBOLT⚡", 6, 9980, 310, 51, "31.2%"),
                LeaderboardEntry(8, "Vikram Patel", "⚔️GLADIATOR⚔️", 7, 9200, 290, 46, "29.0%")
            )
            LeaderboardTab.ALL_TIME -> listOf(
                LeaderboardEntry(1, "Dev Malhotra", "亗_SHADOW_亗", 3, 58900, 1820, 340, "54.0%"),
                LeaderboardEntry(2, "Kabir Singh", "★RAVANA_99★", 1, 56400, 1760, 325, "52.1%"),
                LeaderboardEntry(3, "Rohan Sharma", "☠️HeadHunter☠️", 2, 54100, 1690, 310, "50.4%"),
                LeaderboardEntry(4, "Priya Nair", "✿QUEEN_BEE✿", 5, 49200, 1510, 275, "46.2%"),
                LeaderboardEntry(5, "Arjun Verma", "⚡ViperSniper⚡", 4, 46800, 1420, 258, "34.1%", isCurrentUser = true),
                LeaderboardEntry(6, "Sameer Khan", "★SNIPER_KING★", 8, 44100, 1350, 240, "41.0%")
            )
        }
    }
}
