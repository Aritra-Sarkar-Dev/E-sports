package com.example.ui.model

import androidx.compose.runtime.mutableStateListOf

object TournamentService {
    val matches = mutableStateListOf<EsportsMatch>()
    val registrations = mutableStateListOf<TournamentRegistration>()
    val publishedResults = mutableStateListOf<PublishedResult>()
    val notifications = mutableStateListOf<InAppNotification>()
    val prizeRecords = mutableStateListOf<PrizeRecord>()
    val supportTickets = mutableStateListOf<SupportTicketRecord>()
    val disputeReports = mutableStateListOf<DisputeReportRecord>()
    val usersList = mutableStateListOf<UserManagementRecord>()
    val auditLogs = mutableStateListOf<AuditLogRecord>()

    init {
        resetToDefaults()
    }

    fun resetToDefaults() {
        matches.clear()
        matches.addAll(MockData.matchesList)

        registrations.clear()
        registrations.addAll(
            listOf(
                TournamentRegistration("reg_1", "m1", "CYBERFRAY 2077 GRAND FINALS", "NEON_SOUL", "994821034", "10m ago", "Confirmed"),
                TournamentRegistration("reg_2", "m1", "CYBERFRAY 2077 GRAND FINALS", "SOUL_IMMORTAL", "849201123", "25m ago", "Approved"),
                TournamentRegistration("reg_3", "m1", "CYBERFRAY 2077 GRAND FINALS", "VIPER_STRIKE", "771920441", "1h ago", "Confirmed"),
                TournamentRegistration("reg_4", "m3", "Kalahari Duo Masters Arena", "NEON_SOUL", "994821034", "Yesterday", "Confirmed")
            )
        )

        notifications.clear()
        notifications.addAll(MockData.notificationsList)

        prizeRecords.clear()
        prizeRecords.addAll(
            listOf(
                PrizeRecord("p1", "Midnight Blitz Champions League", "2nd Place", "OnePlus Gaming", "Pro Gaming Headset (Sponsor value ₹3,000)", "Yesterday", "Delivered by sponsor"),
                PrizeRecord("p2", "Kalahari Survival Clash Weekend", "1st Place (Booyah)", "Cosmic Byte", "Mechanical RGB Keyboard + Mouse (Sponsor value ₹5,000)", "22 Oct", "Sponsor notified"),
                PrizeRecord("p3", "CYBERFRAY 2077 GRAND FINALS", "3rd Place", "Asus ROG Gaming", "Official Esports Jersey + Cap (Sponsor value ₹2,000)", "15 Oct", "Recorded")
            )
        )

        supportTickets.clear()
        supportTickets.addAll(
            listOf(
                SupportTicketRecord("t1", "NEON_SOUL", "Room password verification", "Room credentials auto-revealed smoothly. Looking forward to kick-off!", "Closed", "Auto-revealed 15m prior to start."),
                SupportTicketRecord("t2", "VIPER_STRIKE", "Prize delivery tracking", "When will the gaming kit arrive via courier?", "Open", null)
            )
        )

        disputeReports.clear()
        disputeReports.addAll(
            listOf(
                DisputeReportRecord("rep_1", "CYBERFRAY 2077 GRAND FINALS", "NEON_SOUL", "ROGUE_CHEATER", "Suspected emulator / script usage in Round 2", "20m ago", "Pending", "Player was executing instant 360 snipes through smoke cover."),
                DisputeReportRecord("rep_2", "Midnight Blitz Champions League", "VIPER_STRIKE", "SHADOW_KILL", "Unfair cross-team collusion at Clock Tower", "Yesterday", "Resolved", "Match telemetry reviewed by referee. Warning issued.")
            )
        )

        usersList.clear()
        usersList.addAll(
            listOf(
                UserManagementRecord("u1", "NEON_SOUL", "994821034", "Active", 0),
                UserManagementRecord("u2", "SOUL_IMMORTAL", "849201123", "Active", 0),
                UserManagementRecord("u3", "VIPER_STRIKE", "771920441", "Warned", 1),
                UserManagementRecord("u4", "ROGUE_CHEATER", "119203948", "Banned", 3)
            )
        )

        auditLogs.clear()
        auditLogs.addAll(
            listOf(
                AuditLogRecord("a1", "Published results for CyberFray 2077", "admin@demo.com", "Today, 08:35 PM"),
                AuditLogRecord("a2", "Updated room credentials for Kalahari Duo", "organizer@demo.com", "Today, 06:10 PM"),
                AuditLogRecord("a3", "Resolved dispute report #rep_2", "admin@demo.com", "Yesterday, 11:45 PM")
            )
        )
    }

    fun createTournament(
        title: String,
        sponsor: String,
        mode: String,
        map: String,
        startTimeText: String,
        sponsorPrize: Int,
        maxSlots: Int,
        minLevel: Int = 30,
        teamSize: String = "4 Players",
        rulesText: String = "Mobile device only. Emulators prohibited. No teaming or scripts."
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
            perKillPoints = 2,
            minLevel = minLevel,
            teamSize = teamSize,
            maxSlots = maxSlots,
            joinedSlots = 1,
            status = "Upcoming",
            sponsor = sponsor.ifBlank { "Official Sponsor" },
            isJoined = false,
            rulesText = rulesText
        )
        matches.add(0, newMatch)
        auditLogs.add(0, AuditLogRecord("a_${System.currentTimeMillis()}", "Created tournament '$title'", "organizer@demo.com", "Just now"))
        return newMatch
    }

    fun cancelTournament(matchId: String) {
        val idx = matches.indexOfFirst { it.id == matchId }
        if (idx != -1) {
            val title = matches[idx].title
            matches.removeAt(idx)
            auditLogs.add(0, AuditLogRecord("a_${System.currentTimeMillis()}", "Cancelled tournament '$title'", "organizer@demo.com", "Just now"))
            notifications.add(0, InAppNotification(
                id = "notif_${System.currentTimeMillis()}",
                title = "Tournament Cancelled: $title",
                message = "The organizer has cancelled this tournament. Any registered tickets were refunded.",
                timeAgo = "Just now",
                type = "match",
                isRead = false
            ))
        }
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
            auditLogs.add(0, AuditLogRecord("a_${System.currentTimeMillis()}", "Set Room ID $roomId for '${old.title}'", "organizer@demo.com", "Just now"))
            notifications.add(
                0,
                InAppNotification(
                    id = "notif_${System.currentTimeMillis()}",
                    title = "Room Credentials Updated: ${old.title}",
                    message = if (revealImmediately) "Room ID: $roomId | Password: $roomPass. Join immediately!" else "Credentials registered. Auto-reveals 15m before kick-off.",
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
            registeredAt = "Just now",
            status = "Confirmed"
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

    fun approveRegistration(regId: String) {
        val idx = registrations.indexOfFirst { it.id == regId }
        if (idx != -1) {
            registrations[idx] = registrations[idx].copy(status = "Approved")
        }
    }

    fun removeRegistration(regId: String) {
        val idx = registrations.indexOfFirst { it.id == regId }
        if (idx != -1) {
            val reg = registrations.removeAt(idx)
            val match = matches.find { it.id == reg.tournamentId }
            if (match != null) {
                val mIdx = matches.indexOf(match)
                if (mIdx != -1) {
                    matches[mIdx] = match.copy(joinedSlots = (match.joinedSlots - 1).coerceAtLeast(0))
                }
            }
            auditLogs.add(0, AuditLogRecord("a_${System.currentTimeMillis()}", "Removed registration for '${reg.ign}'", "organizer@demo.com", "Just now"))
        }
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
        auditLogs.add(0, AuditLogRecord("a_${System.currentTimeMillis()}", "Published official results for '${result.tournamentTitle}'", "admin@demo.com", "Just now"))
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

    fun updatePrizeStatus(prizeId: String, newStatus: String) {
        val idx = prizeRecords.indexOfFirst { it.id == prizeId }
        if (idx != -1) {
            val old = prizeRecords[idx]
            prizeRecords[idx] = old.copy(status = newStatus)
            auditLogs.add(0, AuditLogRecord("a_${System.currentTimeMillis()}", "Updated prize status to '$newStatus' for '${old.tournamentTitle}'", "organizer@demo.com", "Just now"))
            notifications.add(
                0,
                InAppNotification(
                    id = "prize_${System.currentTimeMillis()}",
                    title = "Prize Status Updated: $newStatus",
                    message = "Your sponsor prize for '${old.tournamentTitle}' was marked '$newStatus'. Sponsor: ${old.sponsorName}.",
                    timeAgo = "Just now",
                    type = "reward",
                    isRead = false
                )
            )
        }
    }

    fun warnUser(userId: String) {
        val idx = usersList.indexOfFirst { it.id == userId }
        if (idx != -1) {
            val old = usersList[idx]
            usersList[idx] = old.copy(status = "Warned", warningsCount = old.warningsCount + 1)
            auditLogs.add(0, AuditLogRecord("a_${System.currentTimeMillis()}", "Issued warning to user '${old.ign}'", "admin@demo.com", "Just now"))
        }
    }

    fun banUser(userId: String) {
        val idx = usersList.indexOfFirst { it.id == userId }
        if (idx != -1) {
            val old = usersList[idx]
            usersList[idx] = old.copy(status = "Banned", warningsCount = old.warningsCount + 1)
            auditLogs.add(0, AuditLogRecord("a_${System.currentTimeMillis()}", "Banned user '${old.ign}' (UID: ${old.ffUid})", "admin@demo.com", "Just now"))
        }
    }

    fun unbanUser(userId: String) {
        val idx = usersList.indexOfFirst { it.id == userId }
        if (idx != -1) {
            val old = usersList[idx]
            usersList[idx] = old.copy(status = "Active")
            auditLogs.add(0, AuditLogRecord("a_${System.currentTimeMillis()}", "Unbanned user '${old.ign}'", "admin@demo.com", "Just now"))
        }
    }

    fun resolveDispute(disputeId: String, action: String) {
        val idx = disputeReports.indexOfFirst { it.id == disputeId }
        if (idx != -1) {
            disputeReports[idx] = disputeReports[idx].copy(status = action)
            auditLogs.add(0, AuditLogRecord("a_${System.currentTimeMillis()}", "Marked dispute #$disputeId as '$action'", "admin@demo.com", "Just now"))
        }
    }

    fun broadcastAnnouncement(title: String, message: String, audience: String) {
        notifications.add(
            0,
            InAppNotification(
                id = "ann_${System.currentTimeMillis()}",
                title = "📢 $title [$audience]",
                message = message,
                timeAgo = "Just now",
                type = "system",
                isRead = false
            )
        )
        auditLogs.add(0, AuditLogRecord("a_${System.currentTimeMillis()}", "Broadcasted announcement '$title' to $audience", "organizer@demo.com", "Just now"))
    }
}
