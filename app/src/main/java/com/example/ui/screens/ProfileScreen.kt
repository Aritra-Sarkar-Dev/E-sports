package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SwitchAccessShortcut
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.core.components.InputField
import com.example.core.components.PrimaryButton
import com.example.core.components.StatTile
import com.example.core.config.AppConfig
import com.example.ui.FFArenaViewModel
import com.example.ui.theme.AcidLime
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardShape
import com.example.ui.theme.CardSurface
import com.example.ui.theme.ErrorDanger
import com.example.ui.theme.SecondaryViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun ProfileScreen(
    viewModel: FFArenaViewModel,
    onNavigateWallet: () -> Unit,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentUser.collectAsState()
    val isPaidEntryEnabled by viewModel.paidEntryEnabled.collectAsState()
    val showEditDialog by viewModel.showEditProfileDialog.collectAsState()
    var showLogoutConfirm by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Screen Title
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "FIGHTER PASSPORT",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AcidLime,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Player Profile",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                )
            }
        }

        // Profile Identity Card (Avatar, Name, UID, Edit button)
        item {
            Card(
                shape = CardShape,
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = BorderStroke(1.2.dp, CardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Avatar
                            Box {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF1B1B2C))
                                        .border(2.dp, AcidLime, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ElectricBolt,
                                        contentDescription = "Player avatar",
                                        tint = AcidLime,
                                        modifier = Modifier.size(34.dp)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(20.dp)
                                        .background(SecondaryViolet, CircleShape)
                                        .border(1.5.dp, CardSurface, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = user?.name ?: "Arjun Verma",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = user?.inGameName ?: "⚡ViperSniper⚡",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = AcidLime
                                    )
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // UID with copy button
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFF1E1E2C))
                                        .clickable {
                                            copyToClipboard(context, "Free Fire UID", user?.ffUid ?: "839472819")
                                            viewModel.showSnackbar("Copied Free Fire UID: ${user?.ffUid}")
                                        }
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "UID: ${user?.ffUid ?: "839472819"}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TextMuted,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy UID",
                                        tint = AcidLime,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }

                        // Edit Button
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E1E2C))
                                .border(1.dp, CardBorder, CircleShape)
                                .clickable { viewModel.openEditProfile() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit profile",
                                tint = TextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Level Progress Bar
                    val currentXp = user?.xp ?: 6850
                    val nextXp = user?.xpNextLevel ?: 10000
                    val progress = (currentXp.toFloat() / nextXp.toFloat()).coerceIn(0f, 1f)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Level ${user?.level ?: 42} Veteran",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "$currentXp / $nextXp XP",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = AcidLime,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = AcidLime,
                        trackColor = CardBorder,
                        strokeCap = StrokeCap.Round
                    )
                }
            }
        }

        // Stats Row (Matches, Wins, Kills, Win rate)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatTile(
                    label = "Matches",
                    value = "${user?.matchesPlayed ?: 138}",
                    modifier = Modifier.weight(1f)
                )
                StatTile(
                    label = "Wins",
                    value = "${user?.wins ?: 47}",
                    accentColor = Color(0xFFFFD700),
                    modifier = Modifier.weight(1f)
                )
                StatTile(
                    label = "Kills",
                    value = "${user?.kills ?: 612}",
                    modifier = Modifier.weight(1f)
                )
                StatTile(
                    label = "Win Rate",
                    value = "${String.format("%.1f", user?.winRate ?: 34.1f)}%",
                    accentColor = SecondaryViolet,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Sponsor Earnings / Wallet Access Card
        item {
            Card(
                shape = CardShape,
                colors = CardDefaults.cardColors(containerColor = Color(0xFF181827)),
                border = BorderStroke(1.dp, if (isPaidEntryEnabled) AcidLime else CardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable {
                        if (isPaidEntryEnabled) {
                            onNavigateWallet()
                        } else {
                            viewModel.showSnackbar("Tournaments are 100% free under India esports law. Prizes are distributed via UPI by sponsors.")
                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(AcidLime.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = AcidLime,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "SPONSOR EARNINGS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMuted
                                )
                            )
                            Text(
                                text = "${AppConfig.CURRENCY_SYMBOL}${String.format("%.2f", user?.sponsorEarningsBalance ?: 3450.0)}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AcidLime
                                )
                            )
                            Text(
                                text = if (isPaidEntryEnabled) "Tap to open Wallet & UPI options" else "Free Entry Mode • Verified Sponsor Payouts",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isPaidEntryEnabled) SecondaryViolet else TextMuted,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open",
                        tint = TextMuted
                    )
                }
            }
        }

        // Developer / Reviewer Config Toggle for India Compliance Rule
        item {
            Card(
                shape = CardShape,
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "COMPLIANCE & TESTING CONFIG",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = AcidLime
                            )
                        )
                        Text(
                            text = "enablePaidEntry = $isPaidEntryEnabled",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = if (isPaidEntryEnabled) {
                                "Wallet & Entry fees active"
                            } else {
                                "India Law: Free entry tournaments only"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                        )
                    }

                    TextButton(
                        onClick = { viewModel.togglePaidEntryForPreview() },
                        modifier = Modifier.testTag("toggle_paid_entry_btn")
                    ) {
                        Text(
                            text = if (isPaidEntryEnabled) "Turn OFF" else "Test Wallet",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = AcidLime,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        // Profile Menu List
        item {
            Card(
                shape = CardShape,
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    ProfileMenuItem(
                        icon = Icons.Default.Edit,
                        title = "Edit profile",
                        subtitle = "Update in-game name & contact details",
                        onClick = { viewModel.openEditProfile() }
                    )
                    Divider(color = CardBorder, thickness = 0.8.dp)
                    ProfileMenuItem(
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        subtitle = "Match starting alerts and room ID notifications",
                        onClick = { viewModel.showSnackbar("Match notification alerts are ENABLED.") }
                    )
                    Divider(color = CardBorder, thickness = 0.8.dp)
                    ProfileMenuItem(
                        icon = Icons.Default.Verified,
                        title = "Verify account",
                        subtitle = "Free Fire UID linked and verified",
                        onClick = { viewModel.showSnackbar("Free Fire UID 839472819 is already Level 30+ verified.") }
                    )
                    Divider(color = CardBorder, thickness = 0.8.dp)
                    ProfileMenuItem(
                        icon = Icons.Default.Security,
                        title = "Fair Play & Anti-Cheat Policy",
                        subtitle = "Server-side result verification & rules",
                        onClick = { viewModel.showSnackbar("Fair Play Policy: Emulator and trigger bans strictly enforced.") }
                    )
                    Divider(color = CardBorder, thickness = 0.8.dp)
                    ProfileMenuItem(
                        icon = Icons.Default.HelpOutline,
                        title = "Help and support",
                        subtitle = "24/7 Discord esports referee desk & FAQs",
                        onClick = { viewModel.showSnackbar("Support desk: Open ticket on FF Arena Discord or email support@arena.in") }
                    )
                    Divider(color = CardBorder, thickness = 0.8.dp)
                    ProfileMenuItem(
                        icon = Icons.Default.ExitToApp,
                        title = "Log out",
                        subtitle = "Sign out of your active fighter session",
                        titleColor = ErrorDanger,
                        iconTint = ErrorDanger,
                        onClick = { showLogoutConfirm = true },
                        testTag = "profile_logout_btn"
                    )
                }
            }
        }
    }

    // Edit Profile Modal Dialog
    if (showEditDialog) {
        var editName by remember { mutableStateOf(user?.name ?: "") }
        var editIgn by remember { mutableStateOf(user?.inGameName ?: "") }
        var editPhone by remember { mutableStateOf(user?.phone ?: "") }

        Dialog(onDismissRequest = { viewModel.closeEditProfile() }) {
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = CardSurface,
                border = BorderStroke(1.5.dp, SecondaryViolet),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Edit Fighter Profile",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    InputField(
                        label = "REAL NAME",
                        value = editName,
                        onValueChange = { editName = it }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    InputField(
                        label = "IN-GAME NAME (IGN)",
                        value = editIgn,
                        onValueChange = { editIgn = it }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    InputField(
                        label = "PHONE NUMBER",
                        value = editPhone,
                        onValueChange = { editPhone = it }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(
                            onClick = { viewModel.closeEditProfile() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel", color = TextMuted)
                        }
                        PrimaryButton(
                            text = "SAVE",
                            onClick = {
                                viewModel.updateProfile(editName, editIgn, editPhone)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }

    // Logout Confirmation Dialog
    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            containerColor = CardSurface,
            title = {
                Text(
                    text = "Log Out of FF Arena?",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
            },
            text = {
                Text(
                    text = "You will need to enter your Free Fire UID or credentials to re-access upcoming tournaments and match rooms.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutConfirm = false
                        viewModel.logout()
                    }
                ) {
                    Text("LOG OUT", color = ErrorDanger, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirm = false }) {
                    Text("CANCEL", color = TextPrimary)
                }
            }
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    titleColor: Color = TextPrimary,
    iconTint: Color = TextPrimary,
    testTag: String = "profile_menu_item"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = titleColor
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                )
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}

private fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}
