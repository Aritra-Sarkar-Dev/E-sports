package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.SportsScore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.CompeteScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MyMatchesScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SignUpScreen
import com.example.ui.screens.TournamentDetailsBottomSheet
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.AcidLime
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FFArenaApp(
    viewModel: FFArenaViewModel = viewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val authScreen by viewModel.authScreen.collectAsState()
    val currentBottomTab by viewModel.currentBottomTab.collectAsState()
    val selectedTournamentDetails by viewModel.selectedTournamentDetails.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()

    var isViewingWallet by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Handle snackbar messages
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (currentUser == null) {
            // AUTH FLOW: Login <-> Sign Up
            AnimatedContent(
                targetState = authScreen,
                transitionSpec = {
                    fadeIn(animationSpec = tween(250)) togetherWith fadeOut(animationSpec = tween(250))
                },
                label = "auth_transition"
            ) { screen ->
                when (screen) {
                    AuthScreen.LOGIN, AuthScreen.FORGOT_PASSWORD, AuthScreen.PHONE_OTP -> {
                        LoginScreen(
                            viewModel = viewModel,
                            onLoginSuccess = {
                                viewModel.setBottomTab(BottomTab.HOME)
                            }
                        )
                    }
                    AuthScreen.SIGN_UP -> {
                        SignUpScreen(
                            viewModel = viewModel,
                            onSignUpSuccess = {
                                viewModel.setBottomTab(BottomTab.HOME)
                            }
                        )
                    }
                }
            }
        } else {
            // MAIN FLOW: Authenticated User with 5-tab Bottom Navigation
            Scaffold(
                containerColor = Color.Transparent,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                bottomBar = {
                    if (!isViewingWallet) {
                        FFArenaBottomBar(
                            currentTab = currentBottomTab,
                            onTabSelected = { tab ->
                                isViewingWallet = false
                                viewModel.setBottomTab(tab)
                            }
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (isViewingWallet) {
                        WalletScreen(
                            viewModel = viewModel,
                            onBackClick = { isViewingWallet = false }
                        )
                    } else {
                        AnimatedContent(
                            targetState = currentBottomTab,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                            },
                            label = "tab_transition"
                        ) { tab ->
                            when (tab) {
                                BottomTab.HOME -> HomeScreen(viewModel = viewModel)
                                BottomTab.COMPETE -> CompeteScreen(viewModel = viewModel)
                                BottomTab.MATCHES -> MyMatchesScreen(viewModel = viewModel)
                                BottomTab.RANKS -> LeaderboardScreen(viewModel = viewModel)
                                BottomTab.PROFILE -> ProfileScreen(
                                    viewModel = viewModel,
                                    onNavigateWallet = { isViewingWallet = true }
                                )
                            }
                        }
                    }
                }
            }

            // Tournament Details Bottom Sheet
            if (selectedTournamentDetails != null) {
                TournamentDetailsBottomSheet(
                    tournament = selectedTournamentDetails!!,
                    sheetState = sheetState,
                    viewModel = viewModel,
                    onDismiss = { viewModel.closeTournamentDetails() }
                )
            }
        }
    }
}

// Bottom Navigation with 5 tabs: Home, Compete, Matches, Ranks, Profile (Active tab is lime)
@Composable
fun FFArenaBottomBar(
    currentTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit
) {
    Surface(
        color = CardSurface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, CardBorder),
        shadowElevation = 16.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("main_bottom_nav_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .height(68.dp)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                isSelected = currentTab == BottomTab.HOME,
                onClick = { onTabSelected(BottomTab.HOME) },
                testTag = "nav_home_tab"
            )
            BottomNavItem(
                icon = Icons.Default.SportsEsports,
                label = "Compete",
                isSelected = currentTab == BottomTab.COMPETE,
                onClick = { onTabSelected(BottomTab.COMPETE) },
                testTag = "nav_compete_tab"
            )
            BottomNavItem(
                icon = Icons.Default.SportsScore,
                label = "Matches",
                isSelected = currentTab == BottomTab.MATCHES,
                onClick = { onTabSelected(BottomTab.MATCHES) },
                testTag = "nav_matches_tab"
            )
            BottomNavItem(
                icon = Icons.Default.Leaderboard,
                label = "Ranks",
                isSelected = currentTab == BottomTab.RANKS,
                onClick = { onTabSelected(BottomTab.RANKS) },
                testTag = "nav_ranks_tab"
            )
            BottomNavItem(
                icon = Icons.Default.Person,
                label = "Profile",
                isSelected = currentTab == BottomTab.PROFILE,
                onClick = { onTabSelected(BottomTab.PROFILE) },
                testTag = "nav_profile_tab"
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(if (isSelected) 36.dp else 28.dp)
                .background(
                    if (isSelected) AcidLime.copy(alpha = 0.15f) else Color.Transparent,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) AcidLime else TextMuted,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                fontSize = 11.sp,
                color = if (isSelected) AcidLime else TextMuted
            )
        )
    }
}
