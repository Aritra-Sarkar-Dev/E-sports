package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.components.FFArenaChip
import com.example.core.components.TournamentCard
import com.example.core.model.DivisionTier
import com.example.core.model.GameMode
import com.example.ui.FFArenaViewModel
import com.example.ui.theme.AcidLime
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun CompeteScreen(
    viewModel: FFArenaViewModel,
    modifier: Modifier = Modifier
) {
    val tournaments by viewModel.tournaments.collectAsState()
    val selectedMode by viewModel.selectedFilterMode.collectAsState()
    var selectedDivision by remember { mutableStateOf<DivisionTier?>(null) }

    val filteredTournaments = tournaments.filter { t ->
        val modeMatches = when (selectedMode) {
            GameMode.ALL -> true
            GameMode.FREE -> t.entryFee == 0
            else -> t.mode == selectedMode
        }
        val divisionMatches = selectedDivision == null || t.division == selectedDivision
        modeMatches && divisionMatches
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("compete_screen_list"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Screen Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "BATTLE ARENAS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AcidLime,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Compete & Win",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Free to enter, sponsor-funded esports tournaments for Free Fire MAX",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                )
            }
        }

        // Mode Filter Chips
        item {
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                Text(
                    text = "MODE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(GameMode.values()) { mode ->
                        FFArenaChip(
                            label = mode.label,
                            isSelected = selectedMode == mode,
                            onClick = { viewModel.setFilterMode(mode) }
                        )
                    }
                }
            }
        }

        // Division Filter Chips (ROOKIE, MID, PRO, OPEN)
        item {
            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                Text(
                    text = "SKILL DIVISION",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FFArenaChip(
                            label = "All Tiers",
                            isSelected = selectedDivision == null,
                            onClick = { selectedDivision = null }
                        )
                    }
                    items(DivisionTier.values()) { division ->
                        FFArenaChip(
                            label = division.label,
                            isSelected = selectedDivision == division,
                            onClick = {
                                selectedDivision = if (selectedDivision == division) null else division
                            }
                        )
                    }
                }
            }
        }

        // List Summary count
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredTournaments.size} TOURNAMENTS AVAILABLE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                )
                Text(
                    text = "Live Matchmaking",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = AcidLime
                    )
                )
            }
        }

        // Tournament Cards
        if (filteredTournaments.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tournaments found matching the filters.\nTry selecting 'All' or a different division.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(filteredTournaments, key = { it.id }) { tournament ->
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
