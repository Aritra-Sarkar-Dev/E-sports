package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.model.*
import com.example.ui.theme.*
import com.example.ui.screens.LeaderboardSectionView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

// 1. MATCH DETAIL MODAL
@Composable
fun MatchDetailModal(
    match: EsportsMatch,
    onDismiss: () -> Unit,
    onJoinClick: () -> Unit,
    onCopyRoomCredentials: () -> Unit,
    onShareTournament: () -> Unit = {}
) {
    val context = LocalContext.current
    var showShareSuccess by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = NeonLime, shape = RoundedCornerShape(8.dp)) {
                            Text("FREE ENTRY", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Black, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                        }
                        Surface(color = ElevatedSurface, shape = RoundedCornerShape(8.dp), border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)) {
                            Text(match.mode, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            showShareSuccess = true
                            onShareTournament()
                        }) {
                            Icon(Icons.Default.Share, contentDescription = "Share", tint = NeonLime, modifier = Modifier.size(20.dp))
                        }
                        IconButton(onClick = { onDismiss() }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary, modifier = Modifier.size(20.dp))
                        }
                    }
                }

                if (showShareSuccess) {
                    Surface(color = ElevatedSurface, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Text("Share link copied! Invite your squadmates.", fontSize = 11.sp, color = NeonLime, modifier = Modifier.padding(6.dp), textAlign = TextAlign.Center)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Column {
                            Text(match.title, fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Official Sponsor: ${match.sponsor}", fontSize = 12.sp, color = ElectricViolet, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Mobile Only & Anti-Cheat Badge
                    item {
                        Surface(
                            color = ElevatedSurface,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Shield, contentDescription = null, tint = NeonLime, modifier = Modifier.size(18.dp))
                                Column {
                                    Text("MOBILE ONLY • NO EMULATOR", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
                                    Text("Anti-cheat protocol active. Min Free Fire Level ${match.minLevel}.", fontSize = 10.sp, color = TextSecondary)
                                }
                            }
                        }
                    }

                    // Key Metrics: Prize, Elimination XP, Kick-Off
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Sponsor Pool", fontSize = 10.sp, color = TextSecondary)
                                    Text("₹${match.prizePool}", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color.White)
                                    Text("Sponsor-funded", fontSize = 9.sp, color = NeonLime, fontWeight = FontWeight.Bold)
                                }
                            }

                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Per-Kill Points", fontSize = 10.sp, color = TextSecondary)
                                    Text("+${match.perKillPoints} pts", fontSize = 15.sp, fontWeight = FontWeight.Black, color = ElectricViolet)
                                    Text("+${match.perKillXp} XP bonus", fontSize = 9.sp, color = TextSecondary)
                                }
                            }

                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Kick-Off", fontSize = 10.sp, color = TextSecondary)
                                    Text(match.startTimeText, fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                                    Text("Server Time", fontSize = 9.sp, color = TextSecondary)
                                }
                            }
                        }
                    }

                    // Room Credentials Box (if joined)
                    if (match.isJoined) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonLime.copy(alpha = 0.6f))
                            ) {
                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Text("CUSTOM ROOM ACCESS", fontSize = 12.sp, fontWeight = FontWeight.Black, color = NeonLime)
                                        Surface(color = NeonLime.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                                            Text("Registered", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeonLime, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }

                                    if (match.isRoomRevealed && match.roomId != null) {
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                            Column {
                                                Text("Room ID: ${match.roomId}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                                                Text("Password: ${match.roomPass ?: "ffpro99"}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ElectricViolet)
                                            }

                                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                Button(
                                                    onClick = { onCopyRoomCredentials() },
                                                    colors = ButtonDefaults.buttonColors(containerColor = ElevatedSurface),
                                                    shape = RoundedCornerShape(10.dp),
                                                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                                ) {
                                                    Text("Copy", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                }

                                                Button(
                                                    onClick = {
                                                        try {
                                                            val intent = context.packageManager.getLaunchIntentForPackage("com.dts.freefiremax")
                                                            if (intent != null) {
                                                                context.startActivity(intent)
                                                            } else {
                                                                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dts.freefiremax"))
                                                                context.startActivity(webIntent)
                                                            }
                                                        } catch (e: Exception) {
                                                            // Fallback
                                                        }
                                                    },
                                                    colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                                                    shape = RoundedCornerShape(10.dp),
                                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                                ) {
                                                    Text("Open Free Fire", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Black)
                                                }
                                            }
                                        }
                                    } else {
                                        Text("Credentials auto-reveal 15 minutes before the match start time.", fontSize = 12.sp, color = TextSecondary)
                                    }
                                }
                            }
                        }
                    }

                    // Placement Scoring Table
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("PLACEMENT POINTS", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Surface(
                                color = ElevatedSurface,
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                            ) {
                                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("1st (Booyah!): 12 pts", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldCoins)
                                        Text("2nd: 9 pts", fontSize = 11.sp, color = Color.White)
                                        Text("3rd: 8 pts", fontSize = 11.sp, color = Color.White)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("4th: 7 pts", fontSize = 11.sp, color = TextSecondary)
                                        Text("5th: 6 pts", fontSize = 11.sp, color = TextSecondary)
                                        Text("6th+: 1 pt each", fontSize = 11.sp, color = TextSecondary)
                                    }
                                }
                            }
                        }
                    }

                    // Rules Text
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("RULES & FAIR PLAY", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Surface(
                                color = ElevatedSurface,
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                            ) {
                                Text(
                                    text = match.rulesText,
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    lineHeight = 16.sp,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom Action
                if (!match.isJoined && match.status != "Completed") {
                    Button(
                        onClick = {
                            onDismiss()
                            onJoinClick()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Join Tournament (100% Free)", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Black)
                    }
                } else {
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = ElevatedSurface),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Close", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// 2. JOIN MATCH MODAL WITH CONFETTI ANIMATION
@Composable
fun JoinMatchModal(
    match: EsportsMatch,
    userProfile: UserProfile,
    onDismiss: () -> Unit,
    onConfirmJoin: (String, List<String>) -> Unit
) {
    var acceptedRules by remember { mutableStateOf(true) }
    var confirmedUid by remember { mutableStateOf(userProfile.ffUid) }
    var teammate1 by remember { mutableStateOf("") }
    var teammate2 by remember { mutableStateOf("") }
    var teammate3 by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { if (!isSuccess) onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isSuccess) {
                    // Confetti and Registered check animation
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonLime, modifier = Modifier.size(64.dp))
                    Text("REGISTERED!", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color.White)
                    Text("You are confirmed for ${match.title}.", fontSize = 13.sp, color = TextSecondary, textAlign = TextAlign.Center)
                    Text("Room ID will auto-reveal 15 minutes before start.", fontSize = 12.sp, color = NeonLime, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Awesome!", color = Color.Black, fontWeight = FontWeight.Black)
                    }
                } else {
                    Text("CONFIRM FREE ENTRY", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                    Text("Zero entry fees. Pure esports competition.", fontSize = 11.sp, color = NeonLime, fontWeight = FontWeight.Bold)

                    Surface(color = ElevatedSurface, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(match.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("${match.mode} • ${match.map} • ${match.startTimeText}", fontSize = 11.sp, color = TextSecondary)
                        }
                    }

                    OutlinedTextField(
                        value = confirmedUid,
                        onValueChange = { confirmedUid = it },
                        label = { Text("Your Free Fire UID") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (match.mode == "Duo" || match.mode == "Squad") {
                        Text("Teammate Free Fire UIDs (Optional now, can add later):", fontSize = 11.sp, color = TextSecondary)
                        OutlinedTextField(value = teammate1, onValueChange = { teammate1 = it }, label = { Text("Teammate 1 UID") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    }
                    if (match.mode == "Squad") {
                        OutlinedTextField(value = teammate2, onValueChange = { teammate2 = it }, label = { Text("Teammate 2 UID") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = teammate3, onValueChange = { teammate3 = it }, label = { Text("Teammate 3 UID") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { acceptedRules = !acceptedRules }
                    ) {
                        Checkbox(
                            checked = acceptedRules,
                            onCheckedChange = { acceptedRules = it },
                            colors = CheckboxDefaults.colors(checkedColor = NeonLime, checkmarkColor = Color.Black)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("I certify mobile play only (no emulator) & accept fair play rules.", fontSize = 11.sp, color = Color.White)
                    }

                    Button(
                        onClick = {
                            if (!acceptedRules) return@Button
                            onConfirmJoin(userProfile.ign, listOfNotNull(teammate1.ifBlank { null }, teammate2.ifBlank { null }, teammate3.ifBlank { null }))
                            isSuccess = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Confirm Registration (Free)", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}

// 3. DAILY SPIN: BONUS COINS MODAL
@Composable
fun SpinWheelModal(
    onDismiss: () -> Unit,
    onSpinWin: (Int) -> Unit
) {
    val prizes = listOf(20, 50, 10, 100, 30, 80, 15, 150)
    var isSpinning by remember { mutableStateOf(false) }
    var wonCoins by remember { mutableStateOf<Int?>(null) }
    var currentRotation by remember { mutableFloatStateOf(0f) }

    Dialog(onDismissRequest = { if (!isSpinning) onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("DAILY SPIN: BONUS COINS", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                Text("Loyalty coins only. Cannot be bought or withdrawn.", fontSize = 11.sp, color = TextSecondary, textAlign = TextAlign.Center)

                // Wheel Visual
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(ElevatedSurface)
                        .border(3.dp, NeonLime, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = GoldCoins,
                        modifier = Modifier
                            .size(72.dp)
                            .rotate(currentRotation)
                    )
                }

                wonCoins?.let { coins ->
                    Text("🎉 You won +$coins Loyalty Coins!", fontSize = 15.sp, fontWeight = FontWeight.Black, color = GoldCoins)
                }

                Button(
                    onClick = {
                        if (!isSpinning && wonCoins == null) {
                            isSpinning = true
                            currentRotation += 720f + Random.nextInt(360)
                            val prize = prizes.random()
                            wonCoins = prize
                            isSpinning = false
                            onSpinWin(prize)
                        } else if (wonCoins != null) {
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (wonCoins != null) "Collect & Close" else if (isSpinning) "Spinning..." else "Spin for Bonus Coins",
                        color = Color.Black,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

// 4. LEADERBOARD MODAL
@Composable
fun LeaderboardModal(
    rows: List<LeaderboardRow>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("ARENA LEADERBOARD", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                    IconButton(onClick = { onDismiss() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                LeaderboardSectionView()
            }
        }
    }
}

// 5. NOTIFICATIONS MODAL
@Composable
fun NotificationsModal(
    notifications: List<InAppNotification>,
    onDismiss: () -> Unit,
    onMarkAllRead: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("NOTIFICATIONS", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                    Row {
                        TextButton(onClick = onMarkAllRead) {
                            Text("Mark All Read", color = NeonLime, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                        }
                    }
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(notifications) { notif ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (!notif.isRead) NeonLime.copy(alpha = 0.5f) else CardBorder)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                                if (!notif.isRead) {
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(NeonLime))
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Column {
                                    Text(notif.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(notif.message, fontSize = 11.sp, color = TextSecondary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(notif.timeAgo, fontSize = 10.sp, color = ElectricViolet)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// 6. REPORT / DISPUTE MODAL
@Composable
fun ReportModal(
    match: EsportsMatch,
    onDismiss: () -> Unit,
    onSubmitReport: (String, String) -> Unit
) {
    var reason by remember { mutableStateOf("Suspected Cheating / Emulator") }
    var description by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("SUBMIT MATCH DISPUTE", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                Text("Disputes must be raised within 30 minutes of match completion. Referees review replay telemetry.", fontSize = 11.sp, color = TextSecondary)

                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Dispute Category") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Evidence & Details") },
                    placeholder = { Text("Specify suspect IGN, round number, or screenshot URL placeholder.") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
                    Button(
                        onClick = {
                            onSubmitReport(reason, description)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LiveRed)
                    ) {
                        Text("Submit to Referee", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// 7. PRIZE HISTORY & DETAIL MODALS
@Composable
fun PrizeHistoryModal(
    prizes: List<PrizeRecord>,
    onDismiss: () -> Unit,
    onSelectPrize: (PrizeRecord) -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("SPONSOR PRIZE HISTORY", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Surface(color = ElevatedSurface, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Prizes are sponsor-funded and fulfilled directly outside the app. No cash payouts or UPI.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(prizes) { p ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectPrize(p) },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(p.tournamentTitle, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Surface(
                                        color = when (p.status) {
                                            "Delivered by sponsor" -> NeonLime.copy(alpha = 0.2f)
                                            "Sponsor notified" -> ElectricViolet.copy(alpha = 0.2f)
                                            else -> CardSurface
                                        },
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(p.status, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (p.status == "Delivered by sponsor") NeonLime else Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Text("Sponsor: ${p.sponsorName} • ${p.rank}", fontSize = 11.sp, color = TextSecondary)
                                Text(p.sponsorPrize, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldCoins)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrizeDetailModal(
    prize: PrizeRecord,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("SPONSOR PRIZE DETAILS", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                Text(prize.tournamentTitle, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NeonLime)
                Text("Prize: ${prize.sponsorPrize}", fontSize = 13.sp, color = Color.White)
                Text("Sponsor: ${prize.sponsorName} • Result: ${prize.rank}", fontSize = 12.sp, color = TextSecondary)

                Divider(color = CardBorder, thickness = 1.dp)

                Text("DELIVERY TIMELINE", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)

                TimelineStep("1. Recorded by Organizer", true)
                TimelineStep("2. Sponsor Notified for Dispatch", prize.status == "Sponsor notified" || prize.status == "Delivered by sponsor")
                TimelineStep("3. Delivered by Sponsor", prize.status == "Delivered by sponsor")

                Surface(color = ElevatedSurface, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Prizes are delivered by the sponsor outside the app. The organizer contacts winners via registered email/Discord. Strictly no bank account or UPI details are required.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun TimelineStep(text: String, isDone: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(
            imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isDone) NeonLime else TextSecondary,
            modifier = Modifier.size(18.dp)
        )
        Text(text, fontSize = 12.sp, color = if (isDone) Color.White else TextSecondary, fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal)
    }
}

// 8. EDIT PROFILE MODAL
@Composable
fun EditProfileModal(
    currentIgn: String,
    currentUid: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var ign by remember { mutableStateOf(currentIgn) }
    var uid by remember { mutableStateOf(currentUid) }
    val isUidValid = remember(uid) { uid.matches(Regex("^[0-9]{8,12}$")) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("EDIT PLAYER PROFILE", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)

                OutlinedTextField(
                    value = ign,
                    onValueChange = { ign = it },
                    label = { Text("In-Game Name (IGN)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uid,
                    onValueChange = { uid = it },
                    label = { Text("Free Fire UID (8-12 digits)") },
                    supportingText = {
                        Text(
                            text = if (isUidValid) "✓ Valid UID format" else "Must be 8-12 numeric digits",
                            color = if (isUidValid) NeonLime else LiveRed,
                            fontSize = 11.sp
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
                    Button(
                        onClick = {
                            if (isUidValid && ign.isNotBlank()) {
                                onSave(ign, uid)
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonLime)
                    ) {
                        Text("Save Changes", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// 9. VERIFY ACCOUNT MODAL
@Composable
fun VerifyAccountModal(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("ACCOUNT VERIFICATION STATUS", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                Text("Mandatory for esports fair play compliance.", fontSize = 11.sp, color = TextSecondary)

                VerifyStep("Free Fire UID Linked (994821034)", true)
                VerifyStep("18+ Age Requirement Certified", true)
                VerifyStep("Level 30+ Account Tier Check", true)
                VerifyStep("Anti-Cheat Radar Scan Passed", true)

                Surface(color = ElevatedSurface, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                    Text("✓ Your account is fully verified for official sponsor tournaments.", fontSize = 11.sp, color = NeonLime, modifier = Modifier.padding(10.dp))
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Done", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun VerifyStep(title: String, isDone: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(Icons.Default.Verified, contentDescription = null, tint = if (isDone) NeonLime else TextSecondary, modifier = Modifier.size(18.dp))
        Text(title, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

// 10. FAIR PLAY & ANTI-CHEAT MODAL
@Composable
fun FairPlayModal(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = NeonLime, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Fair Play Protocol", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                }

                Text("• Free entry. Prizes are sponsor-funded. 18+ only.", fontSize = 12.sp, color = NeonLime, fontWeight = FontWeight.Bold)
                Text("• Mobile only: Emulators (Bluestacks, LDPlayer, etc.) are strictly prohibited.", fontSize = 12.sp, color = TextSecondary)
                Text("• No third-party scripts, aim-assist mods, or game file modifications.", fontSize = 12.sp, color = TextSecondary)
                Text("• Teaming or cross-team collusion leads to immediate disqualification and hardware ban.", fontSize = 12.sp, color = TextSecondary)
                Text("• Match telemetry is audited by automated anti-cheat radar.", fontSize = 12.sp, color = TextSecondary)

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("I Agree & Understand", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// 11. TERMS & PRIVACY MODAL
@Composable
fun TermsModal(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("TERMS & ESPORTS POLICY", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                Text("Free entry. Prizes are sponsor-funded. 18+ only.", fontSize = 12.sp, color = NeonLime, fontWeight = FontWeight.Bold)

                Text("FF Arena is a pure skill-based esports matchmaking platform. There are no wagers, stakes, deposits, or entry fees. Prizes are independently provided and fulfilled by verified sponsors.", fontSize = 11.sp, color = TextSecondary, lineHeight = 16.sp)
                Text("Participants must be 18 years of age or older and possess a valid Level 30+ Free Fire account.", fontSize = 11.sp, color = TextSecondary, lineHeight = 16.sp)

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// 12. HELP & SUPPORT MODAL
@Composable
fun HelpSupportModal(
    onDismiss: () -> Unit,
    onShowToast: (String) -> Unit
) {
    var ticketSubject by remember { mutableStateOf("") }
    var ticketMessage by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("HELP & DISPUTE SUPPORT", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    item {
                        Text("FREQUENTLY ASKED QUESTIONS", fontSize = 12.sp, fontWeight = FontWeight.Black, color = NeonLime)
                        Surface(color = ElevatedSurface, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Q: When does the Room ID reveal?", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("A: Room ID and Password are automatically revealed 15 minutes before kick-off.", fontSize = 11.sp, color = TextSecondary)
                                Divider(color = CardBorder, thickness = 1.dp, modifier = Modifier.padding(vertical = 6.dp))
                                Text("Q: How are sponsor prizes delivered?", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("A: Physical gear and trophies are couriered by official sponsors directly to winners.", fontSize = 11.sp, color = TextSecondary)
                            }
                        }
                    }

                    item {
                        Text("SUBMIT SUPPORT TICKET", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                        OutlinedTextField(
                            value = ticketSubject,
                            onValueChange = { ticketSubject = it },
                            label = { Text("Subject (e.g. Room password query)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = ticketMessage,
                            onValueChange = { ticketMessage = it },
                            label = { Text("Details & Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (ticketSubject.isNotBlank() && ticketMessage.isNotBlank()) {
                                    TournamentService.supportTickets.add(
                                        0,
                                        SupportTicketRecord(
                                            id = "t_${System.currentTimeMillis()}",
                                            userIgn = "NEON_SOUL",
                                            subject = ticketSubject,
                                            message = ticketMessage,
                                            status = "Open"
                                        )
                                    )
                                    onShowToast("Ticket submitted! Referee will respond shortly.")
                                    ticketSubject = ""
                                    ticketMessage = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Submit Ticket", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }

                    item {
                        Text("DIRECT REFEREE CHANNELS", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(color = ElevatedSurface, shape = RoundedCornerShape(10.dp), modifier = Modifier.weight(1f)) {
                                Text("Telegram: @FFArenaSupport", fontSize = 11.sp, color = NeonLime, modifier = Modifier.padding(10.dp), textAlign = TextAlign.Center)
                            }
                            Surface(color = ElevatedSurface, shape = RoundedCornerShape(10.dp), modifier = Modifier.weight(1f)) {
                                Text("Discord: discord.gg/ffarena", fontSize = 11.sp, color = ElectricViolet, modifier = Modifier.padding(10.dp), textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }
    }
}

// 13. REFERRAL MODAL
@Composable
fun ReferralModal(
    profile: UserProfile,
    onCopyReferral: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = NeonLime, modifier = Modifier.size(48.dp))
                Text("INVITE FRIENDS & EARN COINS", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                Text("Both you and your friend earn 100 bonus loyalty coins when they register with your code.", fontSize = 12.sp, color = TextSecondary, textAlign = TextAlign.Center)

                Surface(
                    color = ElevatedSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonLime)
                ) {
                    Text(
                        text = profile.referralCode,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonLime,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                    )
                }

                Button(
                    onClick = {
                        onCopyReferral()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Copy Code & Share", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// 14. LOGOUT CONFIRM DIALOG
@Composable
fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Confirm Log Out", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                Text("Are you sure you want to sign out of your account?", fontSize = 12.sp, color = TextSecondary)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = LiveRed)
                    ) {
                        Text("Log Out", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
