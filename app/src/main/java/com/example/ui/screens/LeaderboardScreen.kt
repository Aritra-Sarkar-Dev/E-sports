package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.LeaderboardEntry
import com.example.core.model.LeaderboardTab
import com.example.ui.FFArenaViewModel
import com.example.ui.theme.AcidLime
import com.example.ui.theme.BronzePodium
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardShape
import com.example.ui.theme.CardSurface
import com.example.ui.theme.DeepPurpleEnd
import com.example.ui.theme.DeepPurpleStart
import com.example.ui.theme.GoldPodium
import com.example.ui.theme.SecondaryViolet
import com.example.ui.theme.SilverPodium
import com.example.ui.theme.SoraFontFamily
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun LeaderboardScreen(
    viewModel: FFArenaViewModel,
    modifier: Modifier = Modifier
) {
    val activeTab by viewModel.leaderboardTab.collectAsState()
    val entries = viewModel.getLeaderboardEntries()

    val topThree = entries.take(3)
    val remainingRanked = entries.drop(3)
    val youEntry = entries.find { it.isCurrentUser }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("leaderboard_screen")
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "ESPORTS STANDINGS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AcidLime,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Hall of Fame",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
            )
        }

        // Tabs: Weekly / Season / All-time
        TabRow(
            selectedTabIndex = activeTab.ordinal,
            containerColor = Color.Transparent,
            divider = { Divider(color = CardBorder, thickness = 1.dp) },
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[activeTab.ordinal]),
                    color = AcidLime,
                    height = 3.dp
                )
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            LeaderboardTab.values().forEach { tab ->
                val tabTitle = when (tab) {
                    LeaderboardTab.WEEKLY -> "WEEKLY"
                    LeaderboardTab.SEASON -> "SEASON"
                    LeaderboardTab.ALL_TIME -> "ALL-TIME"
                }
                Tab(
                    selected = activeTab == tab,
                    onClick = { viewModel.setLeaderboardTab(tab) },
                    text = {
                        Text(
                            text = tabTitle,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (activeTab == tab) AcidLime else TextMuted
                            )
                        )
                    },
                    modifier = Modifier.testTag("leaderboard_tab_${tab.name}")
                )
            }
        }

        // Main List with Top 3 Podium and Ranked List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top 3 Podium Widget
            if (topThree.size >= 3) {
                item {
                    TopThreePodium(
                        first = topThree[0],
                        second = topThree[1],
                        third = topThree[2]
                    )
                }
            }

            item {
                Text(
                    text = "RANKED CONTENDERS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    ),
                    modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                )
            }

            // Ranked List
            items(remainingRanked, key = { it.inGameName + it.rank }) { entry ->
                LeaderboardRow(entry = entry)
            }
        }

        // Pinned Highlighted "You" Row at bottom!
        if (youEntry != null) {
            Card(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B2B)),
                border = BorderStroke(1.5.dp, AcidLime),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("leaderboard_you_row")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(AcidLime, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "#${youEntry.rank}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF0A0A0F)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = youEntry.inGameName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextPrimary
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .background(AcidLime.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "YOU",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = AcidLime,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }
                            Text(
                                text = "${youEntry.kills} Kills • ${youEntry.wins} Wins (${youEntry.winRate} Win Rate)",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${youEntry.points}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = AcidLime
                            )
                        )
                        Text(
                            text = "PTS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted,
                                fontSize = 9.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

// Top 3 Podium Component: 2nd (Left), 1st (Center, highest), 3rd (Right)
@Composable
fun TopThreePodium(
    first: LeaderboardEntry,
    second: LeaderboardEntry,
    third: LeaderboardEntry
) {
    Card(
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.dp, CardBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                // 2nd Place (Silver)
                PodiumColumn(
                    entry = second,
                    rank = 2,
                    podiumColor = SilverPodium,
                    pillarHeight = 90.dp
                )

                // 1st Place (Gold, Tallest)
                PodiumColumn(
                    entry = first,
                    rank = 1,
                    podiumColor = GoldPodium,
                    pillarHeight = 120.dp,
                    isChampion = true
                )

                // 3rd Place (Bronze)
                PodiumColumn(
                    entry = third,
                    rank = 3,
                    podiumColor = BronzePodium,
                    pillarHeight = 75.dp
                )
            }
        }
    }
}

@Composable
fun PodiumColumn(
    entry: LeaderboardEntry,
    rank: Int,
    podiumColor: Color,
    pillarHeight: androidx.compose.ui.unit.Dp,
    isChampion: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(96.dp)
    ) {
        if (isChampion) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = "Champion trophy",
                tint = GoldPodium,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
        }

        // Avatar with podium badge
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(if (isChampion) 54.dp else 46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1A1A28))
                    .border(2.dp, podiumColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ElectricBolt,
                    contentDescription = null,
                    tint = podiumColor,
                    modifier = Modifier.size(if (isChampion) 28.dp else 22.dp)
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 6.dp)
                    .size(18.dp)
                    .background(podiumColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rank.toString(),
                    style = TextStyle(
                        fontFamily = SoraFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp,
                        color = Color.Black
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = entry.inGameName,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            ),
            maxLines = 1,
            textAlign = TextAlign.Center
        )

        Text(
            text = "${entry.points} pts",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                color = AcidLime
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Podium Pedestal
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(pillarHeight)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            podiumColor.copy(alpha = 0.35f),
                            Color(0xFF14141F)
                        )
                    )
                )
                .border(
                    1.dp,
                    podiumColor.copy(alpha = 0.5f),
                    RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "${entry.kills} KILLS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun LeaderboardRow(entry: LeaderboardEntry) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (entry.isCurrentUser) Color(0xFF1B1B2B) else CardSurface
        ),
        border = BorderStroke(1.dp, if (entry.isCurrentUser) AcidLime else CardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "#${entry.rank}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (entry.isCurrentUser) AcidLime else TextMuted
                    ),
                    modifier = Modifier.width(32.dp)
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E1E2C)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ElectricBolt,
                        contentDescription = null,
                        tint = if (entry.isCurrentUser) AcidLime else SecondaryViolet,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = entry.inGameName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "${entry.kills} kills • ${entry.wins} wins (${entry.winRate})",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${entry.points}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = AcidLime
                    )
                )
                Text(
                    text = "PTS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        color = TextMuted
                    )
                )
            }
        }
    }
}
