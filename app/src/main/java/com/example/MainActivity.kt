package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Navigation State
    var currentTab by remember { mutableStateOf("Home") }

    // Back button handling: if not on Home, back goes to Home
    BackHandler(enabled = currentTab != "Home") {
        currentTab = "Home"
    }

    // App State
    var profile by remember { mutableStateOf(MockData.defaultProfile) }
    var wallet by remember { mutableStateOf(MockData.defaultWallet) }
    var coins by remember { mutableIntStateOf(540) }
    var streakDay by remember { mutableIntStateOf(4) }
    var hasCheckedInToday by remember { mutableStateOf(false) }

    var matches by remember { mutableStateOf(MockData.matchesList) }
    var transactions by remember { mutableStateOf(MockData.transactionsList) }
    var missions by remember { mutableStateOf(MockData.missionsList) }
    val storeItems = remember { MockData.storeList }
    var notifications by remember { mutableStateOf(MockData.notificationsList) }

    // Modals state
    var selectedDetailMatch by remember { mutableStateOf<EsportsMatch?>(null) }
    var selectedJoinMatch by remember { mutableStateOf<EsportsMatch?>(null) }
    var showSpinWheel by remember { mutableStateOf(false) }
    var showAddMoney by remember { mutableStateOf(false) }
    var showWithdraw by remember { mutableStateOf(false) }
    var showLeaderboard by remember { mutableStateOf(false) }
    var showNotifications by remember { mutableStateOf(false) }
    var reportMatch by remember { mutableStateOf<EsportsMatch?>(null) }

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
            FloatingPillBottomNav(
                currentTab = currentTab,
                onTabSelect = { currentTab = it },
                unreadNotifications = notifications.count { !it.isRead }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                "Home" -> {
                    HomeScreen(
                        profile = profile,
                        wallet = wallet,
                        coins = coins,
                        matches = matches,
                        leaderboard = MockData.leaderboardList,
                        unreadCount = notifications.count { !it.isRead },
                        onOpenNotifications = { showNotifications = true },
                        onOpenWallet = { currentTab = "Wallet" },
                        onOpenSpinWheel = { showSpinWheel = true },
                        onDailyCheckIn = {
                            if (!hasCheckedInToday) {
                                hasCheckedInToday = true
                                coins += 40
                                showToast("Daily Check-In: +40 Coins added!")
                            }
                        },
                        hasCheckedInToday = hasCheckedInToday,
                        onOpenMatchDetail = { selectedDetailMatch = it },
                        onOpenLeaderboard = { showLeaderboard = true },
                        onSelectModeFilter = { mode ->
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

                "Wallet" -> {
                    WalletScreen(
                        wallet = wallet,
                        profile = profile,
                        transactions = transactions,
                        onAddMoneyClick = { showAddMoney = true },
                        onWithdrawClick = { showWithdraw = true }
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
                                coins += 40
                                showToast("Day $streakDay claimed! +40 Coins.")
                            }
                        },
                        onSpinWheelClick = { showSpinWheel = true },
                        onClaimMission = { mission ->
                            missions = missions.map {
                                if (it.id == mission.id) it.copy(isClaimed = true) else it
                            }
                            coins += mission.rewardCoins
                            showToast("Mission Claimed: +${mission.rewardCoins} Coins!")
                        },
                        onRedeemStoreItem = { item ->
                            if (coins >= item.coins) {
                                coins -= item.coins
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
                            showToast("Referral Code copied: ${profile.referralCode}")
                        },
                        onOpenKyc = {
                            showToast("KYC Status: Verified with UPI ${profile.linkedUpi}")
                        },
                        onOpenEditProfile = {
                            showToast("Edit Gamer Profile opened")
                        },
                        onOpenSettings = { setting ->
                            when (setting) {
                                "notifications" -> showNotifications = true
                                "support" -> showToast("Telegram Gamer Support: @FFArenaSupportBot")
                                "terms" -> showToast("18+ Only • Fair Play Certified")
                                "logout" -> showToast("Logged out of player session")
                                else -> showToast("Opened $setting")
                            }
                        }
                    )
                }
            }
        }
    }

    // Modal Dialogs
    selectedDetailMatch?.let { match ->
        MatchDetailModal(
            match = match,
            userWallet = wallet,
            onDismiss = { selectedDetailMatch = null },
            onJoinClick = {
                selectedJoinMatch = match
            },
            onCopyRoomCredentials = {
                if (match.roomId != null) {
                    clipboardManager.setText(AnnotatedString("Room: ${match.roomId} Pass: ${match.roomPass}"))
                    showToast("Room ID & Pass copied!")
                }
            }
        )
    }

    selectedJoinMatch?.let { match ->
        JoinMatchModal(
            match = match,
            wallet = wallet,
            userProfile = profile,
            onDismiss = { selectedJoinMatch = null },
            onConfirmJoin = { ign, teammates ->
                // Deduct entry fee
                wallet = wallet.copy(
                    deposit = (wallet.deposit - match.entryFee).coerceAtLeast(0)
                )
                // Mark match as joined
                matches = matches.map {
                    if (it.id == match.id) {
                        it.copy(
                            isJoined = true,
                            joinedSlots = it.joinedSlots + 1,
                            roomId = "772901",
                            roomPass = "booyah99",
                            isRoomRevealed = true
                        )
                    } else it
                }
                // Add transaction record
                transactions = listOf(
                    TransactionRecord(
                        id = "tx_${System.currentTimeMillis()}",
                        title = "Entry Fee: ${match.title}",
                        type = "entry_fee",
                        amount = match.entryFee,
                        timeText = "Just now",
                        isPositive = false
                    )
                ) + transactions

                showToast("🎉 Successfully Registered for ${match.title}!")
            },
            onAddMoney = {
                showAddMoney = true
            }
        )
    }

    if (showSpinWheel) {
        SpinWheelModal(
            onDismiss = { showSpinWheel = false },
            onSpinWin = { prizeCoins ->
                coins += prizeCoins
                showToast("🎉 Won +$prizeCoins Coins in Lucky Spin!")
            }
        )
    }

    if (showAddMoney) {
        AddMoneyModal(
            onDismiss = { showAddMoney = false },
            onConfirmAdd = { amount ->
                wallet = wallet.copy(deposit = wallet.deposit + amount)
                transactions = listOf(
                    TransactionRecord(
                        id = "tx_${System.currentTimeMillis()}",
                        title = "UPI Deposit (PhonePe)",
                        type = "deposit",
                        amount = amount,
                        timeText = "Just now",
                        isPositive = true
                    )
                ) + transactions
                showToast("✓ Added ₹$amount to deposit wallet!")
            }
        )
    }

    if (showWithdraw) {
        WithdrawModal(
            currentWinnings = wallet.winnings,
            linkedUpi = profile.linkedUpi,
            onDismiss = { showWithdraw = false },
            onConfirmWithdraw = { amount, upi ->
                wallet = wallet.copy(winnings = wallet.winnings - amount)
                transactions = listOf(
                    TransactionRecord(
                        id = "tx_${System.currentTimeMillis()}",
                        title = "Withdrawal to $upi",
                        type = "withdrawal",
                        amount = amount,
                        timeText = "Processing (60s)",
                        isPositive = false
                    )
                ) + transactions
                showToast("⚡ ₹$amount withdrawal initiated to $upi!")
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
                notifications = notifications.map { it.copy(isRead = true) }
                showToast("All notifications marked as read")
            }
        )
    }

    reportMatch?.let { match ->
        ReportModal(
            match = match,
            onDismiss = { reportMatch = null },
            onSubmitReport = { reason, desc ->
                showToast("Dispute submitted to referee. Ticket ID: #DISP${System.currentTimeMillis() % 10000}")
            }
        )
    }
}

@Composable
fun FloatingPillBottomNav(
    currentTab: String,
    onTabSelect: (String) -> Unit,
    unreadNotifications: Int
) {
    val items = listOf(
        NavigationItem("Home", Icons.Default.Home),
        NavigationItem("Matches", Icons.Default.SportsEsports),
        NavigationItem("Wallet", Icons.Default.AccountBalanceWallet),
        NavigationItem("Rewards", Icons.Default.CardGiftcard),
        NavigationItem("Profile", Icons.Default.Person)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(32.dp), spotColor = NeonLime.copy(alpha = 0.4f))
                .clip(RoundedCornerShape(32.dp))
                .border(
                    1.dp,
                    Brush.horizontalGradient(
                        listOf(NeonLime.copy(alpha = 0.4f), ElectricViolet.copy(alpha = 0.4f), CardBorder)
                    ),
                    RoundedCornerShape(32.dp)
                )
                .testTag("floating_bottom_nav"),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xEE14141F))
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
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isSelected) NeonLime.copy(alpha = 0.15f) else Color.Transparent
                            )
                            .clickable { onTabSelect(navItem.label) }
                            .padding(vertical = 6.dp)
                            .testTag("tab_${navItem.label.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Box {
                                Icon(
                                    imageVector = navItem.icon,
                                    contentDescription = navItem.label,
                                    tint = if (isSelected) NeonLime else TextMuted,
                                    modifier = Modifier.size(22.dp)
                                )

                                if (navItem.label == "Matches") {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .align(Alignment.TopEnd)
                                            .clip(CircleShape)
                                            .background(LiveRed)
                                    )
                                }
                            }

                            Text(
                                text = navItem.label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) NeonLime else TextMuted
                            )
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
