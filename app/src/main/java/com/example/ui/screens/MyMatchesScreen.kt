package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.core.components.DivisionBadge
import com.example.core.components.PrimaryButton
import com.example.core.config.AppConfig
import com.example.core.model.MatchRecord
import com.example.core.model.TournamentStatus
import com.example.ui.FFArenaViewModel
import com.example.ui.MatchesSubTab
import com.example.ui.theme.AcidLime
import com.example.ui.theme.BronzePodium
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardShape
import com.example.ui.theme.CardSurface
import com.example.ui.theme.GoldPodium
import com.example.ui.theme.SecondaryViolet
import com.example.ui.theme.SilverPodium
import com.example.ui.theme.SoraFontFamily
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun MyMatchesScreen(
    viewModel: FFArenaViewModel,
    modifier: Modifier = Modifier
) {
    val matchesTab by viewModel.matchesSubTab.collectAsState()
    val upcomingMatches by viewModel.upcomingMatches.collectAsState()
    val completedMatches by viewModel.completedMatches.collectAsState()
    val selectedProof by viewModel.selectedProofMatch.collectAsState()

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("my_matches_screen")
    ) {
        // Screen Title
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "ESPORTS DIARY",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AcidLime,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "My Matches",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
            )
        }

        // Tabs: Upcoming / Completed
        TabRow(
            selectedTabIndex = if (matchesTab == MatchesSubTab.UPCOMING) 0 else 1,
            containerColor = Color.Transparent,
            divider = { Divider(color = CardBorder, thickness = 1.dp) },
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(
                        tabPositions[if (matchesTab == MatchesSubTab.UPCOMING) 0 else 1]
                    ),
                    color = AcidLime,
                    height = 3.dp
                )
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Tab(
                selected = matchesTab == MatchesSubTab.UPCOMING,
                onClick = { viewModel.setMatchesSubTab(MatchesSubTab.UPCOMING) },
                text = {
                    Text(
                        text = "UPCOMING (${upcomingMatches.size})",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (matchesTab == MatchesSubTab.UPCOMING) AcidLime else TextMuted
                        )
                    )
                },
                modifier = Modifier.testTag("tab_upcoming_matches")
            )
            Tab(
                selected = matchesTab == MatchesSubTab.COMPLETED,
                onClick = { viewModel.setMatchesSubTab(MatchesSubTab.COMPLETED) },
                text = {
                    Text(
                        text = "COMPLETED (${completedMatches.size})",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (matchesTab == MatchesSubTab.COMPLETED) AcidLime else TextMuted
                        )
                    )
                },
                modifier = Modifier.testTag("tab_completed_matches")
            )
        }

        // List Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentPadding = PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (matchesTab == MatchesSubTab.UPCOMING) {
                if (upcomingMatches.isEmpty()) {
                    item {
                        EmptyMatchesCard(
                            message = "No upcoming matches booked.\nBrowse tournaments in Compete to join!"
                        )
                    }
                } else {
                    items(upcomingMatches, key = { it.id }) { match ->
                        UpcomingMatchCard(
                            match = match,
                            onCopyRoom = {
                                copyToClipboard(context, "FF Arena Room", "${match.roomId} / ${match.roomPassword}")
                                viewModel.showSnackbar("Copied Room ID & Password to clipboard!")
                            }
                        )
                    }
                }
            } else {
                if (completedMatches.isEmpty()) {
                    item {
                        EmptyMatchesCard(message = "No completed matches yet. Complete your first match to view certified results!")
                    }
                } else {
                    items(completedMatches, key = { it.id }) { match ->
                        CompletedMatchCard(
                            match = match,
                            onViewProofClick = { viewModel.openProof(match) }
                        )
                    }
                }
            }
        }
    }

    // Proof Modal Dialog
    if (selectedProof != null) {
        ProofDetailsDialog(
            match = selectedProof!!,
            onDismiss = { viewModel.closeProof() }
        )
    }
}

// Upcoming Match Card with locked/unlocked room credentials logic
@Composable
fun UpcomingMatchCard(
    match: MatchRecord,
    onCopyRoom: () -> Unit
) {
    // Check if within 10 minutes or user triggered simulation
    val timeDiff = match.matchTimeMillis - System.currentTimeMillis()
    var forceUnlocked by remember { mutableStateOf(timeDiff in 0..(10 * 60 * 1000)) }
    val isRoomUnlocked = forceUnlocked || timeDiff <= (10 * 60 * 1000)

    Card(
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.2.dp, if (isRoomUnlocked) AcidLime.copy(alpha = 0.7f) else CardBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("upcoming_match_card_${match.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Division badge & Mode + Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DivisionBadge(division = match.division)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = match.mode.label.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = SecondaryViolet
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = if (isRoomUnlocked) AcidLime else TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = match.matchTimeFormatted,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isRoomUnlocked) AcidLime else TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Tournament Title
            Text(
                text = match.tournamentTitle,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Room Credentials Container
            if (isRoomUnlocked) {
                // UNLOCKED STATE
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(AcidLime.copy(alpha = 0.12f))
                        .border(1.5.dp, AcidLime, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LockOpen,
                                    contentDescription = null,
                                    tint = AcidLime,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ROOM CREDENTIALS UNLOCKED",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = AcidLime,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Room ID: ${match.roomId}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "Password: ${match.roomPassword}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextMuted
                                )
                            )
                        }

                        PrimaryButton(
                            text = "COPY",
                            onClick = onCopyRoom,
                            leadingIcon = Icons.Default.ContentCopy,
                            modifier = Modifier.height(42.dp),
                            testTag = "copy_room_btn"
                        )
                    }
                }
            } else {
                // LOCKED STATE (until 10 mins before start)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF0F0F17))
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(CardBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Room locked",
                                    tint = TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "ROOM ID & PASSWORD LOCKED",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                )
                                Text(
                                    text = "Unlocks 10 mins before match start",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextMuted
                                    )
                                )
                            }
                        }

                        // Simulation button for test review
                        TextButton(
                            onClick = { forceUnlocked = true },
                            modifier = Modifier.testTag("simulate_unlock_btn")
                        ) {
                            Text(
                                text = "Unlock Now",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SecondaryViolet,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

// Completed Match Card: shows rank, kills, prize won, and a "View proof" link
@Composable
fun CompletedMatchCard(
    match: MatchRecord,
    onViewProofClick: () -> Unit
) {
    Card(
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.dp, CardBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("completed_match_card_${match.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Mode & Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DivisionBadge(division = match.division)
                Text(
                    text = match.matchTimeFormatted,
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tournament Title
            Text(
                text = match.tournamentTitle,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Stats Row: Rank (Booyah/Podium), Kills, Prize Won
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1B1B2A), RoundedCornerShape(14.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rank
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "RANK",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            color = TextMuted
                        )
                    )
                    val rankText = when (match.rank) {
                        1 -> "🏆 #1 Booyah"
                        2 -> "🥈 #2"
                        3 -> "🥉 #3"
                        else -> "#${match.rank ?: 0}"
                    }
                    val rankColor = when (match.rank) {
                        1 -> GoldPodium
                        2 -> SilverPodium
                        3 -> BronzePodium
                        else -> TextPrimary
                    }
                    Text(
                        text = rankText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = rankColor
                        )
                    )
                }

                // Kills
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ELIMINATIONS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            color = TextMuted
                        )
                    )
                    Text(
                        text = "${match.kills ?: 0} Kills",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    )
                }

                // Prize
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "SPONSOR PRIZE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            color = TextMuted
                        )
                    )
                    Text(
                        text = if ((match.prizeWon ?: 0) > 0) {
                            "+${AppConfig.CURRENCY_SYMBOL}${match.prizeWon}"
                        } else {
                            "₹0"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if ((match.prizeWon ?: 0) > 0) AcidLime else TextMuted
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // View Proof link with referee audit tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = AcidLime,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Referee Certified",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AcidLime,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                Text(
                    text = "View proof →",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = SecondaryViolet,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable(onClick = onViewProofClick)
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .testTag("view_proof_btn_${match.id}")
                )
            }
        }
    }
}

// Dialog for Viewing Proof
@Composable
fun ProofDetailsDialog(
    match: MatchRecord,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = CardSurface,
            border = BorderStroke(1.5.dp, SecondaryViolet),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("proof_dialog")
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = AcidLime,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "OFFICIAL MATCH AUDIT",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Simulated Match Screenshot Certificate Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF0F0F17))
                        .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.SportsEsports,
                            contentDescription = null,
                            tint = SecondaryViolet,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "FREE FIRE MATCH SCREENSHOT LOG",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextMuted,
                                fontSize = 9.sp
                            )
                        )
                        Text(
                            text = "Scorecard Hash: #FF-${match.roomId}-VERIFIED",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = AcidLime,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = match.proofSummary,
                    style = MaterialTheme.typography.bodySmall.copy(
                        textAlign = TextAlign.Center,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "All placements and kill tallies are validated by our Fair Play server anti-cheat log before sponsor payouts are released.",
                    style = MaterialTheme.typography.labelSmall.copy(
                        textAlign = TextAlign.Center,
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                PrimaryButton(
                    text = "CLOSE PROOF",
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun EmptyMatchesCard(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.SportsEsports,
                contentDescription = null,
                tint = TextMuted.copy(alpha = 0.5f),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

private fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}
