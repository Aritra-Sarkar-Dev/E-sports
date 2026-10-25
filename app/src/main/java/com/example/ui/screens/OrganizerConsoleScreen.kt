package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.model.EsportsMatch
import com.example.ui.model.PublishedResult
import com.example.ui.model.TournamentService
import com.example.ui.theme.*

@Composable
fun OrganizerConsoleScreen(
    onClose: () -> Unit,
    onShowToast: (String) -> Unit
) {
    var activeSection by remember { mutableStateOf("Dashboard") }
    val sections = listOf("Dashboard", "Create", "Set Room ID", "Registrations", "Results", "Announcement")

    // Create state
    var title by remember { mutableStateOf("") }
    var sponsor by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf("Squad") }
    var map by remember { mutableStateOf("Bermuda") }
    var timeText by remember { mutableStateOf("Today, 09:00 PM") }
    var prizeText by remember { mutableStateOf("10000") }
    var slotsText by remember { mutableStateOf("48") }

    // Room ID state
    var selectedMatchId by remember { mutableStateOf(TournamentService.matches.firstOrNull()?.id ?: "") }
    var roomIdInput by remember { mutableStateOf("") }
    var roomPassInput by remember { mutableStateOf("") }

    // Registrations state
    var newRegIgn by remember { mutableStateOf("") }
    var newRegUid by remember { mutableStateOf("") }

    // Results state
    var resultMatchId by remember { mutableStateOf(TournamentService.matches.firstOrNull()?.id ?: "") }
    var firstIgn by remember { mutableStateOf("") }
    var firstUid by remember { mutableStateOf("") }
    var firstKills by remember { mutableStateOf("12") }
    var firstPrize by remember { mutableStateOf("₹5,000 Trophy + Kit") }
    var secondIgn by remember { mutableStateOf("") }
    var secondKills by remember { mutableStateOf("8") }

    // Announcement state
    var announceTitle by remember { mutableStateOf("") }
    var announceMsg by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        // Top Header with Close
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CardSurface)
                            .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Organizer Console", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = NeonLime,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text("ACTIVE", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color.Black, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                            }
                        }
                        Text("Free Fire Custom Rooms & Sponsor Cups", fontSize = 10.sp, color = TextSecondary)
                    }
                }

                Text(
                    text = "Close",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.clickable { onClose() }
                )
            }
        }

        // Section Tabs
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(sections) { sec ->
                    val isSelected = activeSection == sec
                    Surface(
                        modifier = Modifier.clickable { activeSection = sec },
                        color = if (isSelected) NeonLime else CardSurface,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NeonLime else CardBorder)
                    ) {
                        Text(
                            text = sec,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            color = if (isSelected) Color.Black else TextSecondary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // SECTION 1: DASHBOARD (COUNTS & METRICS)
        if (activeSection == "Dashboard") {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Tournaments Count
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("TOURNAMENTS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                                Text("${TournamentService.matches.size}", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color.White)
                                Text("${TournamentService.matches.count { it.status == "Live" }} Live Now", fontSize = 10.sp, color = NeonLime, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Registrations Count
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("REGISTRATIONS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                                Text("${TournamentService.registrations.size}", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color.White)
                                Text("Active Contestants", fontSize = 10.sp, color = ElectricViolet, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Sponsor Prize Pools
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("SPONSOR POOLS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                                val totalPool = TournamentService.matches.sumOf { it.prizePool }
                                Text("₹$totalPool", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color.White)
                                Text("100% Sponsor Funded", fontSize = 10.sp, color = GoldCoins)
                            }
                        }

                        // Configured Rooms
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("ROOM SECURITY", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                                val configured = TournamentService.matches.count { it.roomId != null }
                                Text("$configured/${TournamentService.matches.size}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color.White)
                                Text("Credentials Ready", fontSize = 10.sp, color = NeonLime)
                            }
                        }
                    }

                    // Quick Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { activeSection = "Create" },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("+ Create Cup", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Black)
                        }

                        Button(
                            onClick = { activeSection = "Set Room ID" },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = ElevatedSurface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Set Room ID", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("ACTIVE TOURNAMENT LIST", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 1.sp)
                }
            }

            items(TournamentService.matches) { m ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(m.title, fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                                Text("${m.mode} • ${m.map} • Sponsor: ${m.sponsor}", fontSize = 10.sp, color = TextSecondary)
                            }
                            Text("₹${m.prizePool}", fontSize = 12.sp, fontWeight = FontWeight.Black, color = NeonLime)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Room: ${m.roomId ?: "Not set"}", fontSize = 10.sp, color = if (m.roomId != null) NeonLime else TextSecondary)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Config",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonLime,
                                    modifier = Modifier.clickable {
                                        selectedMatchId = m.id
                                        roomIdInput = m.roomId ?: ""
                                        roomPassInput = m.roomPass ?: ""
                                        activeSection = "Set Room ID"
                                    }
                                )
                                Text(
                                    text = "Results",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricViolet,
                                    modifier = Modifier.clickable {
                                        resultMatchId = m.id
                                        activeSection = "Results"
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // SECTION 2: CREATE TOURNAMENT
        if (activeSection == "Create") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Create Tournament", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("100% Free Entry • Sponsor Funded", fontSize = 10.sp, color = NeonLime)

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Tournament Title", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = sponsor,
                            onValueChange = { sponsor = it },
                            label = { Text("Sponsor Name (e.g. Red Bull Esports)", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = mode,
                                onValueChange = { mode = it },
                                label = { Text("Mode (Solo/Duo/Squad)", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = map,
                                onValueChange = { map = it },
                                label = { Text("Map (Bermuda/etc)", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = prizeText,
                                onValueChange = { prizeText = it },
                                label = { Text("Sponsor Prize (₹)", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = slotsText,
                                onValueChange = { slotsText = it },
                                label = { Text("Max Slots", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        OutlinedTextField(
                            value = timeText,
                            onValueChange = { timeText = it },
                            label = { Text("Kick-off Time Text", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = {
                                if (title.isBlank()) {
                                    onShowToast("Please enter a title")
                                    return@Button
                                }
                                val pz = prizeText.toIntOrNull() ?: 10000
                                val sl = slotsText.toIntOrNull() ?: 48
                                TournamentService.createTournament(title, sponsor, mode, map, timeText, pz, sl)
                                onShowToast("Created tournament: $title")
                                title = ""
                                sponsor = ""
                                activeSection = "Dashboard"
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Publish Tournament", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // SECTION 3: SET ROOM ID AND PASSWORD
        if (activeSection == "Set Room ID") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Set Room ID & Password", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("Credentials unlock for registered players", fontSize = 10.sp, color = TextSecondary)

                        Text("Select Tournament:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(TournamentService.matches) { m ->
                                val isSelected = selectedMatchId == m.id
                                Surface(
                                    modifier = Modifier.clickable {
                                        selectedMatchId = m.id
                                        roomIdInput = m.roomId ?: ""
                                        roomPassInput = m.roomPass ?: ""
                                    },
                                    color = if (isSelected) NeonLime else ElevatedSurface,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                                ) {
                                    Text(
                                        m.title,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.Black else Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = roomIdInput,
                            onValueChange = { roomIdInput = it },
                            label = { Text("Free Fire Room ID", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = roomPassInput,
                            onValueChange = { roomPassInput = it },
                            label = { Text("Room Password", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                if (selectedMatchId.isBlank() || roomIdInput.isBlank() || roomPassInput.isBlank()) {
                                    onShowToast("Enter room ID and password")
                                    return@Button
                                }
                                TournamentService.setRoomCredentials(selectedMatchId, roomIdInput, roomPassInput, true)
                                onShowToast("Saved Room ID $roomIdInput for tournament!")
                                activeSection = "Dashboard"
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Save & Reveal Credentials", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // SECTION 4: VIEW REGISTRATIONS
        if (activeSection == "Registrations") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Add Manual Player Registration", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = newRegIgn,
                                onValueChange = { newRegIgn = it },
                                label = { Text("Player IGN", fontSize = 10.sp) },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = newRegUid,
                                onValueChange = { newRegUid = it },
                                label = { Text("FF Numeric UID", fontSize = 10.sp) },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                        Button(
                            onClick = {
                                if (newRegIgn.isBlank() || newRegUid.isBlank()) {
                                    onShowToast("Enter IGN and UID")
                                    return@Button
                                }
                                val matchId = TournamentService.matches.firstOrNull()?.id ?: "m1"
                                TournamentService.addRegistration(matchId, newRegIgn, newRegUid)
                                onShowToast("Registered $newRegIgn!")
                                newRegIgn = ""
                                newRegUid = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Register Player", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }

            items(TournamentService.registrations) { reg ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(reg.ign, fontSize = 12.sp, fontWeight = FontWeight.Black, color = NeonLime)
                            Text("UID: ${reg.ffUid} • ${reg.tournamentTitle}", fontSize = 10.sp, color = TextSecondary)
                        }
                        Surface(
                            color = NeonLime.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(reg.status, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = NeonLime, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                }
            }
        }

        // SECTION 5: PUBLISH RESULTS (PLACEMENT, KILLS)
        if (activeSection == "Results") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Publish Tournament Results", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("Record champion placement, kills & sponsor prize", fontSize = 10.sp, color = TextSecondary)

                        OutlinedTextField(
                            value = firstIgn,
                            onValueChange = { firstIgn = it },
                            label = { Text("1st Place Booyah IGN", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = firstKills,
                                onValueChange = { firstKills = it },
                                label = { Text("1st Kills", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = firstPrize,
                                onValueChange = { firstPrize = it },
                                label = { Text("1st Sponsor Prize", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        OutlinedTextField(
                            value = secondIgn,
                            onValueChange = { secondIgn = it },
                            label = { Text("2nd Place Runner-Up IGN", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                if (firstIgn.isBlank()) {
                                    onShowToast("Enter champion IGN")
                                    return@Button
                                }
                                val k = firstKills.toIntOrNull() ?: 10
                                TournamentService.publishResults(
                                    PublishedResult(
                                        tournamentId = resultMatchId,
                                        firstIgn = firstIgn,
                                        firstUid = firstUid.ifBlank { "991002" },
                                        firstKills = k,
                                        firstPrize = firstPrize,
                                        secondIgn = secondIgn.ifBlank { "RunnerUp_99" },
                                        secondUid = "882012",
                                        secondKills = 6,
                                        secondPrize = "₹3,000 Trophy",
                                        thirdIgn = "Bronze_Hero",
                                        thirdUid = "771920",
                                        thirdKills = 4,
                                        thirdPrize = "₹2,000 Trophy"
                                    )
                                )
                                onShowToast("Published results for tournament! Contestants notified.")
                                activeSection = "Dashboard"
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Publish Official Results", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // SECTION 6: SEND ANNOUNCEMENT
        if (activeSection == "Announcement") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Send In-App Announcement", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("Broadcast notification to all registered players", fontSize = 10.sp, color = TextSecondary)

                        OutlinedTextField(
                            value = announceTitle,
                            onValueChange = { announceTitle = it },
                            label = { Text("Announcement Headline", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = announceMsg,
                            onValueChange = { announceMsg = it },
                            label = { Text("Message details", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )

                        Button(
                            onClick = {
                                if (announceTitle.isBlank() || announceMsg.isBlank()) {
                                    onShowToast("Enter announcement headline and message")
                                    return@Button
                                }
                                TournamentService.sendAnnouncement(announceTitle, announceMsg)
                                onShowToast("Broadcast announcement sent!")
                                announceTitle = ""
                                announceMsg = ""
                                activeSection = "Dashboard"
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Send Announcement", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}
