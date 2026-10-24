package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.model.EsportsMatch
import com.example.ui.theme.*

@Composable
fun MatchesScreen(
    matches: List<EsportsMatch>,
    onOpenMatchDetail: (EsportsMatch) -> Unit,
    onJoinMatch: (EsportsMatch) -> Unit,
    onReportMatch: (EsportsMatch) -> Unit
) {
    var selectedTab by remember { mutableStateOf("Upcoming") }
    val tabs = listOf("Upcoming", "Live", "Completed", "My Matches")

    var showFilters by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedMode by remember { mutableStateOf("All") }
    var selectedMap by remember { mutableStateOf("All") }
    var selectedSort by remember { mutableStateOf("Default") }

    val modes = listOf("All", "Solo", "Duo", "Squad")
    val maps = listOf("All", "Bermuda", "Purgatory", "Kalahari", "Alpine")
    val sortOptions = listOf("Default", "Fee: Low to High", "Prize: High to Low")

    val filteredMatches = remember(matches, selectedTab, searchQuery, selectedMode, selectedMap, selectedSort) {
        var list = when (selectedTab) {
            "Upcoming" -> matches.filter { it.status == "Upcoming" }
            "Live" -> matches.filter { it.status == "Live" }
            "Completed" -> matches.filter { it.status == "Completed" }
            "My Matches" -> matches.filter { it.isJoined }
            else -> matches
        }

        if (searchQuery.isNotBlank()) {
            list = list.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.map.contains(searchQuery, ignoreCase = true) ||
                it.mode.contains(searchQuery, ignoreCase = true)
            }
        }

        if (selectedMode != "All") {
            list = list.filter { it.mode.equals(selectedMode, ignoreCase = true) }
        }

        if (selectedMap != "All") {
            list = list.filter { it.map.equals(selectedMap, ignoreCase = true) }
        }

        when (selectedSort) {
            "Fee: Low to High" -> list.sortedBy { it.entryFee }
            "Prize: High to Low" -> list.sortedByDescending { it.prizePool }
            else -> list
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TOURNAMENTS",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Official Free Fire Custom Rooms & Cash Cups",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                // Filter toggle button
                IconButton(
                    onClick = { showFilters = !showFilters },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (showFilters) NeonLime.copy(alpha = 0.2f) else CardSurface)
                        .border(
                            1.dp,
                            if (showFilters) NeonLime else CardBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .testTag("filter_toggle_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Toggle Filters",
                        tint = if (showFilters) NeonLime else TextPrimary
                    )
                }
            }
        }

        // Sub Tabs
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardSurface)
                    .padding(4.dp)
            ) {
                tabs.forEach { tab ->
                    val isActive = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isActive) NeonLime else Color.Transparent)
                            .clickable { selectedTab = tab }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            fontSize = 13.sp,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                            color = if (isActive) Color.Black else TextMuted
                        )
                    }
                }
            }
        }

        // Filter Tray
        item {
            AnimatedVisibility(visible = showFilters) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardSurface)
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Search bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_tournaments_input"),
                        placeholder = { Text("Search by name, map, mode...", color = TextMuted, fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = TextMuted)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted)
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonLime,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = NeonLime
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Map Filter Row
                    Text("MAP SELECTION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, letterSpacing = 0.5.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(maps) { mapItem ->
                            val isSel = selectedMap == mapItem
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) FireOrange.copy(alpha = 0.2f) else ChipBg)
                                    .border(1.dp, if (isSel) FireOrange else CardBorder, RoundedCornerShape(8.dp))
                                    .clickable { selectedMap = mapItem }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = mapItem,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) FireOrange else TextPrimary
                                )
                            }
                        }
                    }

                    // Mode Filter Row
                    Text("MODE (SQUAD / DUO / SOLO)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, letterSpacing = 0.5.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(modes) { modeItem ->
                            val isSel = selectedMode == modeItem
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) ElectricViolet.copy(alpha = 0.25f) else ChipBg)
                                    .border(1.dp, if (isSel) ElectricViolet else CardBorder, RoundedCornerShape(8.dp))
                                    .clickable { selectedMode = modeItem }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = modeItem,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) ElectricViolet else TextPrimary
                                )
                            }
                        }
                    }

                    // Sort Order Row
                    Text("SORT ORDER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, letterSpacing = 0.5.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(sortOptions) { sortOption ->
                            val isSel = selectedSort == sortOption
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) NeonLime.copy(alpha = 0.2f) else ChipBg)
                                    .border(1.dp, if (isSel) NeonLime else CardBorder, RoundedCornerShape(8.dp))
                                    .clickable { selectedSort = sortOption }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = sortOption,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) NeonLime else TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Filter Indicators Summary
        if (selectedMode != "All" || selectedMap != "All" || selectedSort != "Default" || searchQuery.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Showing ${filteredMatches.size} tournaments",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "Reset All",
                        fontSize = 12.sp,
                        color = NeonLime,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            selectedMode = "All"
                            selectedMap = "All"
                            selectedSort = "Default"
                            searchQuery = ""
                        }
                    )
                }
            }
        }

        // Empty State
        if (filteredMatches.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsEsports,
                            contentDescription = "No matches",
                            tint = TextMuted,
                            modifier = Modifier.size(54.dp)
                        )
                        Text(
                            text = "No Tournaments Found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Try adjusting your filters or check back later for new registrations.",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }

        // Match Cards
        items(filteredMatches, key = { it.id }) { match ->
            MatchCardItem(
                match = match,
                onCardClick = { onOpenMatchDetail(match) },
                onJoinClick = { onJoinMatch(match) },
                onReportClick = { onReportMatch(match) }
            )
        }
    }
}

@Composable
fun MatchCardItem(
    match: EsportsMatch,
    onCardClick: () -> Unit,
    onJoinClick: () -> Unit,
    onReportClick: () -> Unit
) {
    val isFull = match.joinedSlots >= match.maxSlots
    val slotProgress = if (match.maxSlots > 0) match.joinedSlots.toFloat() / match.maxSlots.toFloat() else 0f

    val mapGradient = when (match.map) {
        "Bermuda" -> Brush.verticalGradient(listOf(Color(0xFF1E3A8A).copy(alpha = 0.35f), CardSurface))
        "Purgatory" -> Brush.verticalGradient(listOf(Color(0xFF7C2D12).copy(alpha = 0.35f), CardSurface))
        "Kalahari" -> Brush.verticalGradient(listOf(Color(0xFF78350F).copy(alpha = 0.35f), CardSurface))
        else -> Brush.verticalGradient(listOf(Color(0xFF312E81).copy(alpha = 0.35f), CardSurface))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, CardBorder, RoundedCornerShape(20.dp))
            .clickable { onCardClick() }
            .testTag("match_card_${match.id}"),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(mapGradient)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: Map & Mode Badges + Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Map Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ChipBg)
                            .border(1.dp, CardBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = match.map.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = FireOrange
                        )
                    }

                    // Mode Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ChipBg)
                            .border(1.dp, CardBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = match.mode.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricViolet
                        )
                    }

                    // Division
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ChipBg)
                            .border(1.dp, CardBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = match.division.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextMuted
                        )
                    }
                }

                // Status or Live indicator
                if (match.status == "Live") {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(LiveRed.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(LiveRed)
                        )
                        Text("LIVE NOW", fontSize = 10.sp, fontWeight = FontWeight.Black, color = LiveRed)
                    }
                } else if (match.status == "Completed") {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ChipBg)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("FINISHED", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
                        Text(match.startTimeText, fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.Medium)
                    }
                }
            }

            // Tournament Title & Sponsor
            Column {
                Text(
                    text = match.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                Text(
                    text = "Presented by ${match.sponsor}",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }

            // Stats row: Prize pool, Per kill, Entry fee
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ChipBg.copy(alpha = 0.7f))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Prize Pool
                Column {
                    Text("PRIZE POOL", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    Text("₹${match.prizePool}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = GoldPrize)
                }

                // Per Kill
                Column {
                    Text("PER KILL", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    Text("₹${match.perKillPrize}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = FireOrange)
                }

                // Entry Fee
                Column(horizontalAlignment = Alignment.End) {
                    Text("ENTRY FEE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    if (match.entryFee == 0) {
                        Text("FREE", fontSize = 14.sp, fontWeight = FontWeight.Black, color = NeonLime)
                    } else {
                        Text("₹${match.entryFee}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                    }
                }
            }

            // Slot Progress Bar
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Slots Filled", fontSize = 11.sp, color = TextMuted)
                    Text(
                        "${match.joinedSlots}/${match.maxSlots} joined",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isFull) LiveRed else TextPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(ChipBg)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = slotProgress.coerceIn(0f, 1f))
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(NeonLime, if (isFull) LiveRed else FireOrange)
                                )
                            )
                    )
                }
            }

            // If registered, show Room ID / Credentials or Result
            if (match.isJoined) {
                if (match.status == "Completed") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(NeonLime.copy(alpha = 0.15f))
                            .border(1.dp, NeonLime.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("MATCH RESULT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeonLime)
                            Text("Rank #${match.myRank ?: '-'} • ${match.myKills ?: 0} Kills", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Text("+₹${match.myWinnings ?: 0}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = NeonLime)
                    }
                } else if (match.isRoomRevealed && match.roomId != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ElectricViolet.copy(alpha = 0.15f))
                            .border(1.dp, ElectricViolet.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("CUSTOM ROOM DETAILS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ElectricViolet)
                            Text("Room ID: ${match.roomId}  |  Pass: ${match.roomPass}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = ElectricViolet, modifier = Modifier.size(18.dp))
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ChipBg)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓ Registered • Room credentials reveal 10m before start",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = NeonLime
                        )
                    }
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onCardClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("match_details_button_${match.id}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextPrimary
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.horizontalGradient(listOf(CardBorder, CardBorder))
                    )
                ) {
                    Text("Rules & Info", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                if (match.status == "Upcoming") {
                    if (match.isJoined) {
                        Button(
                            onClick = onCardClick,
                            modifier = Modifier
                                .weight(1.2f)
                                .height(44.dp)
                                .testTag("match_view_ticket_${match.id}"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ChipBg,
                                contentColor = NeonLime
                            )
                        ) {
                            Text("Joined ✓", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    } else if (isFull) {
                        Button(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier
                                .weight(1.2f)
                                .height(44.dp)
                                .testTag("match_full_button_${match.id}"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = ChipBg,
                                disabledContentColor = TextMuted
                            )
                        ) {
                            Text("Slot Full", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = onJoinClick,
                            modifier = Modifier
                                .weight(1.2f)
                                .height(44.dp)
                                .testTag("match_join_button_${match.id}"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonLime,
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                if (match.entryFee == 0) "Join Free" else "Join ₹${match.entryFee}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                } else if (match.status == "Completed") {
                    OutlinedButton(
                        onClick = onReportClick,
                        modifier = Modifier
                            .weight(1.2f)
                            .height(44.dp)
                            .testTag("match_report_button_${match.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextMuted
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.horizontalGradient(listOf(CardBorder, CardBorder))
                        )
                    ) {
                        Icon(Icons.Default.Flag, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Dispute / Help", fontSize = 12.sp)
                    }
                } else {
                    Button(
                        onClick = onCardClick,
                        modifier = Modifier
                            .weight(1.2f)
                            .height(44.dp)
                            .testTag("match_spectate_button_${match.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LiveRed,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Spectate Live", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
