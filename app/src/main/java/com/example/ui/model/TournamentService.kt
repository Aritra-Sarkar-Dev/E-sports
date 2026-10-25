package com.example.ui.model

import androidx.compose.runtime.mutableStateListOf

object TournamentService {
    val matches = mutableStateListOf<EsportsMatch>()
    val registrations = mutableStateListOf<TournamentRegistration>()
    val publishedResults = mutableStateListOf<PublishedResult>()
    val notifications = mutableStateListOf<InAppNotification>()

    init {
        resetToDefaults()
    }

    fun resetToDefaults() {
        matches.clear()
        matches.addAll(MockData.matchesList)

        registrations.clear()
        registrations.addAll(
            listOf(
                TournamentRegistration("reg_1", "m1", "CYBERFRAY 2077 GRAND FINALS", "⚡NEON_SOUL⚡", "994821034", "10m ago"),
                TournamentRegistration("reg_2", "m1", "CYBERFRAY 2077 GRAND FINALS", "SOUL_IMMORTAL", "849201123", "25m ago"),
                TournamentRegistration("reg_3", "m1", "CYBERFRAY 2077 GRAND FINALS", "VIPER_STRIKE", "771920441", "1h ago"),
                TournamentRegistration("reg_4", "m3", "Kalahari Duo Masters Arena", "⚡NEON_SOUL⚡", "994821034", "Yesterday")
            )
        )

        notifications.clear()
        notifications.addAll(MockData.notificationsList)
    }

    fun createTournament(
        title: String,
        sponsor: String,
        mode: String,
        map: String,
        startTimeText: String,
        sponsorPrize: Int,
        maxSlots: Int
    ): EsportsMatch {
        val newMatch = EsportsMatch(
            id = "t_${System.currentTimeMillis()}",
            title = title,
            mode = mode,
            division = "Open",
            map = map,
            startTimeText = startTimeText,
            prizePool = sponsorPrize,
            perKillXp = 10,
            maxSlots = maxSlots,
            joinedSlots = 1,
            status = "Upcoming",
            sponsor = sponsor.ifBlank { "Official Sponsor" },
            isJoined = false
        )
        matches.add(0, newMatch)
        return newMatch
    }

    fun setRoomCredentials(
        matchId: String,
        roomId: String,
        roomPass: String,
        revealImmediately: Boolean
    ) {
        val index = matches.indexOfFirst { it.id == matchId }
        if (index != -1) {
            val old = matches[index]
            matches[index] = old.copy(
                roomId = roomId,
                roomPass = roomPass,
                isRoomRevealed = revealImmediately
            )
            // Send in-app notification to contestants
            notifications.add(
                0,
                InAppNotification(
                    id = "notif_${System.currentTimeMillis()}",
                    title = "Room Credentials Updated: ${old.title}",
                    message = "Room ID: $roomId | Password: $roomPass. Room opens 15m before match.",
                    timeAgo = "Just now",
                    type = "room",
                    isRead = false
                )
            )
        }
    }

    fun addRegistration(
        tournamentId: String,
        ign: String,
        ffUid: String
    ): TournamentRegistration {
        val match = matches.find { it.id == tournamentId }
        val reg = TournamentRegistration(
            id = "reg_${System.currentTimeMillis()}",
            tournamentId = tournamentId,
            tournamentTitle = match?.title ?: "Tournament",
            ign = ign,
            ffUid = ffUid,
            registeredAt = "Just now"
        )
        registrations.add(0, reg)
        if (match != null) {
            val idx = matches.indexOf(match)
            if (idx != -1) {
                matches[idx] = match.copy(joinedSlots = (match.joinedSlots + 1).coerceAtMost(match.maxSlots))
            }
        }
        return reg
    }

    fun publishResults(result: PublishedResult) {
        publishedResults.add(0, result)
        val match = matches.find { it.id == result.tournamentId }
        if (match != null) {
            val idx = matches.indexOf(match)
            if (idx != -1) {
                matches[idx] = match.copy(status = "Completed")
            }
        }
        notifications.add(
            0,
            InAppNotification(
                id = "res_${System.currentTimeMillis()}",
                title = "Tournament Results Published! 🏆",
                message = "1st: ${result.firstIgn} (${result.firstPrize}) • 2nd: ${result.secondIgn} • 3rd: ${result.thirdIgn}",
                timeAgo = "Just now",
                type = "match",
                isRead = false
            )
        )
    }

    fun sendAnnouncement(title: String, message: String, type: String = "system") {
        notifications.add(
            0,
            InAppNotification(
                id = "ann_${System.currentTimeMillis()}",
                title = title,
                message = message,
                timeAgo = "Just now",
                type = type,
                isRead = false
            )
        )
    }
}
