package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.model.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(colorScheme = FFArenaColorScheme) {
                FFArenaPlayerApp()
            }
        }
    }
}

@Composable
fun FFArenaPlayerApp() {
    val clipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Navigation State (Strictly 4 tabs)
    var currentTab by remember { mutableStateOf("Home") }

    // Back button handling: if in sub screen or not on Home, back goes to Home
    var showOrganizerConsole by remember { mutableStateOf(false) }

    BackHandler(enabled = showOrganizerConsole || currentTab != "Home") {
        if (showOrganizerConsole) {
            showOrganizerConsole = false
        } else {
            currentTab = "Home"
        }
    }

    // App State (Zero real-money wallet)
    var profile by remember { mutableStateOf(MockData.defaultProfile) }
    var coins by remember { mutableIntStateOf(540) }
    var streakDay by remember { mutableIntStateOf(5) }
    var hasCheckedInToday by remember { mutableStateOf(false) }

    val matches = TournamentService.matches
    var missions by remember { mutableStateOf(MockData.missionsList) }
    var storeItems by remember { mutableStateOf(MockData.storeList) }
    val notifications = TournamentService.notifications

    // Modals state
    var selectedDetailMatch by remember { mutableStateOf<EsportsMatch?>(null) }
    var selectedJoinMatch by remember { mutableStateOf<EsportsMatch?>(null) }
    var showSpinWheel by remember { mutableStateOf(false) }
    var showLeaderboard by remember { mutableStateOf(false) }
    var showNotifications by remember { mutableStateOf(false) }
    var reportMatch by remember { mutableStateOf<EsportsMatch?>(null) }
    var showPrizeHistory by remember { mutableStateOf(false) }
    var selectedPrizeDetail by remember { mutableStateOf<PrizeRecord?>(null) }
    var showEditProfile by remember { mutableStateOf(false) }
    var showVerifyAccount by remember { mutableStateOf(false) }
    var showFairPlay by remember { mutableStateOf(false) }
    var showTerms by remember { mutableStateOf(false) }
    var showHelpSupport by remember { mutableStateOf(false) }
    var showReferral by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    fun showToast(message: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BgDark,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 90.dp)
            ) { data ->
                Card(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, NeonLime.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonLime, modifier = Modifier.size(18.dp))
                        Text(data.visuals.message, fontSize = 13.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        bottomBar = {
            if (!showOrganizerConsole) {
                FloatingPillBottomNav(
                    currentTab = currentTab,
                    onTabSelect = { currentTab = it }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (showOrganizerConsole) {
                OrganizerConsoleScreen(
                    onClose = { showOrganizerConsole = false },
                    onShowToast = { showToast(it) }
                )
            } else {
                when (currentTab) {
                    "Home" -> {
                        HomeScreen(
                            profile = profile,
                            coins = coins,
                            matches = matches,
                            leaderboard = MockData.leaderboardList,
                            unreadCount = notifications.count { !it.isRead },
                            onOpenNotifications = { showNotifications = true },
                            onOpenRewards = { currentTab = "Rewards" },
                            onOpenSpinWheel = { showSpinWheel = true },
                            onDailyCheckIn = {
                                if (!hasCheckedInToday) {
                                    hasCheckedInToday = true
                                    coins += 50
                                    showToast("Daily Streak Claimed: +50 Loyalty Coins!")
                                }
                            },
                            hasCheckedInToday = hasCheckedInToday,
                            onOpenMatchDetail = { selectedDetailMatch = it },
                            onOpenLeaderboard = { showLeaderboard = true },
                            onSelectModeFilter = {
                                currentTab = "Matches"
                            }
                        )
                    }

                    "Matches" -> {
                        MatchesScreen(
                            matches = matches,
                            onOpenMatchDetail = { selectedDetailMatch = it },
                            onJoinMatch = { selectedJoinMatch = it },
                            onReportMatch = { reportMatch = it }
                        )
                    }

                    "Rewards" -> {
                        RewardsScreen(
                            coins = coins,
                            streakDay = streakDay,
                            hasCheckedInToday = hasCheckedInToday,
                            missions = missions,
                            storeItems = storeItems,
                            onDailyCheckIn = {
                                if (!hasCheckedInToday) {
                                    hasCheckedInToday = true
                                    coins += 50
                                    showToast("Day $streakDay claimed! +50 Loyalty Coins.")
                                }
                            },
                            onSpinWheelClick = { showSpinWheel = true },
                            onClaimMission = { mission ->
                                missions = missions.map {
                                    if (it.id == mission.id) it.copy(isClaimed = true) else it
                                }
                                coins += mission.rewardCoins
                                showToast("Quest Complete: +${mission.rewardCoins} Coins!")
                            },
                            onRedeemStoreItem = { item ->
                                if (coins >= item.coins) {
                                    coins -= item.coins
                                    storeItems = storeItems.map {
                                        if (it.id == item.id) it.copy(isPurchased = true) else it
                                    }
                                    showToast("Redeemed ${item.title}!")
                                } else {
                                    showToast("Need ${item.coins - coins} more coins to redeem.")
                                }
                            }
                        )
                    }

                    "Profile" -> {
                        ProfileScreen(
                            profile = profile,
                            onCopyUid = {
                                clipboardManager.setText(AnnotatedString(profile.ffUid))
                                showToast("Free Fire UID copied to clipboard!")
                            },
                            onCopyReferral = {
                                clipboardManager.setText(AnnotatedString(profile.referralCode))
                                showToast("Code copied: ${profile.referralCode}. Both of you get 100 bonus coins!")
                            },
                            onOpenEditProfile = {
                                showEditProfile = true
                            },
                            onOpenOrganizerConsole = {
                                showOrganizerConsole = true
                            },
                            onToggleRole = {
                                val nextRole = if (profile.role == "organizer") "player" else "organizer"
                                profile = profile.copy(role = nextRole)
                                showToast("Demo Role Switched to: ${nextRole.uppercase()}")
                            },
                            onOpenSettings = { setting ->
                                when (setting) {
                                    "edit_profile" -> showEditProfile = true
                                    "notifications" -> showNotifications = true
                                    "verify" -> showVerifyAccount = true
                                    "prize_history" -> showPrizeHistory = true
                                    "referral" -> showReferral = true
                                    "fairplay" -> showFairPlay = true
                                    "terms" -> showTerms = true
                                    "support" -> showHelpSupport = true
                                    "logout" -> showLogoutDialog = true
                                    else -> showToast("Opened $setting")
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Modal Dialogs
    selectedDetailMatch?.let { match ->
        MatchDetailModal(
            match = match,
            onDismiss = { selectedDetailMatch = null },
            onJoinClick = {
                selectedJoinMatch = match
            },
            onCopyRoomCredentials = {
                if (match.roomId != null) {
                    clipboardManager.setText(AnnotatedString("Room: ${match.roomId} Pass: ${match.roomPass ?: "ffpro"}"))
                    showToast("Room ID & Pass copied!")
                }
            }
        )
    }

    selectedJoinMatch?.let { match ->
        JoinMatchModal(
            match = match,
            userProfile = profile,
            onDismiss = { selectedJoinMatch = null },
            onConfirmJoin = { ign, teammates ->
                TournamentService.addRegistration(match.id, ign, profile.ffUid)
                val idx = TournamentService.matches.indexOfFirst { it.id == match.id }
                if (idx != -1) {
                    val old = TournamentService.matches[idx]
                    TournamentService.matches[idx] = old.copy(
                        isJoined = true,
                        joinedSlots = (old.joinedSlots + 1).coerceAtMost(old.maxSlots),
                        roomId = old.roomId ?: "884910",
                        roomPass = old.roomPass ?: "ffpro99",
                        isRoomRevealed = true
                    )
                }
                coins += 20
                showToast("🎉 Registered for ${match.title}! +20 Loyalty Coins.")
            }
        )
    }

    if (showSpinWheel) {
        SpinWheelModal(
            onDismiss = { showSpinWheel = false },
            onSpinWin = { prizeCoins ->
                coins += prizeCoins
                showToast("🎉 Won +$prizeCoins Coins in Daily Spin!")
            }
        )
    }

    if (showLeaderboard) {
        LeaderboardModal(
            rows = MockData.leaderboardList,
            onDismiss = { showLeaderboard = false }
        )
    }

    if (showNotifications) {
        NotificationsModal(
            notifications = notifications,
            onDismiss = { showNotifications = false },
            onMarkAllRead = {
                val updated = notifications.map { it.copy(isRead = true) }
                notifications.clear()
                notifications.addAll(updated)
                showToast("All notifications marked as read")
            }
        )
    }

    reportMatch?.let { match ->
        ReportModal(
            match = match,
            onDismiss = { reportMatch = null },
            onSubmitReport = { reason, desc ->
                showToast("Dispute ticket submitted to referee. Ticket ID: #DISP${System.currentTimeMillis() % 10000}")
            }
        )
    }

    if (showPrizeHistory) {
        PrizeHistoryModal(
            prizes = TournamentService.prizeRecords,
            onDismiss = { showPrizeHistory = false },
            onSelectPrize = { selectedPrizeDetail = it }
        )
    }

    selectedPrizeDetail?.let { prize ->
        PrizeDetailModal(
            prize = prize,
            onDismiss = { selectedPrizeDetail = null }
        )
    }

    if (showEditProfile) {
        EditProfileModal(
            currentIgn = profile.ign,
            currentUid = profile.ffUid,
            onDismiss = { showEditProfile = false },
            onSave = { newIgn, newUid ->
                profile = profile.copy(ign = newIgn, ffUid = newUid)
                showToast("Profile updated successfully!")
            }
        )
    }

    if (showVerifyAccount) {
        VerifyAccountModal(onDismiss = { showVerifyAccount = false })
    }

    if (showFairPlay) {
        FairPlayModal(onDismiss = { showFairPlay = false })
    }

    if (showTerms) {
        TermsModal(onDismiss = { showTerms = false })
    }

    if (showHelpSupport) {
        HelpSupportModal(onDismiss = { showHelpSupport = false }, onShowToast = { showToast(it) })
    }

    if (showReferral) {
        ReferralModal(
            profile = profile,
            onCopyReferral = {
                clipboardManager.setText(AnnotatedString(profile.referralCode))
                showToast("Referral code copied! Both get 100 bonus coins.")
            },
            onDismiss = { showReferral = false }
        )
    }

    if (showLogoutDialog) {
        LogoutDialog(
            onConfirm = {
                showLogoutDialog = false
                showToast("Logged out successfully.")
            },
            onDismiss = { showLogoutDialog = false }
        )
    }
}

/**
 * Floating Pill Bottom Navigation
 * Strict 4 tabs: Home, Matches, Rewards, Profile
 * Active tab: lime pill with label
 * Inactive tabs: icon only
 */
@Composable
fun FloatingPillBottomNav(
    currentTab: String,
    onTabSelect: (String) -> Unit
) {
    val items = listOf(
        NavigationItem("Home", Icons.Default.Home),
        NavigationItem("Matches", Icons.Default.EmojiEvents),
        NavigationItem("Rewards", Icons.Default.CardGiftcard),
        NavigationItem("Profile", Icons.Default.Person)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .shadow(elevation = 20.dp, shape = RoundedCornerShape(29.dp), spotColor = NeonLime.copy(alpha = 0.35f))
                .clip(RoundedCornerShape(29.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(29.dp))
                .testTag("floating_bottom_nav"),
            shape = RoundedCornerShape(29.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xF014141F))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { navItem ->
                    val isSelected = currentTab == navItem.label

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) NeonLime else Color.Transparent)
                            .clickable { onTabSelect(navItem.label) }
                            .padding(
                                horizontal = if (isSelected) 16.dp else 10.dp,
                                vertical = 8.dp
                            )
                            .testTag("tab_${navItem.label.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = navItem.label,
                                tint = if (isSelected) Color.Black else TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            if (isSelected) {
                                Text(
                                    text = navItem.label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class NavigationItem(
    val label: String,
    val icon: ImageVector
)
