package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.model.EsportsMatch
import com.example.ui.model.LeaderboardRow
import com.example.ui.model.UserProfile
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    profile: UserProfile,
    coins: Int,
    matches: List<EsportsMatch>,
    leaderboard: List<LeaderboardRow>,
    unreadCount: Int,
    onOpenNotifications: () -> Unit,
    onOpenRewards: () -> Unit,
    onOpenSpinWheel: () -> Unit,
    onDailyCheckIn: () -> Unit,
    hasCheckedInToday: Boolean,
    onOpenMatchDetail: (EsportsMatch) -> Unit,
    onOpenLeaderboard: () -> Unit,
    onSelectModeFilter: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All Modes") }
    val categories = listOf("All Modes", "Squad (4v4)", "Solo Cup", "Duo", "Sponsor Cups")

    val liveMatches = remember(matches) { matches.filter { it.status == "Live" } }
    val upcomingMatches = remember(matches) { matches.filter { it.status == "Upcoming" } }
    val myJoinedMatch = remember(matches) { matches.firstOrNull { it.isJoined && it.status != "Completed" } }
    val beginnerMatches = remember(matches) { matches.filter { it.division == "Rookie" } }

    // Auto-scrolling carousel state
    var activeBannerIdx by remember { mutableIntStateOf(0) }
    val carouselBanners = remember {
        listOf(
            Triple("CYBERFRAY 2077 GRAND FINALS", "ASUS ROG Sponsored • ₹50,000 SPONSOR POOL", "MEGA SPONSOR CUP"),
            Triple("SUNDAY ROOKIE SHOWDOWN", "Red Bull Esports • ₹5,000 SPONSOR POOL", "100% FREE ENTRY"),
            Triple("CLASH SQUAD 4v4 INVITATIONAL", "Monster Energy • ₹12,000 SPONSOR POOL", "SPONSOR TROPHY")
        )
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(4500)
            activeBannerIdx = (activeBannerIdx + 1) % carouselBanners.size
        }
    }

    // Ticking countdown clock for next match
    var countdownSecs by remember { mutableIntStateOf(12 * 60 + 30) }
    LaunchedEffect(Unit) {
        while (countdownSecs > 0) {
            delay(1000)
            countdownSecs--
        }
    }
    val countdownText = remember(countdownSecs) {
        val m = countdownSecs / 60
        val s = countdownSecs % 60
        String.format("%02d:%02d", m, s)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
    ) {
        // Sticky Header: Avatar, Greeting, COIN CHIP ONLY, Notification Bell (NO wallet chip)
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
                            color = TextSecondary,
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
                    // Coin Chip (Tap opens Rewards)
                    Surface(
                        modifier = Modifier.clickable { onOpenRewards() },
                        color = ElevatedSurface,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldCoins.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.MonetizationOn, contentDescription = "Coins", tint = GoldCoins, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "$coins", fontSize = 12.sp, fontWeight = FontWeight.Black, color = GoldCoins)
                        }
                    }

                    // Notification Bell
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(ElevatedSurface)
                            .border(1.dp, CardBorder, CircleShape)
                            .clickable { onOpenNotifications() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White, modifier = Modifier.size(18.dp))
                        if (unreadCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .align(Alignment.TopEnd)
                                    .clip(CircleShape)
                                    .background(LiveRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "$unreadCount", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Sponsor Banner Carousel with "Join Free" CTA & Sponsor Label
        item {
            val currentBanner = carouselBanners[activeBannerIdx]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, CardBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = ElevatedSurface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF17113D), Color(0xFF1B1B2B), Color(0xFF0A0A0F))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = NeonLime,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = currentBanner.third,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            // Dots
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                carouselBanners.indices.forEach { idx ->
                                    Box(
                                        modifier = Modifier
                                            .size(if (idx == activeBannerIdx) 14.dp else 6.dp, 6.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(if (idx == activeBannerIdx) NeonLime else Color.White.copy(alpha = 0.3f))
                                    )
                                }
                            }
                        }

                        Column {
                            Text(
                                text = currentBanner.first,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = currentBanner.second,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ElectricViolet
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "• Sponsor-funded prize",
                                fontSize = 10.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.Bold
                            )

                            Button(
                                onClick = { onSelectModeFilter("Upcoming") },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                                shape = RoundedCornerShape(14.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("Join Free", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Black)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }
        }

        // Quick Actions Row (4 items: Streak, Daily Spin, My Squad, Ranks)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Streak
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onDailyCheckIn() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (hasCheckedInToday) CardBorder else NeonLime.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = NeonLime, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Streak", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(if (hasCheckedInToday) "Claimed" else "+50 Coins", fontSize = 9.sp, fontWeight = FontWeight.Black, color = NeonLime)
                    }
                }

                // Daily Spin
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenSpinWheel() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = GoldCoins, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Daily Spin", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Free Win", fontSize = 9.sp, fontWeight = FontWeight.Black, color = GoldCoins)
                    }
                }

                // Squad
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSelectModeFilter("Squad") },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Group, contentDescription = null, tint = ElectricViolet, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("My Squad", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("4v4 Ready", fontSize = 9.sp, fontWeight = FontWeight.Black, color = ElectricViolet)
                    }
                }

                // Ranks
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenLeaderboard() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ElevatedSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = NeonLime, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Ranks", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Top Players", fontSize = 9.sp, fontWeight = FontWeight.Black, color = NeonLime)
                    }
                }
            }
        }

        // Pinned Next Match Countdown Card (If registered)
        if (myJoinedMatch != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenMatchDetail(myJoinedMatch) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(2.dp, NeonLime)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(NeonLime)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("NEXT MATCH KICK-OFF", fontSize = 10.sp, fontWeight = FontWeight.Black, color = NeonLime)
                            }

                            Surface(
                                color = BgDark,
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Schedule, contentDescription = null, tint = NeonLime, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Room reveals in $countdownText", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeonLime)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(myJoinedMatch.title, fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("${myJoinedMatch.mode} • ${myJoinedMatch.map} • Sponsor: ${myJoinedMatch.sponsor}", fontSize = 11.sp, color = TextSecondary)
                            }

                            Surface(
                                color = NeonLime.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonLime.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("View Room", fontSize = 11.sp, fontWeight = FontWeight.Black, color = NeonLime)
                                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NeonLime, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Mode Filter Chips (With end padding so last chip is never clipped)
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(end = 24.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        modifier = Modifier.clickable {
                            selectedCategory = cat
                            onSelectModeFilter(cat)
                        },
                        color = if (isSelected) NeonLime else CardSurface,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NeonLime else CardBorder)
                    ) {
                        Text(
                            text = cat,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            color = if (isSelected) Color.Black else TextSecondary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Section: LIVE NOW
        if (liveMatches.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(LiveRed)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("LIVE NOW", fontSize = 12.sp, fontWeight = FontWeight.Black, color = LiveRed)
                    }
                    Text("Watch Lobby", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                }
            }

            items(liveMatches) { match ->
                MatchCard(match = match, onClick = { onOpenMatchDetail(match) })
            }
        }

        // Section: STARTING SOON
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = NeonLime, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("STARTING SOON", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                }
                Text("See all", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonLime, modifier = Modifier.clickable { onSelectModeFilter("Upcoming") })
            }
        }

        items(upcomingMatches.take(3)) { match ->
            MatchCard(match = match, onClick = { onOpenMatchDetail(match) })
        }

        // Referral Card: Invite Friends (Coins only, no cash!)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(listOf(Color(0xFF17113D), ElevatedSurface))
                        )
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("INVITE FRIENDS", fontSize = 10.sp, fontWeight = FontWeight.Black, color = NeonLime)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Both of you get 100 bonus coins", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Code: ${profile.referralCode}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldCoins)
                    }

                    Button(
                        onClick = { onOpenRewards() },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Share", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
        }

        // Fair Play Guarantee
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = BgDark,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = NeonLime, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Fair Play & Sponsor Guarantee", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Free entry. Prizes are sponsor-funded. Automated telemetry anti-cheat active across all custom lobbies.",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MatchCard(
    match: EsportsMatch,
    onClick: () -> Unit
) {
    val remainingSlots = match.maxSlots - match.joinedSlots
    val isUrgent = remainingSlots in 1..4
    val slotsPercent = (match.joinedSlots.toFloat() / match.maxSlots).coerceIn(0f, 1f)

    val mapAccent = when (match.map) {
        "Bermuda" -> NeonLime
        "Purgatory" -> LiveRed
        "Kalahari" -> GoldCoins
        else -> ElectricViolet
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, CardBorder, RoundedCornerShape(20.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Left map accent bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
                    .background(mapAccent)
            )

            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = NeonLime,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "FREE ENTRY",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Surface(
                                color = ElevatedSurface,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                            ) {
                                Text(
                                    text = match.mode,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Surface(
                                color = ElevatedSurface,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                            ) {
                                Text(
                                    text = match.map,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            if (match.status == "Live") {
                                Surface(
                                    color = LiveRed,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "LIVE",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(match.title, fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("Sponsored by ${match.sponsor}", fontSize = 10.sp, color = ElectricViolet, fontWeight = FontWeight.Bold)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "₹${match.prizePool}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text("Sponsor Pool", fontSize = 9.sp, color = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 3 Metric Chips: Free Entry | +10 XP per kill | Kick-off
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ElevatedSurface)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Entry", fontSize = 8.sp, color = TextSecondary)
                        Text("FREE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = NeonLime)
                    }
                    Column {
                        Text("Elimination", fontSize = 8.sp, color = TextSecondary)
                        Text("+10 XP", fontSize = 11.sp, fontWeight = FontWeight.Black, color = ElectricViolet)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Kick-Off", fontSize = 8.sp, color = TextSecondary)
                        Text(match.startTimeText, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Slots progress & Join CTA
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .width(90.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(BgDark)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(slotsPercent)
                                    .fillMaxHeight()
                                    .background(if (isUrgent) LiveRed else NeonLime)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        if (isUrgent) {
                            Text("$remainingSlots slots left!", fontSize = 10.sp, fontWeight = FontWeight.Black, color = LiveRed)
                        } else {
                            Text("${match.joinedSlots}/${match.maxSlots} Slots", fontSize = 10.sp, color = TextSecondary)
                        }
                    }

                    Surface(
                        color = if (match.isJoined) NeonLime.copy(alpha = 0.15f) else NeonLime,
                        shape = RoundedCornerShape(12.dp),
                        border = if (match.isJoined) androidx.compose.foundation.BorderStroke(1.dp, NeonLime.copy(alpha = 0.4f)) else null
                    ) {
                        Text(
                            text = if (match.isJoined) "Registered" else "Join Free",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = if (match.isJoined) NeonLime else Color.Black,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }
    }
}
