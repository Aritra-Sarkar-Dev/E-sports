package com.example.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.model.UserProfile
import com.example.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(
    profile: UserProfile,
    onCopyUid: () -> Unit,
    onCopyReferral: () -> Unit,
    onOpenEditProfile: () -> Unit,
    onOpenOrganizerConsole: () -> Unit,
    onToggleRole: () -> Unit,
    onOpenSettings: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
    ) {
        // Profile Card with Glowing Avatar (Supports long-press shortcut to Console)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF17113D), CardSurface)
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar with long-press hidden shortcut
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(CircleShape)
                                .background(CardSurface)
                                .border(2.dp, NeonLime, CircleShape)
                                .combinedClickable(
                                    onClick = { onOpenEditProfile() },
                                    onLongClick = { onOpenOrganizerConsole() }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile Avatar (Long press for Organizer Console)",
                                tint = NeonLime,
                                modifier = Modifier.size(44.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = profile.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = NeonLime.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonLime.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = profile.rankTier,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = NeonLime,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = profile.ign,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonLime
                            )
                            Text("•", color = TextSecondary)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { onCopyUid() }
                            ) {
                                Text("UID: ${profile.ffUid}", fontSize = 11.sp, color = TextSecondary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy UID", tint = TextSecondary, modifier = Modifier.size(12.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // XP Progress Bar
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(ElevatedSurface)
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Level ${profile.level}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                                Text("${profile.xp}/${profile.maxXp} XP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(BgDark)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(profile.xp.toFloat() / profile.maxXp)
                                        .fillMaxHeight()
                                        .background(NeonLime)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Dedicated Organizer Console Button (Shown ONLY when user.role is 'admin' or 'organizer')
        if (profile.role == "organizer" || profile.role == "admin") {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenOrganizerConsole() },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(2.dp, NeonLime)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(listOf(Color(0xFF17113D), CardSurface))
                            )
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(NeonLime.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = NeonLime, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Organizer Console", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = NeonLime,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            profile.role.uppercase(),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.Black,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text("Create tournaments, set room ID, publish results", fontSize = 10.sp, color = TextSecondary)
                            }
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NeonLime, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }

        // 6-Metric Stats Grid (NO real-money earnings! "PRIZES WON" count)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "CAREER STATISTICS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 1.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard("Matches", "${profile.matches}", modifier = Modifier.weight(1f))
                    StatCard("Booyah", "${profile.wins}", tint = NeonLime, modifier = Modifier.weight(1f))
                    StatCard("Kills", "${profile.kills}", modifier = Modifier.weight(1f))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard("K/D Ratio", "${profile.kdRatio}", modifier = Modifier.weight(1f))
                    StatCard("Win Rate", "${profile.winRate}%", tint = ElectricViolet, modifier = Modifier.weight(1f))
                    StatCard("PRIZES WON", "${profile.prizesWon}", tint = GoldCoins, modifier = Modifier.weight(1f))
                }
            }
        }

        // Badges: "Top Player" (renamed from Top Earner)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "EARNED BADGES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 1.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BadgeCard("👑", "Top Player", "Tier 1 Elite", NeonLime, modifier = Modifier.weight(1f))
                    BadgeCard("⚡", "Booyah Master", "10+ Victories", ElectricViolet, modifier = Modifier.weight(1f))
                    BadgeCard("🎯", "Sharpshooter", "150+ Headshots", Color.White, modifier = Modifier.weight(1f))
                }
            }
        }

        // Referral Card (Coins only, no cash bonus!)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("INVITE FRIENDS", fontSize = 10.sp, fontWeight = FontWeight.Black, color = NeonLime)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Both of you get 100 bonus coins", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Code: ${profile.referralCode}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldCoins)
                    }

                    Button(
                        onClick = { onCopyReferral() },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Share", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
        }

        // Settings & Policies (Fully interactive rows)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "SETTINGS & POLICIES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 1.sp
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column {
                        // Edit Profile
                        SettingsItem(
                            icon = Icons.Default.Edit,
                            title = "Edit Player Profile",
                            subtitle = "Update IGN and Free Fire UID",
                            onClick = { onOpenSettings("edit_profile") }
                        )

                        Divider(color = CardBorder, thickness = 1.dp)

                        // Notifications Settings
                        SettingsItem(
                            icon = Icons.Default.Notifications,
                            title = "Notifications & Alerts",
                            subtitle = "Match reminders & room ID reveals",
                            onClick = { onOpenSettings("notifications") }
                        )

                        Divider(color = CardBorder, thickness = 1.dp)

                        // Verify Account
                        SettingsItem(
                            icon = Icons.Default.Verified,
                            title = "Verify Account",
                            subtitle = "UID verification & 18+ check",
                            onClick = { onOpenSettings("verify") }
                        )

                        Divider(color = CardBorder, thickness = 1.dp)

                        // Prize History
                        SettingsItem(
                            icon = Icons.Default.EmojiEvents,
                            title = "Sponsor Prize History",
                            subtitle = "Track recorded and delivered prizes",
                            onClick = { onOpenSettings("prize_history") }
                        )

                        Divider(color = CardBorder, thickness = 1.dp)

                        // Referral
                        SettingsItem(
                            icon = Icons.Default.CardGiftcard,
                            title = "Invite Friends & Referral",
                            subtitle = "Both get 100 bonus coins",
                            onClick = { onOpenSettings("referral") }
                        )

                        Divider(color = CardBorder, thickness = 1.dp)

                        // Demo Role Switch & Organizer Console
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenOrganizerConsole() }
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = NeonLime, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Organizer Console", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = NeonLime,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text("DEMO", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }
                                    Text("Create tournaments, set room ID, publish results", fontSize = 10.sp, color = TextSecondary)
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NeonLime, modifier = Modifier.size(18.dp))
                        }

                        Divider(color = CardBorder, thickness = 1.dp)

                        // Fair Play & Anti-cheat
                        SettingsItem(
                            icon = Icons.Default.Shield,
                            title = "Fair Play & Anti-Cheat Protocol",
                            subtitle = "Free entry. Prizes are sponsor-funded.",
                            onClick = { onOpenSettings("fairplay") }
                        )

                        Divider(color = CardBorder, thickness = 1.dp)

                        // Terms and Privacy
                        SettingsItem(
                            icon = Icons.Default.VerifiedUser,
                            title = "Terms & Privacy Policy",
                            subtitle = "Certified non-gambling esports competition",
                            onClick = { onOpenSettings("terms") }
                        )

                        Divider(color = CardBorder, thickness = 1.dp)

                        // Help & Support
                        SettingsItem(
                            icon = Icons.Default.HelpOutline,
                            title = "Help & Dispute Support",
                            subtitle = "24/7 Referee assistance",
                            onClick = { onOpenSettings("support") }
                        )

                        Divider(color = CardBorder, thickness = 1.dp)

                        // Logout
                        SettingsItem(
                            icon = Icons.Default.Logout,
                            title = "Log Out",
                            subtitle = "Sign out of your account",
                            onClick = { onOpenSettings("logout") }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun StatCard(
    label: String,
    value: String,
    tint: Color = Color.White,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, fontSize = 9.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Black, color = tint)
        }
    }
}

@Composable
fun BadgeCard(
    emoji: String,
    title: String,
    subtitle: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontSize = 10.sp, fontWeight = FontWeight.Black, color = tint)
            Text(subtitle, fontSize = 8.sp, color = TextSecondary)
        }
    }
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(subtitle, fontSize = 10.sp, color = TextSecondary)
            }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
    }
}
