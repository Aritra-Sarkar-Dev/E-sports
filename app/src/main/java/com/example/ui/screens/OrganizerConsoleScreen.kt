package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.window.Dialog
import com.example.ui.model.EsportsMatch
import com.example.ui.model.PublishedResult
import com.example.ui.model.TournamentService
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OrganizerConsoleScreen(
    onClose: () -> Unit,
    onShowToast: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var activeSection by remember { mutableStateOf("Dashboard") }
    val sections = listOf("Dashboard", "Tournaments", "Room Manager", "Registrations", "Results", "Prize Tracker", "Support Tickets", "Reports", "Users", "Audit Log", "Announcements")

    var isSyncing by remember { mutableStateOf(false) }
    var showAdminGuide by remember { mutableStateOf(false) }

    // Create / Edit Tournament state
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

    // Announcement state
    var announceTitle by remember { mutableStateOf("") }
    var announceMsg by remember { mutableStateOf("") }

    // Support ticket reply state
    var replyText by remember { mutableStateOf("") }
    var selectedTicketId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        // Top Header with Close, Admin Guide (?) and Sync Now
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
                                Text("DEMO", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color.Black, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                            }
                        }
                        Text("Free Fire Custom Rooms & Sponsor Cups", fontSize = 10.sp, color = TextSecondary)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    // Sync Now button
                    Button(
                        onClick = {
                            isSyncing = true
                            coroutineScope.launch {
                                delay(1200)
                                isSyncing = false
                                onShowToast("Tournament & registration data synced successfully!")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ElevatedSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonLime.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        if (isSyncing) {
                            CircularProgressIndicator(modifier = Modifier.size(14.dp), color = NeonLime, strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Sync, contentDescription = "Sync", tint = NeonLime, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Sync Now", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeonLime)
                        }
                    }

                    // Admin Guide Button (?)
                    IconButton(
                        onClick = { showAdminGuide = true },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(ElevatedSurface)
                            .border(1.dp, CardBorder, CircleShape)
                    ) {
                        Icon(Icons.Default.HelpOutline, contentDescription = "Admin Guide", tint = NeonLime, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        // Section Tabs (Horizontally scrollable)
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

        // 1. DASHBOARD
        if (activeSection == "Dashboard") {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
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

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("QUICK MANAGEMENT", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 1.sp)
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
                            Text("Room ID: ${m.roomId ?: "Not set"}", fontSize = 10.sp, color = if (m.roomId != null) NeonLime else TextSecondary)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Manage Room",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonLime,
                                    modifier = Modifier.clickable {
                                        selectedMatchId = m.id
                                        roomIdInput = m.roomId ?: ""
                                        roomPassInput = m.roomPass ?: ""
                                        activeSection = "Room Manager"
                                    }
                                )
                                Text(
                                    text = "Publish Results",
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

        // 2. TOURNAMENTS (Create, Edit, Cancel, Duplicate)
        if (activeSection == "Tournaments") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Create / Edit Tournament", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("Free entry • Sponsor-funded prizes only", fontSize = 10.sp, color = NeonLime)

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
                            label = { Text("Sponsor Name (e.g. Asus ROG)", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = prizeText,
                                onValueChange = { prizeText = it },
                                label = { Text("Sponsor Prize Pool (₹)", fontSize = 11.sp) },
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
                            label = { Text("Kick-off Schedule Text", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                if (title.isBlank()) {
                                    onShowToast("Enter title")
                                    return@Button
                                }
                                val pz = prizeText.toIntOrNull() ?: 10000
                                val sl = slotsText.toIntOrNull() ?: 48
                                TournamentService.createTournament(title, sponsor, mode, map, timeText, pz, sl)
                                onShowToast("Tournament published: $title")
                                title = ""
                                sponsor = ""
                                activeSection = "Dashboard"
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime)
                        ) {
                            Text("Publish Cup", color = Color.Black, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }

            items(TournamentService.matches) { m ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
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
                            Text(m.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Sponsor: ${m.sponsor} • ₹${m.prizePool}", fontSize = 10.sp, color = TextSecondary)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                "Duplicate",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonLime,
                                modifier = Modifier.clickable {
                                    TournamentService.createTournament("${m.title} (Copy)", m.sponsor, m.mode, m.map, m.startTimeText, m.prizePool, m.maxSlots)
                                    onShowToast("Duplicated tournament!")
                                }
                            )
                            Text(
                                "Cancel",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = LiveRed,
                                modifier = Modifier.clickable {
                                    val idx = TournamentService.matches.indexOf(m)
                                    if (idx != -1) TournamentService.matches.removeAt(idx)
                                    onShowToast("Tournament cancelled.")
                                }
                            )
                        }
                    }
                }
            }
        }

        // 3. ROOM MANAGER (Room ID, Password, auto-reveal 15 mins before start, instant reveal)
        if (activeSection == "Room Manager") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Room Credential Manager", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("Auto-unlocks 15 minutes before start time", fontSize = 10.sp, color = TextSecondary)

                        Text("Select Match:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                        LazyRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(TournamentService.matches) { m ->
                                val isSel = selectedMatchId == m.id
                                Surface(
                                    modifier = Modifier.clickable {
                                        selectedMatchId = m.id
                                        roomIdInput = m.roomId ?: ""
                                        roomPassInput = m.roomPass ?: ""
                                    },
                                    color = if (isSel) NeonLime else ElevatedSurface,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(m.title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp))
                                }
                            }
                        }

                        OutlinedTextField(
                            value = roomIdInput,
                            onValueChange = { roomIdInput = it },
                            label = { Text("Room ID", fontSize = 11.sp) },
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
                                if (roomIdInput.isBlank()) {
                                    onShowToast("Enter room ID")
                                    return@Button
                                }
                                TournamentService.setRoomCredentials(selectedMatchId, roomIdInput, roomPassInput, true)
                                onShowToast("Instant room reveal published to players!")
                                activeSection = "Dashboard"
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime)
                        ) {
                            Text("Instant Reveal Credentials", color = Color.Black, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // 4. REGISTRATIONS
        if (activeSection == "Registrations") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Manual Registration", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = newRegIgn,
                                onValueChange = { newRegIgn = it },
                                label = { Text("IGN", fontSize = 10.sp) },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = newRegUid,
                                onValueChange = { newRegUid = it },
                                label = { Text("UID (8-12 digits)", fontSize = 10.sp) },
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
                                onShowToast("Registered player $newRegIgn")
                                newRegIgn = ""
                                newRegUid = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime)
                        ) {
                            Text("Approve & Register", color = Color.Black, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }

            items(TournamentService.registrations) { reg ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
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
                        Text(
                            "Remove",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = LiveRed,
                            modifier = Modifier.clickable {
                                TournamentService.registrations.remove(reg)
                                onShowToast("Removed registration.")
                            }
                        )
                    }
                }
            }
        }

        // 5. RESULTS (Publish results: placement, kills, sponsor prize label)
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
                                onShowToast("Published official results! Contestants notified.")
                                activeSection = "Dashboard"
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime)
                        ) {
                            Text("Publish Official Results", color = Color.Black, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // 6. PRIZE TRACKER (Set status Recorded, Sponsor notified, Delivered by sponsor; triggers player notification)
        if (activeSection == "Prize Tracker") {
            item {
                Text("Sponsor Prize Tracker", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                Text("Manage prize delivery status for winners", fontSize = 10.sp, color = TextSecondary)
            }

            items(TournamentService.prizeRecords) { prize ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(prize.tournamentTitle, fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Surface(color = NeonLime.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)) {
                                Text(prize.status, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = NeonLime, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text("${prize.rank} • Sponsor: ${prize.sponsorName}", fontSize = 11.sp, color = TextSecondary)
                        Text("Prize: ${prize.sponsorPrize}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldCoins)

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    TournamentService.updatePrizeStatus(prize.id, "Recorded")
                                    onShowToast("Marked as Recorded")
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = ElevatedSurface),
                                contentPadding = PaddingValues(4.dp)
                            ) {
                                Text("Recorded", fontSize = 10.sp, color = Color.White)
                            }
                            Button(
                                onClick = {
                                    TournamentService.updatePrizeStatus(prize.id, "Sponsor notified")
                                    onShowToast("Marked as Sponsor Notified")
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = ElevatedSurface),
                                contentPadding = PaddingValues(4.dp)
                            ) {
                                Text("Notified", fontSize = 10.sp, color = GoldCoins)
                            }
                            Button(
                                onClick = {
                                    TournamentService.updatePrizeStatus(prize.id, "Delivered by sponsor")
                                    onShowToast("Marked as Delivered by Sponsor")
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                                contentPadding = PaddingValues(4.dp)
                            ) {
                                Text("Delivered", fontSize = 10.sp, color = Color.Black, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }

        // 7. SUPPORT TICKETS
        if (activeSection == "Support Tickets") {
            item {
                Text("Player Support & Dispute Tickets", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
            }

            items(TournamentService.supportTickets) { ticket ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(ticket.subject, fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Surface(color = if (ticket.status == "Open") LiveRed.copy(alpha = 0.2f) else NeonLime.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)) {
                                Text(ticket.status, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (ticket.status == "Open") LiveRed else NeonLime, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text("From: ${ticket.userIgn}", fontSize = 10.sp, color = TextSecondary)
                        Text(ticket.message, fontSize = 11.sp, color = TextPrimary)

                        if (ticket.reply != null) {
                            Text("Reply: ${ticket.reply}", fontSize = 11.sp, color = NeonLime, fontWeight = FontWeight.Bold)
                        } else {
                            OutlinedTextField(
                                value = if (selectedTicketId == ticket.id) replyText else "",
                                onValueChange = {
                                    selectedTicketId = ticket.id
                                    replyText = it
                                },
                                label = { Text("Type reply...", fontSize = 10.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Button(
                                onClick = {
                                    if (replyText.isNotBlank()) {
                                        TournamentService.replyTicket(ticket.id, replyText)
                                        onShowToast("Ticket replied and closed.")
                                        replyText = ""
                                        selectedTicketId = null
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonLime)
                            ) {
                                Text("Send Reply & Close Ticket", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }

        // 8. REPORTS / DISPUTES
        if (activeSection == "Reports") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Active Dispute Reports", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("No active dispute reports currently logged by referees.", fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }
        }

        // 9. USERS (Warn, Ban, Unban)
        if (activeSection == "Users") {
            item {
                Text("User Management & Anti-Cheat", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
            }

            items(TournamentService.usersList) { u ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
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
                            Text(u.ign, fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Text("UID: ${u.ffUid} • Status: ${u.status}", fontSize = 10.sp, color = when (u.status) {
                                "Banned" -> LiveRed
                                "Warned" -> GoldCoins
                                else -> NeonLime
                            })
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                "Warn",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldCoins,
                                modifier = Modifier.clickable {
                                    TournamentService.updateUserStatus(u.id, "Warned")
                                    onShowToast("Warned user ${u.ign}")
                                }
                            )
                            Text(
                                "Ban",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = LiveRed,
                                modifier = Modifier.clickable {
                                    TournamentService.updateUserStatus(u.id, "Banned")
                                    onShowToast("Banned user ${u.ign}")
                                }
                            )
                            Text(
                                "Active",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonLime,
                                modifier = Modifier.clickable {
                                    TournamentService.updateUserStatus(u.id, "Active")
                                    onShowToast("Restored user ${u.ign}")
                                }
                            )
                        }
                    }
                }
            }
        }

        // 10. AUDIT LOG
        if (activeSection == "Audit Log") {
            item {
                Text("Administrator Audit Log", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
            }

            items(TournamentService.auditLogs) { log ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(log.action, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Admin: ${log.adminName} • ${log.timestamp}", fontSize = 9.sp, color = TextSecondary)
                    }
                }
            }
        }

        // 11. ANNOUNCEMENTS
        if (activeSection == "Announcements") {
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
                                    onShowToast("Enter headline and message")
                                    return@Button
                                }
                                TournamentService.sendAnnouncement(announceTitle, announceMsg)
                                onShowToast("Broadcast announcement sent!")
                                announceTitle = ""
                                announceMsg = ""
                                activeSection = "Dashboard"
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime)
                        ) {
                            Text("Send Announcement", color = Color.Black, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }

    // Admin Guide Dialog (?)
    if (showAdminGuide) {
        Dialog(onDismissRequest = { showAdminGuide = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, CardBorder, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("ADMIN GUIDE", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                        IconButton(onClick = { showAdminGuide = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                        }
                    }

                    Text("• Tournaments: Create, edit, duplicate or cancel free-entry cups.", fontSize = 12.sp, color = TextPrimary, lineHeight = 16.sp)
                    Text("• Room Manager: Set Room ID & Passwords. Automatically unlocks 15m before start or instantly via reveal button.", fontSize = 12.sp, color = TextPrimary, lineHeight = 16.sp)
                    Text("• Prize Tracker: Track sponsor prize delivery status (Recorded, Sponsor Notified, Delivered).", fontSize = 12.sp, color = TextPrimary, lineHeight = 16.sp)
                    Text("• Support Tickets & Users: Reply to player support queries, warn or ban anti-cheat violators.", fontSize = 12.sp, color = TextPrimary, lineHeight = 16.sp)

                    Button(
                        onClick = { showAdminGuide = false },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonLime)
                    ) {
                        Text("Got It", color = Color.Black, fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}
