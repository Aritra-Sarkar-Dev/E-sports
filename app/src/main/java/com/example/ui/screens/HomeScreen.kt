package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.core.components.FFArenaChip
import com.example.core.components.PrimaryButton
import com.example.core.components.SectionTitle
import com.example.core.components.TournamentCard
import com.example.core.config.AppConfig
import com.example.core.model.GameMode
import com.example.core.model.Tournament
import com.example.core.model.User
import com.example.ui.BottomTab
import com.example.ui.FFArenaViewModel
import com.example.ui.theme.AcidLime
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardShape
import com.example.ui.theme.CardSurface
import com.example.ui.theme.DeepPurpleEnd
import com.example.ui.theme.DeepPurpleStart
import com.example.ui.theme.ErrorDanger
import com.example.ui.theme.HeroCardShape
import com.example.ui.theme.LivePulseRed
import com.example.ui.theme.SecondaryViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: FFArenaViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val allTournaments by viewModel.tournaments.collectAsState()
    val selectedFilter by viewModel.selectedFilterMode.collectAsState()

    val heroTournament = allTournaments.firstOrNull() ?: return
    val startingSoonList = allTournaments.drop(1)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_scroll"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // 1. Header: Greeting with player avatar & notification bell
        item {
            HomeTopGreetingHeader(
                user = currentUser,
                onAvatarClick = { viewModel.setBottomTab(BottomTab.PROFILE) },
                onNotificationClick = {
                    viewModel.showSnackbar("You're signed up for upcoming match room alerts!")
                }
            )
        }

        // 2. Hero Card for the next big tournament (live countdown, prize pool, entry, spots-filled progress bar, big lime Join button)
        item {
            HeroTournamentCard(
                tournament = heroTournament,
                onCardClick = { viewModel.openTournamentDetails(heroTournament) },
                onJoinClick = { viewModel.joinTournament(heroTournament.id) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // 3. Daily Streak Card
        item {
            DailyStreakCard(
                user = currentUser,
                onClaimClick = { viewModel.claimStreak() },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // 4. Filter Chips (All, Solo, Duo, Squad, Free)
        item {
            Column(modifier = Modifier.padding(top = 14.dp, bottom = 6.dp)) {
                Text(
                    text = "CATEGORIES",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.testTag("home_filter_chips_row")
                ) {
                    items(GameMode.values()) { mode ->
                        FFArenaChip(
                            label = mode.label,
                            isSelected = selectedFilter == mode,
                            onClick = { viewModel.setFilterMode(mode) }
                        )
                    }
                }
            }
        }

        // 5. Section: Starting Soon
        item {
            SectionTitle(
                title = "Starting Soon",
                actionText = "See All",
                onActionClick = { viewModel.setBottomTab(BottomTab.COMPETE) },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
        }

        // Starting soon tournament items
        val filteredList = when (selectedFilter) {
            GameMode.ALL -> startingSoonList
            GameMode.FREE -> startingSoonList.filter { it.entryFee == 0 }
            else -> startingSoonList.filter { it.mode == selectedFilter }
        }

        if (filteredList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tournaments in this category right now.\nCheck back in 15 minutes!",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            items(filteredList, key = { it.id }) { tournament ->
                TournamentCard(
                    tournament = tournament,
                    onCardClick = { viewModel.openTournamentDetails(tournament) },
                    onJoinClick = { viewModel.joinTournament(tournament.id) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun HomeTopGreetingHeader(
    user: User?,
    onAvatarClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onAvatarClick)
        ) {
            // Player avatar with level pill
            Box {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(CardSurface)
                        .border(1.5.dp, AcidLime, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ElectricBolt,
                        contentDescription = "Avatar",
                        tint = AcidLime,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(SecondaryViolet, RoundedCornerShape(6.dp))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "L${user?.level ?: 1}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 8.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "HELLO CHAMPION 👋",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user?.inGameName ?: "Pro Player",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified player",
                        tint = AcidLime,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Notification Bell
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(CardSurface)
                .border(1.dp, CardBorder, CircleShape)
                .clickable(onClick = onNotificationClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = TextPrimary,
                modifier = Modifier.size(20.dp)
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 10.dp)
                    .background(AcidLime, CircleShape)
            )
        }
    }
}

// Hero Card Component (28px radius, deep purple gradient, banner image, live countdown, spots progress, Join button)
@Composable
fun HeroTournamentCard(
    tournament: Tournament,
    onCardClick: () -> Unit,
    onJoinClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Live countdown simulation
    var remainingSeconds by remember { mutableLongStateOf(42 * 60L + 15L) }
    LaunchedEffect(Unit) {
        while (remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--
        }
    }
    val hours = remainingSeconds / 3600
    val minutes = (remainingSeconds % 3600) / 60
    val seconds = remainingSeconds % 60
    val countdownText = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Card(
        onClick = onCardClick,
        shape = HeroCardShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.5.dp, SecondaryViolet.copy(alpha = 0.6f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("hero_tournament_card")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DeepPurpleStart, DeepPurpleEnd, CardSurface)
                    )
                )
        ) {
            // Decorative Hero Banner
            Image(
                painter = painterResource(id = R.drawable.ff_hero_banner_1792570258756),
                contentDescription = "Tournament artwork",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )

            // Dark gradient overlay on image for readability
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                DeepPurpleStart.copy(alpha = 0.7f),
                                DeepPurpleEnd
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Top tag row: Live status + Countdown pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(LivePulseRed.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .border(1.dp, LivePulseRed, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FiberManualRecord,
                                contentDescription = null,
                                tint = LivePulseRed,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "NEXT BIG MATCH",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    // Live countdown pill
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF0A0A0F).copy(alpha = 0.75f), RoundedCornerShape(12.dp))
                            .border(1.dp, SecondaryViolet, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = AcidLime,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = countdownText,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AcidLime,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(55.dp))

                // Title
                Text(
                    text = tournament.title,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${tournament.subtitle} • Squad Pro",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFFD1C4E9),
                        fontWeight = FontWeight.Medium
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Progress Bar: spots filled
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Registration: ${tournament.joinedSlots}/${tournament.totalSlots} Slots",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "Filling Fast! Only ${tournament.spotsLeft} Left",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = AcidLime
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { tournament.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = AcidLime,
                    trackColor = Color(0xFF0A0A0F).copy(alpha = 0.5f),
                    strokeCap = StrokeCap.Round
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Prize Pool & Entry Fee + Big Lime Join Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "PRIZE POOL",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                        )
                        Text(
                            text = "${AppConfig.CURRENCY_SYMBOL}${tournament.prizePool}",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = AcidLime,
                                fontSize = 24.sp
                            )
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "ENTRY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                        )
                        Text(
                            text = if (AppConfig.enablePaidEntry && tournament.entryFee > 0) {
                                "${AppConfig.CURRENCY_SYMBOL}${tournament.entryFee}"
                            } else {
                                "FREE"
                            },
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = AcidLime
                            )
                        )
                    }

                    PrimaryButton(
                        text = if (tournament.isJoined) "ENTERED" else "JOIN NOW",
                        onClick = onJoinClick,
                        enabled = !tournament.isJoined,
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("hero_join_btn")
                    )
                }
            }
        }
    }
}

// Daily Streak Card
@Composable
fun DailyStreakCard(
    user: User?,
    onClaimClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.dp, CardBorder),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFFF9100).copy(alpha = 0.15f))
                        .border(1.dp, Color(0xFFFF9100).copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Daily Streak",
                        tint = Color(0xFFFF9100),
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "${user?.currentStreak ?: 1} DAYS STREAK 🔥",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = if (user?.streakClaimedToday == true) {
                            "Reward claimed for today! Return tomorrow."
                        } else {
                            "+250 XP & Sponsor Boost ready"
                        },
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (user?.streakClaimedToday == true) TextMuted else AcidLime
                        )
                    )
                }
            }

            if (user?.streakClaimedToday == true) {
                Box(
                    modifier = Modifier
                        .background(CardBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "CLAIMED",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                    )
                }
            } else {
                PrimaryButton(
                    text = "CLAIM",
                    onClick = onClaimClick,
                    modifier = Modifier.height(42.dp),
                    testTag = "claim_streak_btn"
                )
            }
        }
    }
}
