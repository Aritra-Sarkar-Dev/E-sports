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
import com.example.ui.model.EsportsMatch
import com.example.ui.model.LeaderboardRow
import com.example.ui.model.MockData
import com.example.ui.theme.*

@Composable
fun MatchesScreen(
    matches: List<EsportsMatch>,
    onOpenMatchDetail: (EsportsMatch) -> Unit,
    onJoinMatch: (EsportsMatch) -> Unit,
    onReportMatch: (EsportsMatch) -> Unit,
    initialTab: String = "Upcoming"
) {
    var selectedSubTab by remember { mutableStateOf(initialTab) }
    val tabs = listOf("Upcoming", "Live", "Completed", "My Matches", "Leaderboard")

    var selectedModeFilter by remember { mutableStateOf("All Modes") }
    val modes = listOf("All Modes", "Squad", "Duo", "Solo")

    val filteredMatches = remember(matches, selectedSubTab, selectedModeFilter) {
        val byTab = when (selectedSubTab) {
            "Upcoming" -> matches.filter { it.status == "Upcoming" }
            "Live" -> matches.filter { it.status == "Live" }
            "Completed" -> matches.filter { it.status == "Completed" }
            "My Matches" -> matches.filter { it.isJoined }
            else -> emptyList()
        }
        if (selectedModeFilter == "All Modes") byTab else byTab.filter { it.mode == selectedModeFilter }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "TOURNAMENT ARENA",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "100% Free Entry • Sponsor-Funded Prizes Only",
                    fontSize = 12.sp,
                    color = NeonLime,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Sub Tabs: Upcoming, Live, Completed, My Matches, Leaderboard
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 2.dp, top = 2.dp, end = 24.dp, bottom = 2.dp)
            ) {
                items(tabs) { tab ->
                    val isSelected = selectedSubTab == tab
                    Surface(
                        modifier = Modifier.clickable { selectedSubTab = tab },
                        color = if (isSelected) NeonLime else ElevatedSurface,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NeonLime else CardBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (tab == "Live") {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) Color.Black else LiveRed)
                                )
                            }
                            Text(
                                text = tab,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                color = if (isSelected) Color.Black else TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Mode Filter (Only when not Leaderboard)
        if (selectedSubTab != "Leaderboard") {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    modes.forEach { mode ->
                        val isSelected = selectedModeFilter == mode
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedModeFilter = mode },
                            color = if (isSelected) NeonLime.copy(alpha = 0.15f) else CardSurface,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NeonLime else CardBorder)
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = mode,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                    color = if (isSelected) NeonLime else TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Leaderboard Tab View
        if (selectedSubTab == "Leaderboard") {
            item {
                LeaderboardSectionView()
            }
        } else {
            // Tournaments List
            if (filteredMatches.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.SportsEsports, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("No tournaments in this category", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Check other tabs or come back soon for new sponsor matches.", fontSize = 12.sp, color = TextSecondary)
                        }
                    }
                }
            } else {
                items(filteredMatches) { match ->
                    MatchCardItem(
                        match = match,
                        onOpenDetail = { onOpenMatchDetail(match) },
                        onJoin = { onJoinMatch(match) },
                        onReport = { onReportMatch(match) }
                    )
                }
            }
        }
    }
}

@Composable
fun MatchCardItem(
    match: EsportsMatch,
    onOpenDetail: () -> Unit,
    onJoin: () -> Unit,
    onReport: () -> Unit
) {
    val remainingSlots = match.maxSlots - match.joinedSlots
    val isUnderTenPercent = (remainingSlots.toFloat() / match.maxSlots) <= 0.10f
    val isNearStart = match.status == "Live" || match.startTimeText.contains("LIVE", ignoreCase = true) || (match.startTimeText.contains("min", ignoreCase = true) && !match.startTimeText.contains("Tomorrow"))
    val isUrgent = isUnderTenPercent || isNearStart

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
            .clickable { onOpenDetail() },
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Map Accent
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
                    .background(mapAccent)
            )

            Column(modifier = Modifier.padding(14.dp)) {
                // Top Tags & Prize
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
                                Text("FREE ENTRY", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Black, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                            Surface(
                                color = ElevatedSurface,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                            ) {
                                Text(match.mode, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                            Surface(
                                color = ElevatedSurface,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                            ) {
                                Text(match.map, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                            if (match.status == "Live") {
                                Surface(color = LiveRed, shape = RoundedCornerShape(8.dp)) {
                                    Text("LIVE", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(match.title, fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("Sponsor: ${match.sponsor}", fontSize = 11.sp, color = ElectricViolet, fontWeight = FontWeight.Bold)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("₹${match.prizePool}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("Sponsor-funded", fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 3 Metric Chips: Entry, Elimination, Kick-Off (Minimum 12-13px text)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ElevatedSurface)
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Entry", fontSize = 12.sp, color = TextSecondary)
                        Text("FREE", fontSize = 13.sp, fontWeight = FontWeight.Black, color = NeonLime)
                    }
                    Column {
                        Text("Elimination", fontSize = 12.sp, color = TextSecondary)
                        Text("+${match.perKillXp} XP", fontSize = 13.sp, fontWeight = FontWeight.Black, color = ElectricViolet)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Kick-Off", fontSize = 12.sp, color = TextSecondary)
                        Text(match.startTimeText, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom row: Slots or Completed status
                if (match.status == "Completed") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = ElevatedSurface,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("Sponsor prize: ₹${match.prizePool}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Surface(color = NeonLime.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                                    Text("Recorded", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeonLime, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                        }

                        TextButton(onClick = { onReport() }) {
                            Text("Dispute Match", color = LiveRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
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
                                Text("$remainingSlots slots left!", fontSize = 11.sp, fontWeight = FontWeight.Black, color = LiveRed)
                            } else {
                                Text("${match.joinedSlots}/${match.maxSlots} Slots", fontSize = 11.sp, color = TextSecondary)
                            }
                        }

                        Button(
                            onClick = {
                                if (match.isJoined) onOpenDetail() else onJoin()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (match.isJoined) NeonLime.copy(alpha = 0.15f) else NeonLime
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(
                                text = if (match.isJoined) "View Room" else "Join Free",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = if (match.isJoined) NeonLime else Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardSectionView() {
    var period by remember { mutableStateOf("Weekly") }
    val periods = listOf("Weekly", "Season", "All-time")

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Period Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            periods.forEach { p ->
                val isSelected = period == p
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { period = p },
                    color = if (isSelected) NeonLime else CardSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NeonLime else CardBorder)
                ) {
                    Box(modifier = Modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = p,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            color = if (isSelected) Color.Black else TextSecondary
                        )
                    }
                }
            }
        }

        // Top-3 Podium
        val top3 = MockData.leaderboardList.take(3)
        if (top3.size >= 3) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                // #2
                PodiumColumn(row = top3[1], height = 100.dp, color = Color(0xFFC0C0C0), medal = "🥈")
                // #1
                PodiumColumn(row = top3[0], height = 125.dp, color = GoldCoins, medal = "👑")
                // #3
                PodiumColumn(row = top3[2], height = 85.dp, color = Color(0xFFCD7F32), medal = "🥉")
            }
        }

        Text(
            text = "ARENA STANDINGS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = Color.White,
            letterSpacing = 1.sp
        )

        // Ranks 4 to 10
        MockData.leaderboardList.drop(3).forEach { row ->
            val isUser = row.ign == "NEON_SOUL" || row.name.contains("Aritra")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = if (isUser) NeonLime.copy(alpha = 0.12f) else CardSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isUser) NeonLime else CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "#${row.rank}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isUser) NeonLime else TextSecondary
                        )
                        Column {
                            Text(row.ign, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("${row.kills} Kills • ${row.wins} Booyahs", fontSize = 11.sp, color = TextSecondary)
                        }
                    }

                    Text(
                        text = "${row.points} pts",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isUser) NeonLime else Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun PodiumColumn(
    row: LeaderboardRow,
    height: androidx.compose.ui.unit.Dp,
    color: Color,
    medal: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(medal, fontSize = 22.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(row.ign, fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
        Text("${row.points} pts", fontSize = 10.sp, color = color, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .width(80.dp)
                .height(height)
                .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                .background(color.copy(alpha = 0.2f))
                .border(1.dp, color, RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("#${row.rank}", fontSize = 20.sp, fontWeight = FontWeight.Black, color = color)
        }
    }
}
