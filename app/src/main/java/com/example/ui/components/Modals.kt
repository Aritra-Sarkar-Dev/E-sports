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
    userWallet: WalletInfo,
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
                        Text(match.title, fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextPrimary, maxLines = 1)
                        Text("${match.map} • ${match.mode} • ${match.startTimeText}", fontSize = 11.sp, color = TextMuted)
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                // Sub Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ChipBg)
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
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) Color.Black else TextMuted
                            )
                        }
                    }
                }

                // Tab Content
                Box(modifier = Modifier.weight(1f)) {
                    when (selectedTab) {
                        "Overview" -> {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                item {
                                    // Room Details card if registered
                                    if (match.isJoined) {
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(16.dp),
                                            colors = CardDefaults.cardColors(containerColor = ChipBg),
                                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(ElectricViolet, NeonLime)))
                                        ) {
                                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                                Text("CUSTOM ROOM ACCESS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ElectricViolet)
                                                if (match.isRoomRevealed && match.roomId != null) {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Column {
                                                            Text("Room ID: ${match.roomId}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                                                            Text("Password: ${match.roomPass}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = NeonLime)
                                                        }
                                                        Button(
                                                            onClick = onCopyRoomCredentials,
                                                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime, contentColor = Color.Black),
                                                            shape = RoundedCornerShape(8.dp)
                                                        ) {
                                                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                                            Spacer(modifier = Modifier.width(4.dp))
                                                            Text("Copy", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                        }
                                                    }
                                                } else {
                                                    Text(
                                                        "Room ID & Password will be unlocked 10 minutes prior to match schedule. Keep the app open to receive room credentials.",
                                                        fontSize = 12.sp,
                                                        color = TextMuted
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                item {
                                    // Quick Stats Table
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(ChipBg)
                                            .padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("Total Prize Pool", fontSize = 12.sp, color = TextMuted)
                                            Text("₹${match.prizePool}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldPrize)
                                        }
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("Per Kill Bounty", fontSize = 12.sp, color = TextMuted)
                                            Text("₹${match.perKillPrize}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = FireOrange)
                                        }
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("Entry Fee", fontSize = 12.sp, color = TextMuted)
                                            Text(if (match.entryFee == 0) "FREE" else "₹${match.entryFee}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NeonLime)
                                        }
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("Map / Mode", fontSize = 12.sp, color = TextMuted)
                                            Text("${match.map} • ${match.mode}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        }
                                    }
                                }

                                item {
                                    Text("Organizer Note", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text(
                                        "Matches are hosted in official Free Fire custom esports rooms. All participants must enter room 5 minutes before scheduled match start. Teaming and cheating will result in permanent ban.",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                            }
                        }

                        "Prize Table" -> {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                item {
                                    Text("POSITION REWARDS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                                }
                                val prizeDistribution = listOf(
                                    "1st Place (Booyah)" to (match.prizePool * 0.45).toInt(),
                                    "2nd Place" to (match.prizePool * 0.25).toInt(),
                                    "3rd Place" to (match.prizePool * 0.15).toInt(),
                                    "4th - 5th Place" to (match.prizePool * 0.05).toInt(),
                                    "Per Kill Bounty" to match.perKillPrize
                                )
                                items(prizeDistribution) { (pos, reward) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(ChipBg)
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(pos, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text("₹$reward", fontSize = 13.sp, fontWeight = FontWeight.Black, color = GoldPrize)
                                    }
                                }
                            }
                        }

                        "Rules" -> {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                val rules = listOf(
                                    "1. Mobile players only. PC emulators, iPads, and tablets are strictly prohibited.",
                                    "2. In-game name and FF UID must match your registered profile.",
                                    "3. Teaming with opponents or using any 3rd party scripts results in permanent hardware ban.",
                                    "4. Screenshot submission is mandatory in case of disputes.",
                                    "5. Payouts are credited instantly to your winnings wallet within 15 minutes of match completion."
                                )
                                items(rules) { rule ->
                                    Text(rule, fontSize = 12.sp, color = TextPrimary, lineHeight = 16.sp)
                                }
                            }
                        }

                        "Players" -> {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                item {
                                    Text("Registered Players (${match.joinedSlots}/${match.maxSlots})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextMuted)
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
                                            .background(ChipBg)
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
                                                    .size(28.dp)
                                                    .clip(CircleShape)
                                                    .background(NeonLime.copy(alpha = 0.15f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(Icons.Default.Person, contentDescription = null, tint = NeonLime, modifier = Modifier.size(16.dp))
                                            }
                                            Text(ign, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        }
                                        Text(team, fontSize = 11.sp, color = TextMuted)
                                    }
                                }
                            }
                        }
                    }
                }

                // Bottom Action
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
                            text = if (match.entryFee == 0) "Join For Free" else "Join Match • ₹${match.entryFee}",
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
    wallet: WalletInfo,
    userProfile: UserProfile,
    onDismiss: () -> Unit,
    onConfirmJoin: (String, List<String>) -> Unit,
    onAddMoney: () -> Unit
) {
    var inGameName by remember { mutableStateOf(userProfile.ign) }
    var teammate1 by remember { mutableStateOf("") }
    var teammate2 by remember { mutableStateOf("") }
    var teammate3 by remember { mutableStateOf("") }
    var agreeTerms by remember { mutableStateOf(true) }

    val hasEnoughBalance = wallet.total >= match.entryFee

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
                        Text("JOIN TOURNAMENT", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                        Text("${match.title} • ${match.mode}", fontSize = 11.sp, color = TextMuted)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                // Balance summary
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ChipBg)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Entry Fee", fontSize = 11.sp, color = TextMuted)
                        Text(if (match.entryFee == 0) "FREE" else "₹${match.entryFee}", fontSize = 15.sp, fontWeight = FontWeight.Black, color = NeonLime)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Wallet Balance", fontSize = 11.sp, color = TextMuted)
                        Text("₹${wallet.total}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = if (hasEnoughBalance) TextPrimary else LiveRed)
                    }
                }

                // In-Game Name Input
                OutlinedTextField(
                    value = inGameName,
                    onValueChange = { inGameName = it },
                    label = { Text("Your Free Fire IGN", fontSize = 12.sp) },
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
                        label = { Text("Teammate 1 FF UID / IGN", fontSize = 12.sp) },
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
                        label = { Text("Teammate 2 FF UID / IGN", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonLime, unfocusedBorderColor = CardBorder),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = teammate3,
                        onValueChange = { teammate3 = it },
                        label = { Text("Teammate 3 FF UID / IGN", fontSize = 12.sp) },
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
                    Text("I confirm I am 18+ and agree to Fair Play & Anti-Cheat rules.", fontSize = 11.sp, color = TextMuted)
                }

                // Action buttons
                if (!hasEnoughBalance && match.entryFee > 0) {
                    Button(
                        onClick = {
                            onDismiss()
                            onAddMoney()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FireOrange, contentColor = Color.White)
                    ) {
                        Text("Low Balance • Add ₹${match.entryFee - wallet.total}", fontSize = 14.sp, fontWeight = FontWeight.Black)
                    }
                } else {
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
                        Text("Confirm Registration", fontSize = 14.sp, fontWeight = FontWeight.Black)
                    }
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
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                // Spin Visual Display
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(Brush.sweepGradient(listOf(GoldPrize, FireOrange, ElectricViolet, NeonLime, GoldPrize)))
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
                                tint = GoldPrize,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                if (wonCoins != null) "+$wonCoins" else "WIN UP TO",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = GoldPrize
                            )
                            Text("200 COINS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                    }
                }

                Text(
                    text = if (wonCoins != null) "🎉 You won $wonCoins Coins! Credited to rewards." else "Spin once every 24 hours to win bonus loyalty coins.",
                    fontSize = 12.sp,
                    color = if (wonCoins != null) NeonLime else TextMuted
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
fun AddMoneyModal(
    onDismiss: () -> Unit,
    onConfirmAdd: (Int) -> Unit
) {
    var amountText by remember { mutableStateOf("100") }
    val presets = listOf(50, 100, 200, 500)
    var selectedUpi by remember { mutableStateOf("PhonePe") }
    val upiApps = listOf("PhonePe", "Google Pay", "Paytm", "BHIM UPI")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
                .testTag("add_money_modal"),
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
                    Text("ADD MONEY TO WALLET", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                // Amount Input
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Deposit Amount (₹)", fontSize = 12.sp) },
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

                // Presets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presets.forEach { preset ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (amountText == "$preset") NeonLime.copy(alpha = 0.2f) else ChipBg)
                                .border(1.dp, if (amountText == "$preset") NeonLime else CardBorder, RoundedCornerShape(10.dp))
                                .clickable { amountText = "$preset" }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+₹$preset", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (amountText == "$preset") NeonLime else TextPrimary)
                        }
                    }
                }

                // UPI Apps
                Text("SELECT UPI APP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    upiApps.forEach { app ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selectedUpi == app) ChipBg else Color.Transparent)
                                .border(1.dp, if (selectedUpi == app) NeonLime else CardBorder, RoundedCornerShape(10.dp))
                                .clickable { selectedUpi = app }
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(app, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            RadioButton(
                                selected = selectedUpi == app,
                                onClick = { selectedUpi = app },
                                colors = RadioButtonDefaults.colors(selectedColor = NeonLime)
                            )
                        }
                    }
                }

                val amountInt = amountText.toIntOrNull() ?: 0

                Button(
                    onClick = {
                        if (amountInt > 0) {
                            onConfirmAdd(amountInt)
                            onDismiss()
                        }
                    },
                    enabled = amountInt >= 10,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("proceed_deposit_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonLime, contentColor = Color.Black)
                ) {
                    Text("Proceed to Pay ₹$amountInt", fontSize = 14.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun WithdrawModal(
    currentWinnings: Int,
    linkedUpi: String,
    onDismiss: () -> Unit,
    onConfirmWithdraw: (Int, String) -> Unit
) {
    var amountText by remember { mutableStateOf("100") }
    var upiId by remember { mutableStateOf(linkedUpi) }
    val amountInt = amountText.toIntOrNull() ?: 0

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
                .testTag("withdraw_modal"),
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
                    Text("INSTANT UPI WITHDRAWAL", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                // Balance info
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ChipBg)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Withdrawable Winnings", fontSize = 11.sp, color = TextMuted)
                        Text("₹$currentWinnings", fontSize = 16.sp, fontWeight = FontWeight.Black, color = GoldPrize)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Minimum", fontSize = 11.sp, color = TextMuted)
                        Text("₹100", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                }

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Withdrawal Amount (₹)", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldPrize, unfocusedBorderColor = CardBorder),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = upiId,
                    onValueChange = { upiId = it },
                    label = { Text("UPI ID (e.g. name@okhdfcbank)", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldPrize, unfocusedBorderColor = CardBorder),
                    shape = RoundedCornerShape(12.dp)
                )

                Text(
                    "⚡ Payouts are transferred automatically via UPI within 60 seconds with 0% deduction fees.",
                    fontSize = 11.sp,
                    color = TextMuted
                )

                Button(
                    onClick = {
                        if (amountInt in 100..currentWinnings && upiId.isNotBlank()) {
                            onConfirmWithdraw(amountInt, upiId)
                            onDismiss()
                        }
                    },
                    enabled = amountInt in 100..currentWinnings && upiId.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_withdraw_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrize, contentColor = Color.Black)
                ) {
                    Text("Withdraw ₹$amountInt to UPI", fontSize = 14.sp, fontWeight = FontWeight.Black)
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
                        Text("Top Free Fire players & cash earnings", fontSize = 11.sp, color = TextMuted)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                // Period switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(ChipBg)
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
                            Text(period, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSel) Color.Black else TextMuted)
                        }
                    }
                }

                // Top 3 Podium
                val top3 = rows.take(3)
                if (top3.size >= 3) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Rank 2 (Silver)
                        PodiumColumn(top3[1], height = 80.dp, color = Color(0xFFC0C0C0), rank = 2)
                        // Rank 1 (Gold)
                        PodiumColumn(top3[0], height = 110.dp, color = GoldPrize, rank = 1)
                        // Rank 3 (Bronze)
                        PodiumColumn(top3[2], height = 70.dp, color = Color(0xFFCD7F32), rank = 3)
                    }
                }

                // Remaining Ranks
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(rows.drop(3)) { row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ChipBg)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("#${row.rank}", fontSize = 12.sp, fontWeight = FontWeight.Black, color = TextMuted)
                                Column {
                                    Text(row.ign, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text("${row.wins} Wins • ${row.kills} Kills", fontSize = 10.sp, color = TextMuted)
                                }
                            }

                            Text("₹${row.earnings}", fontSize = 13.sp, fontWeight = FontWeight.Black, color = GoldPrize)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PodiumColumn(row: LeaderboardRow, height: androidx.compose.ui.unit.Dp, color: Color, rank: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(row.ign.take(8), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextPrimary, maxLines = 1)
        Text("₹${row.earnings}", fontSize = 11.sp, fontWeight = FontWeight.Black, color = color)

        Box(
            modifier = Modifier
                .width(68.dp)
                .height(height)
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(color.copy(alpha = 0.25f))
                .border(1.dp, color, RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("#$rank", fontSize = 18.sp, fontWeight = FontWeight.Black, color = color)
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
                        Text("Tournament reminders & wallet updates", fontSize = 11.sp, color = TextMuted)
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
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
                                .background(if (!notif.isRead) NeonLime.copy(alpha = 0.08f) else ChipBg)
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
                                            "wallet" -> NeonLime.copy(alpha = 0.2f)
                                            else -> FireOrange.copy(alpha = 0.2f)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (notif.type) {
                                        "match" -> Icons.Default.SportsEsports
                                        "wallet" -> Icons.Default.AccountBalanceWallet
                                        else -> Icons.Default.Campaign
                                    },
                                    contentDescription = null,
                                    tint = when (notif.type) {
                                        "match" -> ElectricViolet
                                        "wallet" -> NeonLime
                                        else -> FireOrange
                                    },
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(notif.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text(notif.message, fontSize = 11.sp, color = TextMuted)
                                Text(notif.timeAgo, fontSize = 9.sp, color = TextMuted, modifier = Modifier.padding(top = 2.dp))
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
                        Text(match?.title ?: "Select tournament to dispute", fontSize = 11.sp, color = TextMuted)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Text("REASON FOR DISPUTE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    reasons.forEach { r ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (reason == r) ChipBg else Color.Transparent)
                                .clickable { reason = r }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = reason == r,
                                onClick = { reason = r },
                                colors = RadioButtonDefaults.colors(selectedColor = FireOrange)
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
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = FireOrange, unfocusedBorderColor = CardBorder),
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
                    colors = ButtonDefaults.buttonColors(containerColor = FireOrange, contentColor = Color.White)
                ) {
                    Text("Submit Report to Referee", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
