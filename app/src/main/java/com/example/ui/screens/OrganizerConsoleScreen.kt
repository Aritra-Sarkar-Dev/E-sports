package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.model.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
 * ARCHITECTURAL PRODUCTION NOTE:
 * Real production admin operations must run via a dedicated hosted web panel
 * with Firebase Auth Custom Claims (role: organizer / admin). Match results
 * and sponsor prize status updates are executed via authenticated Cloud Functions
 * with server-side validation to maintain zero-trust security.
 */

@Composable
fun OrganizerConsoleScreen(
    onClose: () -> Unit,
    onShowToast: (String) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()
    var activeSection by remember { mutableStateOf("Dashboard") }
    val sections = listOf(
        "Dashboard",
        "Tournaments",
        "Room Manager",
        "Registrations",
        "Results",
        "Prize Tracker",
        "Support Tickets",
        "Reports",
        "Users",
        "Audit Log",
        "Announcements"
    )

    var isSyncing by remember { mutableStateOf(false) }
    var showAdminGuide by remember { mutableStateOf(false) }

    // Confirmation dialog states
    var banConfirmUser by remember { mutableStateOf<UserManagementRecord?>(null) }
    var cancelConfirmMatch by remember { mutableStateOf<EsportsMatch?>(null) }
    var removeConfirmReg by remember { mutableStateOf<TournamentRegistration?>(null) }

    // Tournament creation state
    var title by remember { mutableStateOf("") }
    var sponsor by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf("Squad") }
    var map by remember { mutableStateOf("Bermuda") }
    var selectedDate by remember { mutableStateOf("Today") }
    var selectedTimeHour by remember { mutableStateOf("09") }
    var selectedTimeMin by remember { mutableStateOf("00") }
    var selectedTimeAmPm by remember { mutableStateOf("PM") }
    var prizeText by remember { mutableStateOf("10000") }
    var slotsText by remember { mutableStateOf("48") }
    var minLevel by remember { mutableIntStateOf(30) }
    var teamSize by remember { mutableStateOf("4 Players") }
    var rulesText by remember { mutableStateOf("Mobile device only. Emulators strictly prohibited. No hacks, teaming, or third-party scripts. Minimum Free Fire Level 30 required.") }
    var perKillPoints by remember { mutableIntStateOf(2) }

    // Room ID state
    var selectedMatchId by remember { mutableStateOf(TournamentService.matches.firstOrNull()?.id ?: "") }
    var roomIdInput by remember { mutableStateOf("") }
    var roomPassInput by remember { mutableStateOf("") }

    // Registrations state
    var regTournamentId by remember { mutableStateOf(TournamentService.matches.firstOrNull()?.id ?: "") }
    var newRegIgn by remember { mutableStateOf("") }
    var newRegUid by remember { mutableStateOf("") }

    // Results state
    var resultMatchId by remember { mutableStateOf(TournamentService.matches.firstOrNull()?.id ?: "") }
    var previewResultsDialog by remember { mutableStateOf(false) }
    var firstIgn by remember { mutableStateOf("SOUL_IMMORTAL") }
    var firstUid by remember { mutableStateOf("849201123") }
    var firstKills by remember { mutableStateOf("12") }
    var firstPrize by remember { mutableStateOf("Trophy + Pro Headset (Sponsor value ₹5,000)") }
    var secondIgn by remember { mutableStateOf("NEON_SOUL") }
    var secondUid by remember { mutableStateOf("994821034") }
    var secondKills by remember { mutableStateOf("9") }
    var secondPrize by remember { mutableStateOf("Mechanical Keyboard (Sponsor value ₹3,000)") }
    var thirdIgn by remember { mutableStateOf("VIPER_STRIKE") }
    var thirdUid by remember { mutableStateOf("771920441") }
    var thirdKills by remember { mutableStateOf("6") }
    var thirdPrize by remember { mutableStateOf("Esports Jersey (Sponsor value ₹1,500)") }

    // Announcements state
    var announceTitle by remember { mutableStateOf("") }
    var announceMsg by remember { mutableStateOf("") }
    var announceAudience by remember { mutableStateOf("All Players (Broadcast)") }

    // Users search & filter state
    var userSearchQuery by remember { mutableStateOf("") }
    var userStatusFilter by remember { mutableStateOf("All") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp)
    ) {
        // Top Bar: Back, Title, Guide Button, Sync Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onClose() },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CardSurface)
                            .border(1.dp, CardBorder, CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("ORGANIZER CONSOLE", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(color = NeonLime, shape = RoundedCornerShape(6.dp)) {
                                Text("DEMO", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text("100% Free Entry • Sponsor Funded Platform", fontSize = 11.sp, color = TextSecondary)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = { showAdminGuide = true },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CardSurface)
                            .border(1.dp, CardBorder, CircleShape)
                    ) {
                        Icon(Icons.Default.HelpOutline, contentDescription = "Admin Guide", tint = NeonLime, modifier = Modifier.size(18.dp))
                    }

                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                isSyncing = true
                                delay(600)
                                isSyncing = false
                                onShowToast("Synced backend data successfully.")
                            }
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CardSurface)
                            .border(1.dp, CardBorder, CircleShape)
                    ) {
                        if (isSyncing) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = NeonLime, strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Sync, contentDescription = "Sync", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // Section Tabs
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 2.dp, top = 2.dp, end = 24.dp, bottom = 2.dp)
            ) {
                items(sections) { sec ->
                    val isSelected = activeSection == sec
                    Surface(
                        modifier = Modifier.clickable { activeSection = sec },
                        color = if (isSelected) NeonLime else ElevatedSurface,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NeonLime else CardBorder)
                    ) {
                        Text(
                            text = sec,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            color = if (isSelected) Color.Black else TextSecondary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Section: Dashboard
        if (activeSection == "Dashboard") {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("PLATFORM OVERVIEW", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 1.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ConsoleMetricCard("Active Tournaments", "${TournamentService.matches.count { it.status != "Completed" }}", NeonLime, Modifier.weight(1f))
                        ConsoleMetricCard("Total Players", "${TournamentService.usersList.size}", Color.White, Modifier.weight(1f))
                        ConsoleMetricCard("Prizes Recorded", "${TournamentService.prizeRecords.size}", GoldCoins, Modifier.weight(1f))
                    }

                    // Cloud architecture notice card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = NeonLime, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Production Architecture Notice", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "In production, organizer administration is conducted via a dedicated web panel with Firebase Auth Custom Claims (role: organizer). Tournament result publishing and sponsor prize delivery tracking are secured via server-side Cloud Functions.",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // Quick Actions
                    Text("QUICK DISPATCH", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 1.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { activeSection = "Tournaments" },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Create Tournament", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Black)
                        }
                        Button(
                            onClick = { activeSection = "Room Manager" },
                            colors = ButtonDefaults.buttonColors(containerColor = ElevatedSurface),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Set Room ID", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Section: Tournaments
        if (activeSection == "Tournaments") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("CREATE NEW TOURNAMENT", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("100% Free Entry • Sponsor Funded Prize Pool", fontSize = 11.sp, color = NeonLime, fontWeight = FontWeight.Bold)

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Tournament Title") },
                            placeholder = { Text("e.g. CYBERFRAY GRAND FINALS") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = sponsor,
                            onValueChange = { sponsor = it },
                            label = { Text("Sponsor Name") },
                            placeholder = { Text("e.g. Asus ROG Gaming") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Mode & Map
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Mode", fontSize = 11.sp, color = TextSecondary)
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    listOf("Solo", "Duo", "Squad").forEach { m ->
                                        val isSel = mode == m
                                        Surface(
                                            modifier = Modifier.clickable {
                                                mode = m
                                                teamSize = when (m) {
                                                    "Solo" -> "1 Player"
                                                    "Duo" -> "2 Players"
                                                    else -> "4 Players"
                                                }
                                            },
                                            color = if (isSel) NeonLime else ElevatedSurface,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(m, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp))
                                        }
                                    }
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text("Map", fontSize = 11.sp, color = TextSecondary)
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    listOf("Bermuda", "Purgatory", "Kalahari").forEach { mp ->
                                        val isSel = map == mp
                                        Surface(
                                            modifier = Modifier.clickable { map = mp },
                                            color = if (isSel) NeonLime else ElevatedSurface,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(mp, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp))
                                        }
                                    }
                                }
                            }
                        }

                        // Real Date Selector
                        Column {
                            Text("Kick-Off Date", fontSize = 11.sp, color = TextSecondary)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("Today", "Tomorrow", "In 2 Days", "Saturday").forEach { d ->
                                    val isSel = selectedDate == d
                                    Surface(
                                        modifier = Modifier.clickable { selectedDate = d },
                                        color = if (isSel) NeonLime else ElevatedSurface,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(d, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                                    }
                                }
                            }
                        }

                        // Real Time Selector (Hour, Minute, AM/PM)
                        Column {
                            Text("Kick-Off Time", fontSize = 11.sp, color = TextSecondary)
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                // Hour
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    listOf("07", "08", "09", "10", "11").forEach { hr ->
                                        val isSel = selectedTimeHour == hr
                                        Surface(
                                            modifier = Modifier.clickable { selectedTimeHour = hr },
                                            color = if (isSel) NeonLime else ElevatedSurface,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(hr, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp))
                                        }
                                    }
                                }
                                Text(":", color = Color.White, fontWeight = FontWeight.Bold)
                                // Min
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    listOf("00", "15", "30", "45").forEach { mn ->
                                        val isSel = selectedTimeMin == mn
                                        Surface(
                                            modifier = Modifier.clickable { selectedTimeMin = mn },
                                            color = if (isSel) NeonLime else ElevatedSurface,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(mn, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp))
                                        }
                                    }
                                }
                                // AM/PM
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    listOf("PM", "AM").forEach { ap ->
                                        val isSel = selectedTimeAmPm == ap
                                        Surface(
                                            modifier = Modifier.clickable { selectedTimeAmPm = ap },
                                            color = if (isSel) ElectricViolet else ElevatedSurface,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(ap, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp))
                                        }
                                    }
                                }
                            }
                        }

                        // Minimum Level & Team Size & Per-kill points
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Min FF Level", fontSize = 11.sp, color = TextSecondary)
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    listOf(20, 30, 40).forEach { lvl ->
                                        val isSel = minLevel == lvl
                                        Surface(
                                            modifier = Modifier.clickable { minLevel = lvl },
                                            color = if (isSel) NeonLime else ElevatedSurface,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Lvl $lvl+", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp))
                                        }
                                    }
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text("Per-Kill Points", fontSize = 11.sp, color = TextSecondary)
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    listOf(1, 2, 3).forEach { pt ->
                                        val isSel = perKillPoints == pt
                                        Surface(
                                            modifier = Modifier.clickable { perKillPoints = pt },
                                            color = if (isSel) ElectricViolet else ElevatedSurface,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("$pt pt", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp))
                                        }
                                    }
                                }
                            }
                        }

                        // Placement Points Table Display
                        Surface(
                            color = ElevatedSurface,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Placement Points Table:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonLime)
                                Text("1st: 12 pts | 2nd: 9 pts | 3rd: 8 pts | 4th: 7 pts | 5th: 6 pts | 6th+: 1 pt", fontSize = 10.sp, color = TextSecondary)
                            }
                        }

                        // Rules Text
                        OutlinedTextField(
                            value = rulesText,
                            onValueChange = { rulesText = it },
                            label = { Text("Rules & Fair Play Text") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = prizeText,
                                onValueChange = { prizeText = it },
                                label = { Text("Sponsor Prize Value (₹)") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = slotsText,
                                onValueChange = { slotsText = it },
                                label = { Text("Max Slots") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Button(
                            onClick = {
                                if (title.isBlank()) {
                                    onShowToast("Please enter a tournament title")
                                    return@Button
                                }
                                val formattedTime = "$selectedDate, $selectedTimeHour:$selectedTimeMin $selectedTimeAmPm"
                                val newMatch = TournamentService.createTournament(
                                    title = title,
                                    sponsor = sponsor,
                                    mode = mode,
                                    map = map,
                                    startTimeText = formattedTime,
                                    sponsorPrize = prizeText.toIntOrNull() ?: 10000,
                                    maxSlots = slotsText.toIntOrNull() ?: 48,
                                    minLevel = minLevel,
                                    teamSize = teamSize,
                                    rulesText = rulesText
                                )
                                onShowToast("Created tournament '${newMatch.title}'!")
                                title = ""
                                sponsor = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Publish Tournament (Free Entry)", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 13.sp)
                        }
                    }
                }
            }

            // Existing Tournaments List with Cancel option
            item {
                Text("CURRENT ACTIVE TOURNAMENTS", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 1.sp)
            }

            items(TournamentService.matches) { match ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(match.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("${match.mode} • ${match.map} • ${match.startTimeText}", fontSize = 11.sp, color = TextSecondary)
                            Text("Sponsor: ${match.sponsor} • ₹${match.prizePool} pool (Sponsor-funded)", fontSize = 11.sp, color = NeonLime)
                        }

                        IconButton(onClick = { cancelConfirmMatch = match }) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Cancel Tournament", tint = LiveRed)
                        }
                    }
                }
            }
        }

        // Section: Room Manager
        if (activeSection == "Room Manager") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("SET FREE FIRE ROOM CREDENTIALS", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("Auto-reveals to joined players 15 minutes prior to kick-off", fontSize = 11.sp, color = TextSecondary)

                        // Tournament Dropdown Selector
                        Text("Select Tournament:", fontSize = 11.sp, color = TextSecondary)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(TournamentService.matches.filter { it.status != "Completed" }) { m ->
                                val isSel = selectedMatchId == m.id
                                Surface(
                                    modifier = Modifier.clickable { selectedMatchId = m.id },
                                    color = if (isSel) NeonLime else ElevatedSurface,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(m.title.take(24) + "...", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp))
                                }
                            }
                        }

                        OutlinedTextField(
                            value = roomIdInput,
                            onValueChange = { roomIdInput = it },
                            label = { Text("Free Fire Custom Room ID") },
                            placeholder = { Text("e.g. 8849102") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = roomPassInput,
                            onValueChange = { roomPassInput = it },
                            label = { Text("Room Password") },
                            placeholder = { Text("e.g. ffpro99") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    if (roomIdInput.isBlank()) {
                                        onShowToast("Enter room ID")
                                        return@Button
                                    }
                                    TournamentService.setRoomCredentials(selectedMatchId, roomIdInput, roomPassInput, revealImmediately = false)
                                    onShowToast("Credentials scheduled. Will auto-reveal 15m prior!")
                                    roomIdInput = ""
                                    roomPassInput = ""
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ElevatedSurface),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Auto-Reveal (15m)", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    if (roomIdInput.isBlank()) {
                                        onShowToast("Enter room ID")
                                        return@Button
                                    }
                                    TournamentService.setRoomCredentials(selectedMatchId, roomIdInput, roomPassInput, revealImmediately = true)
                                    onShowToast("Credentials revealed immediately to contestants!")
                                    roomIdInput = ""
                                    roomPassInput = ""
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Reveal Now ⚡", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }

        // Section: Registrations
        if (activeSection == "Registrations") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("MANUAL REGISTRATION ENTRY", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Button(
                                onClick = {
                                    val csv = TournamentService.registrations.joinToString("\n") { "${it.ign},${it.ffUid},${it.tournamentTitle},${it.status}" }
                                    clipboardManager.setText(AnnotatedString(csv))
                                    onShowToast("Exported ${TournamentService.registrations.size} registrations to clipboard (CSV)!")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ElevatedSurface),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Export CSV", fontSize = 11.sp, color = NeonLime, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Tournament Selector
                        Text("Tournament:", fontSize = 11.sp, color = TextSecondary)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(TournamentService.matches) { m ->
                                val isSel = regTournamentId == m.id
                                Surface(
                                    modifier = Modifier.clickable { regTournamentId = m.id },
                                    color = if (isSel) NeonLime else ElevatedSurface,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(m.title.take(20) + "...", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp))
                                }
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = newRegIgn,
                                onValueChange = { newRegIgn = it },
                                label = { Text("Player IGN") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = newRegUid,
                                onValueChange = { newRegUid = it },
                                label = { Text("FF UID (8-12 digits)") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Button(
                            onClick = {
                                if (newRegIgn.isBlank() || !newRegUid.matches(Regex("^[0-9]{8,12}$"))) {
                                    onShowToast("Enter valid IGN and 8-12 digit UID")
                                    return@Button
                                }
                                TournamentService.addRegistration(regTournamentId, newRegIgn, newRegUid)
                                onShowToast("Registered '$newRegIgn'!")
                                newRegIgn = ""
                                newRegUid = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Confirm Registration", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 12.sp)
                        }
                    }
                }
            }

            item {
                Text("PARTICIPANT ROSTER (${TournamentService.registrations.size})", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 1.sp)
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(reg.ign, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = if (reg.status == "Approved") NeonLime.copy(alpha = 0.2f) else ElevatedSurface,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(reg.status, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (reg.status == "Approved") NeonLime else TextSecondary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Text("UID: ${reg.ffUid} • ${reg.tournamentTitle.take(25)}...", fontSize = 11.sp, color = TextSecondary)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (reg.status != "Approved") {
                                IconButton(onClick = {
                                    TournamentService.approveRegistration(reg.id)
                                    onShowToast("Approved ${reg.ign}")
                                }) {
                                    Icon(Icons.Default.Check, contentDescription = "Approve", tint = NeonLime, modifier = Modifier.size(18.dp))
                                }
                            }
                            IconButton(onClick = { removeConfirmReg = reg }) {
                                Icon(Icons.Default.Close, contentDescription = "Remove", tint = LiveRed, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        // Section: Results
        if (activeSection == "Results") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("PUBLISH TOURNAMENT STANDINGS & RESULTS", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("Placements 1st to 12th • Sponsor Prizes Only", fontSize = 11.sp, color = NeonLime, fontWeight = FontWeight.Bold)

                        // Tournament Dropdown
                        Text("Tournament:", fontSize = 11.sp, color = TextSecondary)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(TournamentService.matches) { m ->
                                val isSel = resultMatchId == m.id
                                Surface(
                                    modifier = Modifier.clickable { resultMatchId = m.id },
                                    color = if (isSel) NeonLime else ElevatedSurface,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(m.title.take(24) + "...", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp))
                                }
                            }
                        }

                        // 1st Place
                        Text("1st Place (Champion Booyah):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldCoins)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = firstIgn, onValueChange = { firstIgn = it }, label = { Text("IGN") }, singleLine = true, modifier = Modifier.weight(1f))
                            OutlinedTextField(value = firstKills, onValueChange = { firstKills = it }, label = { Text("Kills") }, singleLine = true, modifier = Modifier.width(70.dp))
                        }
                        OutlinedTextField(value = firstPrize, onValueChange = { firstPrize = it }, label = { Text("Sponsor Prize Item Label") }, singleLine = true, modifier = Modifier.fillMaxWidth())

                        // 2nd Place
                        Text("2nd Place:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC0C0C0))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = secondIgn, onValueChange = { secondIgn = it }, label = { Text("IGN") }, singleLine = true, modifier = Modifier.weight(1f))
                            OutlinedTextField(value = secondKills, onValueChange = { secondKills = it }, label = { Text("Kills") }, singleLine = true, modifier = Modifier.width(70.dp))
                        }
                        OutlinedTextField(value = secondPrize, onValueChange = { secondPrize = it }, label = { Text("Sponsor Prize Item Label") }, singleLine = true, modifier = Modifier.fillMaxWidth())

                        // 3rd Place
                        Text("3rd Place:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFCD7F32))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = thirdIgn, onValueChange = { thirdIgn = it }, label = { Text("IGN") }, singleLine = true, modifier = Modifier.weight(1f))
                            OutlinedTextField(value = thirdKills, onValueChange = { thirdKills = it }, label = { Text("Kills") }, singleLine = true, modifier = Modifier.width(70.dp))
                        }
                        OutlinedTextField(value = thirdPrize, onValueChange = { thirdPrize = it }, label = { Text("Sponsor Prize Item Label") }, singleLine = true, modifier = Modifier.fillMaxWidth())

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { previewResultsDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = ElevatedSurface),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Preview Results", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    val tTitle = TournamentService.matches.find { it.id == resultMatchId }?.title ?: "Tournament"
                                    val res = PublishedResult(
                                        tournamentId = resultMatchId,
                                        tournamentTitle = tTitle,
                                        firstIgn = firstIgn,
                                        firstUid = firstUid,
                                        firstKills = firstKills.toIntOrNull() ?: 0,
                                        firstPrize = firstPrize,
                                        secondIgn = secondIgn,
                                        secondUid = secondUid,
                                        secondKills = secondKills.toIntOrNull() ?: 0,
                                        secondPrize = secondPrize,
                                        thirdIgn = thirdIgn,
                                        thirdUid = thirdUid,
                                        thirdKills = thirdKills.toIntOrNull() ?: 0,
                                        thirdPrize = thirdPrize
                                    )
                                    TournamentService.publishResults(res)
                                    onShowToast("Results published for '$tTitle'!")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Publish to App", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }

        // Section: Prize Tracker
        if (activeSection == "Prize Tracker") {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("SPONSOR PRIZE LOGISTICS TRACKER", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 1.sp)
                    Text("Prizes are sponsor-funded and fulfilled outside the app. No cash payouts or UPI.", fontSize = 11.sp, color = TextSecondary)
                }
            }

            items(TournamentService.prizeRecords) { prize ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(prize.tournamentTitle, fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Surface(
                                color = when (prize.status) {
                                    "Delivered by sponsor" -> NeonLime.copy(alpha = 0.2f)
                                    "Sponsor notified" -> ElectricViolet.copy(alpha = 0.2f)
                                    else -> ElevatedSurface
                                },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    prize.status,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (prize.status) {
                                        "Delivered by sponsor" -> NeonLime
                                        "Sponsor notified" -> ElectricViolet
                                        else -> Color.White
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Text("Item: ${prize.sponsorPrize} • Sponsor: ${prize.sponsorName}", fontSize = 12.sp, color = NeonLime, fontWeight = FontWeight.Bold)

                        // 3-Step Flow: Highlight only current step, checks on completed, next action lime button
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            // Step 1: Recorded
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonLime, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Recorded", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Text("→", color = TextSecondary)
                            // Step 2: Sponsor Notified
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (prize.status == "Sponsor notified" || prize.status == "Delivered by sponsor") {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonLime, modifier = Modifier.size(16.dp))
                                } else {
                                    Icon(Icons.Default.RadioButtonUnchecked, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Notified", fontSize = 11.sp, color = if (prize.status != "Recorded") Color.White else TextSecondary, fontWeight = FontWeight.Bold)
                            }
                            Text("→", color = TextSecondary)
                            // Step 3: Delivered
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (prize.status == "Delivered by sponsor") {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonLime, modifier = Modifier.size(16.dp))
                                } else {
                                    Icon(Icons.Default.RadioButtonUnchecked, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Delivered", fontSize = 11.sp, color = if (prize.status == "Delivered by sponsor") NeonLime else TextSecondary, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Next Action Button (Prominent Lime)
                        when (prize.status) {
                            "Recorded" -> {
                                Button(
                                    onClick = {
                                        TournamentService.updatePrizeStatus(prize.id, "Sponsor notified")
                                        onShowToast("Sponsor ${prize.sponsorName} notified for delivery!")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Mark: Sponsor Notified", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Black)
                                }
                            }
                            "Sponsor notified" -> {
                                Button(
                                    onClick = {
                                        TournamentService.updatePrizeStatus(prize.id, "Delivered by sponsor")
                                        onShowToast("Prize marked Delivered by Sponsor!")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Mark: Delivered by Sponsor ✓", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Black)
                                }
                            }
                            else -> {
                                Text("✓ Delivery fulfilled by sponsor outside the app", fontSize = 11.sp, color = TextSecondary)
                            }
                        }
                    }
                }
            }
        }

        // Section: Reports & Disputes
        if (activeSection == "Reports") {
            item {
                Text("DISPUTE & ANTI-CHEAT TICKETS (${TournamentService.disputeReports.size})", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 1.sp)
            }

            if (TournamentService.disputeReports.isEmpty()) {
                item {
                    EmptyStateCard("No dispute reports pending. Fair play radar is clear!")
                }
            } else {
                items(TournamentService.disputeReports) { rep ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Report by: ${rep.reporterIgn}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Surface(
                                    color = if (rep.status == "Resolved") NeonLime.copy(alpha = 0.2f) else LiveRed.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(rep.status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (rep.status == "Resolved") NeonLime else LiveRed, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Text("Accused: ${rep.reportedIgn} • Tournament: ${rep.tournamentTitle}", fontSize = 11.sp, color = TextSecondary)
                            Text("Reason: ${rep.reason}", fontSize = 12.sp, color = LiveRed, fontWeight = FontWeight.Bold)
                            Text(rep.details, fontSize = 11.sp, color = Color.White)

                            if (rep.status == "Pending") {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = {
                                            TournamentService.resolveDispute(rep.id, "Resolved")
                                            onShowToast("Dispute #${rep.id} resolved by referee.")
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Resolve & Warn", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Black)
                                    }
                                    Button(
                                        onClick = {
                                            TournamentService.resolveDispute(rep.id, "Dismissed")
                                            onShowToast("Dispute #${rep.id} dismissed.")
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = ElevatedSurface),
                                        shape = RoundedCornerShape(10.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Dismiss", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Users
        if (activeSection == "Users") {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("USER MODERATION & ACCESS CONTROL", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 1.sp)

                    // Search box
                    OutlinedTextField(
                        value = userSearchQuery,
                        onValueChange = { userSearchQuery = it },
                        placeholder = { Text("Search by IGN or Free Fire UID...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Filter chips: All, Active, Warned, Banned
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("All", "Active", "Warned", "Banned").forEach { flt ->
                            val isSel = userStatusFilter == flt
                            Surface(
                                modifier = Modifier.clickable { userStatusFilter = flt },
                                color = if (isSel) NeonLime else ElevatedSurface,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) NeonLime else CardBorder)
                            ) {
                                Text(
                                    flt,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Black else FontWeight.Bold,
                                    color = if (isSel) Color.Black else TextSecondary,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            val filteredUsers = TournamentService.usersList.filter { u ->
                val matchesQuery = u.ign.contains(userSearchQuery, ignoreCase = true) || u.ffUid.contains(userSearchQuery)
                val matchesFilter = userStatusFilter == "All" || u.status.equals(userStatusFilter, ignoreCase = true)
                matchesQuery && matchesFilter
            }

            if (filteredUsers.isEmpty()) {
                item { EmptyStateCard("No users match the search and filter criteria.") }
            } else {
                items(filteredUsers) { u ->
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
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(u.ign, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = when (u.status) {
                                            "Active" -> NeonLime.copy(alpha = 0.2f)
                                            "Warned" -> GoldCoins.copy(alpha = 0.2f)
                                            else -> LiveRed.copy(alpha = 0.2f)
                                        },
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            u.status,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when (u.status) {
                                                "Active" -> NeonLime
                                                "Warned" -> GoldCoins
                                                else -> LiveRed
                                            },
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text("UID: ${u.ffUid} • Warnings: ${u.warningsCount}", fontSize = 11.sp, color = TextSecondary)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (u.status == "Banned") {
                                    Button(
                                        onClick = {
                                            TournamentService.unbanUser(u.id)
                                            onShowToast("Unbanned user '${u.ign}'")
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("Unban", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            TournamentService.warnUser(u.id)
                                            onShowToast("Warned user '${u.ign}'")
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = ElevatedSurface),
                                        shape = RoundedCornerShape(8.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("Warn", color = GoldCoins, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Button(
                                        onClick = { banConfirmUser = u },
                                        colors = ButtonDefaults.buttonColors(containerColor = LiveRed.copy(alpha = 0.2f)),
                                        shape = RoundedCornerShape(8.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, LiveRed.copy(alpha = 0.5f)),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("Ban", color = LiveRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Support Tickets
        if (activeSection == "Support Tickets") {
            item {
                Text("HELP DESK TICKETS (${TournamentService.supportTickets.size})", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 1.sp)
            }

            items(TournamentService.supportTickets) { t ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(t.subject, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Surface(
                                color = if (t.status == "Open") LiveRed.copy(alpha = 0.2f) else NeonLime.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(t.status, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (t.status == "Open") LiveRed else NeonLime, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text("From: ${t.userIgn}", fontSize = 11.sp, color = NeonLime)
                        Text(t.message, fontSize = 12.sp, color = TextSecondary)

                        t.reply?.let { r ->
                            Surface(color = ElevatedSurface, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                                Text("Organizer Reply: $r", fontSize = 11.sp, color = Color.White, modifier = Modifier.padding(8.dp))
                            }
                        }
                    }
                }
            }
        }

        // Section: Audit Log
        if (activeSection == "Audit Log") {
            item {
                Text("ORGANIZER ACTION AUDIT TRAIL", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 1.sp)
            }

            items(TournamentService.auditLogs) { log ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
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
                            Text(log.action, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("By: ${log.adminName}", fontSize = 10.sp, color = NeonLime)
                        }
                        Text(log.timestamp, fontSize = 10.sp, color = TextSecondary)
                    }
                }
            }
        }

        // Section: Announcements
        if (activeSection == "Announcements") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("SEND IN-APP ANNOUNCEMENT", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)

                        // Audience Selector: All Players or specific tournament
                        Text("Target Audience:", fontSize = 11.sp, color = TextSecondary)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("All Players (Broadcast)", "Selected Tournament").forEach { aud ->
                                val isSel = announceAudience == aud
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { announceAudience = aud },
                                    color = if (isSel) NeonLime else ElevatedSurface,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Box(modifier = Modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                                        Text(aud, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else TextSecondary)
                                    }
                                }
                            }
                        }

                        OutlinedTextField(
                            value = announceTitle,
                            onValueChange = { announceTitle = it },
                            label = { Text("Announcement Headline") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = announceMsg,
                            onValueChange = { announceMsg = it },
                            label = { Text("Message Body") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                if (announceTitle.isBlank() || announceMsg.isBlank()) {
                                    onShowToast("Please enter headline and message")
                                    return@Button
                                }
                                TournamentService.broadcastAnnouncement(announceTitle, announceMsg, announceAudience)
                                onShowToast("Dispatched announcement to $announceAudience!")
                                announceTitle = ""
                                announceMsg = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Broadcast Announcement 📢", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    // Confirmation Dialogs
    banConfirmUser?.let { u ->
        Dialog(onDismissRequest = { banConfirmUser = null }) {
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = CardSurface)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Confirm Account Ban", fontSize = 16.sp, fontWeight = FontWeight.Black, color = LiveRed)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Are you sure you want to ban player '${u.ign}' (UID: ${u.ffUid})? They will be barred from joining any tournament rooms.", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { banConfirmUser = null }) { Text("Cancel", color = TextSecondary) }
                        Button(
                            onClick = {
                                TournamentService.banUser(u.id)
                                onShowToast("Banned player '${u.ign}'")
                                banConfirmUser = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = LiveRed)
                        ) {
                            Text("Ban Player", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    cancelConfirmMatch?.let { m ->
        Dialog(onDismissRequest = { cancelConfirmMatch = null }) {
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = CardSurface)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Cancel Tournament?", fontSize = 16.sp, fontWeight = FontWeight.Black, color = LiveRed)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Cancelling '${m.title}' will notify all registered contestants and remove it from the schedule.", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { cancelConfirmMatch = null }) { Text("Keep Active", color = TextSecondary) }
                        Button(
                            onClick = {
                                TournamentService.cancelTournament(m.id)
                                onShowToast("Cancelled '${m.title}'")
                                cancelConfirmMatch = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = LiveRed)
                        ) {
                            Text("Cancel Tournament", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    removeConfirmReg?.let { reg ->
        Dialog(onDismissRequest = { removeConfirmReg = null }) {
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = CardSurface)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Remove Registration?", fontSize = 16.sp, fontWeight = FontWeight.Black, color = LiveRed)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Remove contestant '${reg.ign}' (UID: ${reg.ffUid}) from ${reg.tournamentTitle}?", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { removeConfirmReg = null }) { Text("Cancel", color = TextSecondary) }
                        Button(
                            onClick = {
                                TournamentService.removeRegistration(reg.id)
                                onShowToast("Removed '${reg.ign}' from tournament roster")
                                removeConfirmReg = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = LiveRed)
                        ) {
                            Text("Remove", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Results Preview Dialog
    if (previewResultsDialog) {
        Dialog(onDismissRequest = { previewResultsDialog = false }) {
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = CardSurface)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("OFFICIAL RESULTS PREVIEW", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color.White)
                    Text("Placement Standings & Verified Sponsor Prizes", fontSize = 11.sp, color = NeonLime, fontWeight = FontWeight.Bold)

                    Surface(color = ElevatedSurface, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("🥇 1st: $firstIgn • $firstKills Kills", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldCoins)
                            Text("Sponsor Prize: $firstPrize", fontSize = 11.sp, color = TextSecondary)
                            Divider(color = CardBorder, thickness = 1.dp)
                            Text("🥈 2nd: $secondIgn • $secondKills Kills", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC0C0C0))
                            Text("Sponsor Prize: $secondPrize", fontSize = 11.sp, color = TextSecondary)
                            Divider(color = CardBorder, thickness = 1.dp)
                            Text("🥉 3rd: $thirdIgn • $thirdKills Kills", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFCD7F32))
                            Text("Sponsor Prize: $thirdPrize", fontSize = 11.sp, color = TextSecondary)
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Button(
                            onClick = { previewResultsDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime)
                        ) {
                            Text("Close Preview", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Admin Guide Overlay Dialog
    if (showAdminGuide) {
        Dialog(onDismissRequest = { showAdminGuide = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.HelpOutline, contentDescription = null, tint = NeonLime, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Organizer Quick Guide", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }

                    Text("1. Tournaments: Schedule 100% free entry matches with sponsor prize labels.", fontSize = 12.sp, color = TextSecondary)
                    Text("2. Room Manager: Register Custom Room ID & Password. Set to auto-reveal 15 minutes before kick-off.", fontSize = 12.sp, color = TextSecondary)
                    Text("3. Registrations: Approve or remove contestants and export rosters to CSV.", fontSize = 12.sp, color = TextSecondary)
                    Text("4. Results: Submit final Booyah placements (1st to 12th) with kills. Prizes are sponsor-fulfilled.", fontSize = 12.sp, color = TextSecondary)
                    Text("5. Prize Tracker: Advance prize status (Recorded → Sponsor Notified → Delivered by Sponsor).", fontSize = 12.sp, color = TextSecondary)
                    Text("6. Reports: Review player disputes and cheat telemetry.", fontSize = 12.sp, color = TextSecondary)

                    Button(
                        onClick = { showAdminGuide = false },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Got It", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ConsoleMetricCard(
    label: String,
    value: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = tint)
            Spacer(modifier = Modifier.height(2.dp))
            Text(label, fontSize = 10.sp, color = TextSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Composable
fun EmptyStateCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonLime, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(message, fontSize = 12.sp, color = TextSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}
