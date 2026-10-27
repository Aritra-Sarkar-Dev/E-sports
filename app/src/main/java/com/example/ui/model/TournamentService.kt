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

    val prizeRecords = mutableStateListOf(
        PrizeRecord("p1", "Midnight Blitz Champions League", "2nd Place", "OnePlus Gaming", "₹3,000 Trophy", "Yesterday", "Delivered by sponsor"),
        PrizeRecord("p2", "Kalahari Survival Clash Weekend", "1st Place (Booyah)", "Cosmic Byte", "₹5,000 Gaming Kit", "22 Oct", "Sponsor notified"),
        PrizeRecord("p3", "CyberFrey 2077 Grand Finals", "3rd Place", "Asus ROG Gaming", "₹2,000 Voucher", "15 Oct", "Recorded")
    )

    val supportTickets = mutableStateListOf(
        SupportTicketRecord("t1", "ViperSniper", "Room password issue", "Room password was delayed by 2 minutes.", "Open", null),
        SupportTicketRecord("t2", "SoulImmortal", "Prize delivery query", "When will the gaming kit arrive?", "Closed", "Shipped via courier tracking #IND88392")
    )

    val auditLogs = mutableStateListOf(
        AuditLogRecord("a1", "Published results for CyberFray 2077", "admin@demo.com", "Today, 08:35 PM"),
        AuditLogRecord("a2", "Updated room credentials for Kalahari Duo", "organizer@demo.com", "Today, 06:10 PM")
    )

    val usersList = mutableStateListOf(
        UserManagementRecord("u1", "ViperSniper", "994821034", "Active"),
        UserManagementRecord("u2", "SoulImmortal", "849201123", "Active"),
        UserManagementRecord("u3", "RogueCheater", "119203948", "Banned")
    )

    fun updatePrizeStatus(prizeId: String, newStatus: String) {
        val idx = prizeRecords.indexOfFirst { it.id == prizeId }
        if (idx != -1) {
            val old = prizeRecords[idx]
            prizeRecords[idx] = old.copy(status = newStatus)
            // Trigger notification
            notifications.add(
                0,
                InAppNotification(
                    id = "pnotif_${System.currentTimeMillis()}",
                    title = "Prize Status Update! 🏆",
                    message = "Your prize from ${old.tournamentTitle} was marked as '$newStatus'.",
                    timeAgo = "Just now",
                    type = "match",
                    isRead = false
                )
            )
        }
    }

    fun replyTicket(ticketId: String, replyMsg: String) {
        val idx = supportTickets.indexOfFirst { it.id == ticketId }
        if (idx != -1) {
            val t = supportTickets[idx]
            supportTickets[idx] = t.copy(status = "Closed", reply = replyMsg)
        }
    }

    fun updateUserStatus(userId: String, newStatus: String) {
        val idx = usersList.indexOfFirst { it.id == userId }
        if (idx != -1) {
            val u = usersList[idx]
            usersList[idx] = u.copy(status = newStatus)
        }
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

