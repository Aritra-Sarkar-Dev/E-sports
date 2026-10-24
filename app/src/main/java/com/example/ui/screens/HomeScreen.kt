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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.model.EsportsMatch
import com.example.ui.model.LeaderboardRow
import com.example.ui.model.UserProfile
import com.example.ui.model.WalletInfo
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    profile: UserProfile,
    wallet: WalletInfo,
    coins: Int,
    matches: List<EsportsMatch>,
    leaderboard: List<LeaderboardRow>,
    unreadCount: Int,
    onOpenNotifications: () -> Unit,
    onOpenWallet: () -> Unit,
    onOpenSpinWheel: () -> Unit,
    onDailyCheckIn: () -> Unit,
    hasCheckedInToday: Boolean,
    onOpenMatchDetail: (EsportsMatch) -> Unit,
    onOpenLeaderboard: () -> Unit,
    onSelectModeFilter: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All Modes") }
    val categories = listOf("All Modes", "Battle Royale", "Clash Squad", "Lone Wolf", "Free Entry", "Pro Cash")

    val liveMatches = remember(matches) { matches.filter { it.status == "Live" } }
    val startingSoon = remember(matches) { matches.filter { it.status == "Upcoming" }.take(4) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
    ) {
        // Sticky Header with Avatar, Greeting, Coin chip, Wallet chip, Notification Bell
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(CardSurface)
                            .border(2.dp, NeonLime, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = NeonLime,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "WELCOME BACK",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Hey, ${profile.name.substringBefore(" ")} ⚡",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Coins Chip
                    Surface(
                        color = ChipBg,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrize.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.MonetizationOn, contentDescription = "Coins", tint = GoldPrize, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "$coins", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldPrize)
                        }
                    }

                    // Wallet Chip
                    Surface(
                        modifier = Modifier.clickable { onOpenWallet() },
                        color = ChipBg,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonLime.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Wallet", tint = NeonLime, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "₹${wallet.total}", fontSize = 12.sp, fontWeight = FontWeight.Black, color = NeonLime)
                        }
                    }

                    // Notification Bell
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ChipBg)
                            .border(1.dp, CardBorder, CircleShape)
                            .clickable { onOpenNotifications() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White, modifier = Modifier.size(18.dp))
                        if (unreadCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .align(Alignment.TopEnd)
                                    .clip(CircleShape)
                                    .background(FireOrange),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "$unreadCount", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // Hero Promo Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(FireOrange.copy(alpha = 0.7f), ElectricViolet.copy(alpha = 0.8f), CardSurface)
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            color = NeonLime,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "CYBERFRAY GRAND FINALS",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "₹50,000 SPONSOR POOL",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "Asus ROG Sponsored • Level 30+ Free Fire Verified",
                            fontSize = 11.sp,
                            color = Color(0xFFFFD54F)
                        )
                    }
                }
            }
        }

        // Quick Actions Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Daily Check in
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onDailyCheckIn() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (hasCheckedInToday) NeonLime.copy(alpha = 0.4f) else CardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = "Check-in",
                            tint = if (hasCheckedInToday) NeonLime else GoldPrize,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (hasCheckedInToday) "Streak (5)" else "Check-in",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Spin Wheel
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenSpinWheel() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Spin Wheel", tint = ElectricViolet, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Spin Wheel", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                // Invite & Earn
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.GroupAdd, contentDescription = "Invite", tint = FireOrange, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Invite & Win", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                // Support
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.HeadsetMic, contentDescription = "Support", tint = Color(0xFF00E5FF), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "24/7 Help", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // Mode Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = cat == selectedCategory
                    Surface(
                        modifier = Modifier.clickable {
                            selectedCategory = cat
                            onSelectModeFilter(cat)
                        },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) NeonLime else CardSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NeonLime else CardBorder)
                    ) {
                        Text(
                            text = cat,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.Black else TextMuted,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Live Matches Section
        if (liveMatches.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(LiveRed))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "LIVE BATTLE ARENA", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                        Text(text = "1.4k Spectating", fontSize = 11.sp, color = TextMuted)
                    }

                    liveMatches.forEach { match ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenMatchDetail(match) },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LiveRed.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(color = LiveRed, shape = RoundedCornerShape(6.dp)) {
                                        Text(
                                            text = "LIVE NOW",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(text = "₹${match.prizePool} Pool", fontSize = 13.sp, fontWeight = FontWeight.Black, color = GoldPrize)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = match.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text(text = "${match.mode} • Map: ${match.map}", fontSize = 11.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }
        }

        // Starting Soon Section
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = "STARTING SOON", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 1.sp)
                startingSoon.forEach { match ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenMatchDetail(match) },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = NeonLime.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "${match.mode} • ${match.map}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonLime,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(text = "₹${match.prizePool}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = GoldPrize)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = match.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (match.entryFee == 0) "FREE ENTRY" else "Entry: ₹${match.entryFee}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (match.entryFee == 0) NeonLime else Color.White
                                )
                                Button(
                                    onClick = { onOpenMatchDetail(match) },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text(text = if (match.isJoined) "View Room" else "Join", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Champions Strip
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenLeaderboard() },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(GoldPrize.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = "Trophy", tint = GoldPrize, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "Top Champions This Week", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(text = "1st: ${leaderboard.firstOrNull()?.ign ?: "SOUL_KING"} (₹48,500)", fontSize = 10.sp, color = TextMuted)
                        }
                    }
                    Text(text = "View >", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonLime)
                }
            }
        }
    }
}
