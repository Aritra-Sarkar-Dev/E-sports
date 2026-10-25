package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.model.*
import com.example.ui.theme.*

@Composable
fun MatchDetailModal(
    match: EsportsMatch,
    onDismiss: () -> Unit,
    onJoinClick: () -> Unit,
    onCopyRoomCredentials: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("Overview") }
    val detailTabs = listOf("Overview", "Prize Table", "Rules", "Players")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
                .testTag("match_detail_modal"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header: Title & Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(match.title, fontSize = 15.sp, fontWeight = FontWeight.Black, color = TextPrimary, maxLines = 1)
                        Text("${match.map} • ${match.mode} • Sponsor: ${match.sponsor}", fontSize = 11.sp, color = TextSecondary)
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                // Sub Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ElevatedSurface)
                        .padding(3.dp)
                ) {
                    detailTabs.forEach { tab ->
                        val isSel = selectedTab == tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(9.dp))
                                .background(if (isSel) NeonLime else Color.Transparent)
                                .clickable { selectedTab = tab }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                fontSize = 11.sp,
                                fontWeight = if (isSel) FontWeight.Black else FontWeight.Bold,
                                color = if (isSel) Color.Black else TextSecondary
                            )
                        }
                    }
                }

                // Content Switcher
                Box(modifier = Modifier.weight(1f)) {
                    when (selectedTab) {
                        "Overview" -> {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                item {
                                    // Room Credentials Card
                                    Card(
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, if (match.roomId != null) NeonLime.copy(alpha = 0.5f) else CardBorder)
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(Icons.Default.VpnKey, contentDescription = null, tint = NeonLime, modifier = Modifier.size(16.dp))
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text("CUSTOM ROOM ACCESS", fontSize = 10.sp, fontWeight = FontWeight.Black, color = NeonLime)
                                                }
                                                if (match.roomId != null) {
                                                    Surface(
                                                        color = NeonLime.copy(alpha = 0.2f),
                                                        shape = RoundedCornerShape(8.dp)
                                                    ) {
                                                        Text("UNLOCKED", fontSize = 9.sp, fontWeight = FontWeight.Black, color = NeonLime, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                                    }
                                                } else {
                                                    Text("Reveals 15m prior", fontSize = 10.sp, color = TextSecondary)
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            if (match.roomId != null) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column {
                                                        Text("Room ID: ${match.roomId}", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                                                        Text("Password: ${match.roomPass ?: "ffpro"}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                                                    }
                                                    Button(
                                                        onClick = onCopyRoomCredentials,
                                                        colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                                                        shape = RoundedCornerShape(10.dp),
                                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                                    ) {
                                                        Text("Copy", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Black)
                                                    }
                                                }
                                            } else {
                                                Text(
                                                    "Credentials are encrypted and automatically unlock for registered players 15 minutes before the match start time.",
                                                    fontSize = 11.sp,
                                                    color = TextSecondary,
                                                    lineHeight = 15.sp
                                                )
                                            }
                                        }
                                    }
                                }

                                item {
                                    // 3 Highlights: Free Entry, Prize Pool, Per kill
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Card(
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                                        ) {
                                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text("Entry", fontSize = 9.sp, color = TextSecondary)
                                                Text("FREE", fontSize = 13.sp, fontWeight = FontWeight.Black, color = NeonLime)
                                            }
                                        }

                                        Card(
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                                        ) {
                                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text("Sponsor Pool", fontSize = 9.sp, color = TextSecondary)
                                                Text("₹${match.prizePool}", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                                            }
                                        }

                                        Card(
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                                        ) {
                                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text("Per Kill", fontSize = 9.sp, color = TextSecondary)
                                                Text("+10 XP", fontSize = 13.sp, fontWeight = FontWeight.Black, color = ElectricViolet)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        "Prize Table" -> {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                val prizeDistribution = listOf(
                                    "1st Place (Booyah)" to "₹${(match.prizePool * 0.5).toInt()} Trophy + Kit",
                                    "2nd Place" to "₹${(match.prizePool * 0.3).toInt()} Trophy",
                                    "3rd Place" to "₹${(match.prizePool * 0.2).toInt()} Trophy",
                                    "Per Kill Reward" to "+10 XP to Gamer Profile"
                                )
                                items(prizeDistribution) { (pos, reward) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(ElevatedSurface)
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(pos, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(reward, fontSize = 12.sp, fontWeight = FontWeight.Black, color = NeonLime)
                                    }
                                }
                            }
                        }

                        "Rules" -> {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                val rules = listOf(
                                    "1. 100% Free Entry tournament sponsored by ${match.sponsor}.",
                                    "2. Mobile players only. PC emulators and third-party recoil scripts result in permanent ban.",
                                    "3. In-game name and FF UID must match your registered profile.",
                                    "4. Teaming up in Solo or collusion will forfeit any sponsor prize.",
                                    "5. Room credentials unlock 15 minutes before the match."
                                )
                                items(rules) { rule ->
                                    Text(rule, fontSize = 11.sp, color = TextPrimary, lineHeight = 16.sp)
                                }
                            }
                        }

                        "Players" -> {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                item {
                                    Text("Registered Players (${match.joinedSlots}/${match.maxSlots})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                                }
                                val samplePlayers = listOf(
                                    "⚡NEON_SOUL⚡" to "Team Neon",
                                    "Viper_007" to "Solo",
                                    "GhostRider" to "Phoenix Esports",
                                    "ShadowKill" to "Solo",
                                    "DarkAura" to "Dragon Claw",
                                    "RogueTitan" to "Solo"
                                )
                                items(samplePlayers) { (ign, team) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(ElevatedSurface)
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(26.dp)
                                                    .clip(CircleShape)
                                                    .background(NeonLime.copy(alpha = 0.15f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(Icons.Default.Person, contentDescription = null, tint = NeonLime, modifier = Modifier.size(16.dp))
                                            }
                                            Text(ign, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        }
                                        Text(team, fontSize = 11.sp, color = TextSecondary)
                                    }
                                }
                            }
                        }
                    }
                }

                // Bottom Join Action
                if (match.status == "Upcoming" && !match.isJoined && match.joinedSlots < match.maxSlots) {
                    Button(
                        onClick = {
                            onDismiss()
                            onJoinClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("detail_modal_join_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonLime, contentColor = Color.Black)
                    ) {
                        Text(
                            text = "Join Free Tournament",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JoinMatchModal(
    match: EsportsMatch,
    userProfile: UserProfile,
    onDismiss: () -> Unit,
    onConfirmJoin: (String, List<String>) -> Unit
) {
    var inGameName by remember { mutableStateOf(userProfile.ign) }
    var teammate1 by remember { mutableStateOf("") }
    var teammate2 by remember { mutableStateOf("") }
    var teammate3 by remember { mutableStateOf("") }
    var agreeTerms by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
                .testTag("join_match_modal"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("FREE TOURNAMENT ENTRY", fontSize = 15.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                        Text("${match.title} • ${match.mode}", fontSize = 11.sp, color = TextSecondary)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                // Free Entry Pill
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = ElevatedSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonLime.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Entry Fee", fontSize = 10.sp, color = TextSecondary)
                            Text("100% FREE ENTRY", fontSize = 13.sp, fontWeight = FontWeight.Black, color = NeonLime)
                        }
                        Surface(
                            color = NeonLime,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("SPONSORED", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                }

                // In-Game Name Input
                OutlinedTextField(
                    value = inGameName,
                    onValueChange = { inGameName = it },
                    label = { Text("Your Free Fire IGN", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonLime,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                // Team members if Duo or Squad
                if (match.mode == "Duo" || match.mode == "Squad") {
                    OutlinedTextField(
                        value = teammate1,
                        onValueChange = { teammate1 = it },
                        label = { Text("Teammate 1 FF UID / IGN", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonLime, unfocusedBorderColor = CardBorder),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                if (match.mode == "Squad") {
                    OutlinedTextField(
                        value = teammate2,
                        onValueChange = { teammate2 = it },
                        label = { Text("Teammate 2 FF UID / IGN", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonLime, unfocusedBorderColor = CardBorder),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = teammate3,
                        onValueChange = { teammate3 = it },
                        label = { Text("Teammate 3 FF UID / IGN", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonLime, unfocusedBorderColor = CardBorder),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // 18+ and Fair Play Checkbox
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { agreeTerms = !agreeTerms },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(
                        checked = agreeTerms,
                        onCheckedChange = { agreeTerms = it },
                        colors = CheckboxDefaults.colors(checkedColor = NeonLime, checkmarkColor = Color.Black)
                    )
                    Text("I confirm I am 18+ and agree to Fair Play & Anti-Cheat rules.", fontSize = 11.sp, color = TextSecondary)
                }

                Button(
                    onClick = {
                        val teammates = listOfNotNull(
                            teammate1.takeIf { it.isNotBlank() },
                            teammate2.takeIf { it.isNotBlank() },
                            teammate3.takeIf { it.isNotBlank() }
                        )
                        onConfirmJoin(inGameName, teammates)
                        onDismiss()
                    },
                    enabled = agreeTerms && inGameName.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_join_match_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonLime, contentColor = Color.Black)
                ) {
                    Text("Reserve Free Slot", fontSize = 14.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun SpinWheelModal(
    onDismiss: () -> Unit,
    onSpinWin: (Int) -> Unit
) {
    var isSpinning by remember { mutableStateOf(false) }
    var wonCoins by remember { mutableStateOf<Int?>(null) }
    val prizes = listOf(20, 50, 100, 200, 30, 80)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
                .testTag("spin_wheel_modal"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("DAILY LUCKY SPIN", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                // Spin Visual Display
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Brush.sweepGradient(listOf(GoldCoins, StreakOrange, ElectricViolet, NeonLime, GoldCoins)))
                        .border(4.dp, CardSurface, CircleShape)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(CardSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = GoldCoins,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                if (wonCoins != null) "+$wonCoins" else "WIN UP TO",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = GoldCoins
                            )
                            Text("200 COINS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                    }
                }

                Text(
                    text = if (wonCoins != null) "🎉 You won $wonCoins Loyalty Coins!" else "Spin once every 24 hours to win bonus loyalty coins.",
                    fontSize = 11.sp,
                    color = if (wonCoins != null) NeonLime else TextSecondary
                )

                Button(
                    onClick = {
                        isSpinning = true
                        val won = prizes.random()
                        wonCoins = won
                        onSpinWin(won)
                    },
                    enabled = wonCoins == null && !isSpinning,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("spin_wheel_action_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonLime, contentColor = Color.Black)
                ) {
                    Text(if (wonCoins != null) "Claimed Today ✓" else "SPIN NOW", fontSize = 14.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun LeaderboardModal(
    rows: List<LeaderboardRow>,
    onDismiss: () -> Unit
) {
    var selectedPeriod by remember { mutableStateOf("Weekly") }
    val periods = listOf("Daily", "Weekly", "All-Time")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
                .testTag("leaderboard_modal"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("HALL OF CHAMPIONS", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                        Text("Top Free Fire players & sponsor trophies", fontSize = 11.sp, color = TextSecondary)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                // Period switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(ElevatedSurface)
                        .padding(3.dp)
                ) {
                    periods.forEach { period ->
                        val isSel = selectedPeriod == period
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) NeonLime else Color.Transparent)
                                .clickable { selectedPeriod = period }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(period, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else TextSecondary)
                        }
                    }
                }

                // Remaining Ranks
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(rows) { row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(ElevatedSurface)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("#${row.rank}", fontSize = 12.sp, fontWeight = FontWeight.Black, color = if (row.rank == 1) NeonLime else TextSecondary)
                                Column {
                                    Text(row.ign, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text("${row.wins} Booyah • ${row.kills} Kills", fontSize = 10.sp, color = TextSecondary)
                                }
                            }

                            Text("${row.prizesWon} Prizes Won", fontSize = 12.sp, fontWeight = FontWeight.Black, color = GoldCoins)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationsModal(
    notifications: List<InAppNotification>,
    onDismiss: () -> Unit,
    onMarkAllRead: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
                .testTag("notifications_modal"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("NOTIFICATIONS", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                        Text("Tournament reminders & room alerts", fontSize = 11.sp, color = TextSecondary)
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        "Mark all read",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonLime,
                        modifier = Modifier.clickable { onMarkAllRead() }
                    )
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notifications) { notif ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (!notif.isRead) NeonLime.copy(alpha = 0.08f) else ElevatedSurface)
                                .border(1.dp, if (!notif.isRead) NeonLime.copy(alpha = 0.3f) else CardBorder, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (notif.type) {
                                            "match" -> ElectricViolet.copy(alpha = 0.2f)
                                            "room" -> NeonLime.copy(alpha = 0.2f)
                                            else -> GoldCoins.copy(alpha = 0.2f)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (notif.type) {
                                        "match" -> Icons.Default.SportsEsports
                                        "room" -> Icons.Default.VpnKey
                                        else -> Icons.Default.Campaign
                                    },
                                    contentDescription = null,
                                    tint = when (notif.type) {
                                        "match" -> ElectricViolet
                                        "room" -> NeonLime
                                        else -> GoldCoins
                                    },
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(notif.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text(notif.message, fontSize = 11.sp, color = TextSecondary)
                                Text(notif.timeAgo, fontSize = 9.sp, color = TextSecondary, modifier = Modifier.padding(top = 2.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReportModal(
    match: EsportsMatch?,
    onDismiss: () -> Unit,
    onSubmitReport: (String, String) -> Unit
) {
    var reason by remember { mutableStateOf("Hacking / Aimbot") }
    var description by remember { mutableStateOf("") }
    val reasons = listOf("Hacking / Aimbot", "Teaming up in Solo", "Fake Result / Score", "Room Password Delay", "Other Dispute")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
                .testTag("report_modal"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("DISPUTE / REPORT", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                        Text(match?.title ?: "Select tournament to dispute", fontSize = 11.sp, color = TextSecondary)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Text("REASON FOR DISPUTE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    reasons.forEach { r ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (reason == r) ElevatedSurface else Color.Transparent)
                                .clickable { reason = r }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = reason == r,
                                onClick = { reason = r },
                                colors = RadioButtonDefaults.colors(selectedColor = NeonLime)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(r, fontSize = 12.sp, color = TextPrimary)
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Describe what happened...", fontSize = 12.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonLime, unfocusedBorderColor = CardBorder),
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = {
                        onSubmitReport(reason, description)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonLime, contentColor = Color.Black)
                ) {
                    Text("Submit Report to Referee", fontSize = 14.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
