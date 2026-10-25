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

    var selectedMap by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val myMatchesCount = remember(matches) { matches.count { it.isJoined } }

    val filteredMatches = remember(matches, selectedTab, selectedMap, searchQuery) {
        matches.filter { match ->
            val matchesTab = when (selectedTab) {
                "My Matches" -> match.isJoined
                else -> match.status == selectedTab
            }
            val matchesMap = selectedMap == "All" || match.map == selectedMap
            val matchesSearch = searchQuery.isBlank() ||
                match.title.contains(searchQuery, ignoreCase = true) ||
                match.sponsor.contains(searchQuery, ignoreCase = true) ||
                match.map.contains(searchQuery, ignoreCase = true)

            matchesTab && matchesMap && matchesSearch
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
        // Header with Title & Exact Subtitle
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = NeonLime, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tournament Lobby",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Official Free Fire Custom Rooms & Sponsor Cups",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        // Horizontal Scrolling Tabs without clipping
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardSurface)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(tabs) { tab ->
                    val isSelected = selectedTab == tab
                    Surface(
                        modifier = Modifier
                            .clickable { selectedTab = tab },
                        color = if (isSelected) NeonLime else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tab,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                color = if (isSelected) Color.Black else TextSecondary
                            )
                            if (tab == "My Matches" && myMatchesCount > 0) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = if (isSelected) Color.Black else NeonLime.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "$myMatchesCount",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (isSelected) NeonLime else NeonLime,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Search Bar & Map Filter row
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search tournament, sponsor, or map...", fontSize = 12.sp, color = TextSecondary) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardSurface,
                    unfocusedContainerColor = CardSurface,
                    focusedBorderColor = NeonLime,
                    unfocusedBorderColor = CardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
        }

        // Map Filter Chips
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(end = 24.dp)
            ) {
                items(listOf("All", "Bermuda", "Purgatory", "Kalahari", "Alpine")) { mapName ->
                    val isSelected = selectedMap == mapName
                    Surface(
                        modifier = Modifier.clickable { selectedMap = mapName },
                        color = if (isSelected) NeonLime else CardSurface,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NeonLime else CardBorder)
                    ) {
                        Text(
                            text = mapName,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            color = if (isSelected) Color.Black else TextSecondary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Matches list
        if (filteredMatches.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = CardBorder, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("No tournaments found", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Try selecting another map or game mode filter", fontSize = 12.sp, color = TextSecondary)
                    }
                }
            }
        } else {
            items(filteredMatches) { match ->
                MatchCard(
                    match = match,
                    onClick = { onOpenMatchDetail(match) }
                )
            }
        }
    }
}
