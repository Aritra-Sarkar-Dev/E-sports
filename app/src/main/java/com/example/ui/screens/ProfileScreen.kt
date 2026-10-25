package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.model.UserProfile
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    profile: UserProfile,
    onCopyUid: () -> Unit,
    onCopyReferral: () -> Unit,
    onOpenKyc: () -> Unit,
    onOpenEditProfile: () -> Unit,
    onOpenSettings: (String) -> Unit
) {
    val xpProgress = (profile.xp.toFloat() / profile.maxXp.toFloat()).coerceIn(0f, 1f)

    val stats = listOf(
        "MATCHES" to "${profile.matches}",
        "VICTORIES" to "${profile.wins}",
        "TOTAL KILLS" to "${profile.kills}",
        "K/D RATIO" to "${profile.kdRatio}",
        "WIN RATE" to "${profile.winRate}%",
        "COINS" to "${profile.totalEarnings}"
    )

    val badges = listOf(
        "Headshot King" to Icons.Default.GpsFixed,
        "Squad Leader" to Icons.Default.Groups,
        "Clutch God" to Icons.Default.FlashOn,
        "Grandmaster" to Icons.Default.MilitaryTech,
        "Top Player" to Icons.Default.EmojiEvents
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
    ) {
        // Hero Profile Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
                    .testTag("profile_hero_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Cover Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(ElectricViolet.copy(alpha = 0.5f), FireOrange.copy(alpha = 0.5f), NeonLime.copy(alpha = 0.3f))
                                )
                            )
                    )

                    // Avatar & Info Overlay
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-36).dp)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Avatar with glowing border
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(CircleShape)
                                .background(CardSurface)
                                .border(3.dp, NeonLime, CircleShape)
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(listOf(ElectricViolet, FireOrange))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = profile.name.take(2).uppercase(),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }

                        // Username & IGN
                        Text(
                            text = profile.ign,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )

                        Text(
                            text = profile.name,
                            fontSize = 12.sp,
                            color = TextMuted
                        )

                        // Free Fire UID Chip with copy
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(ChipBg)
                                .border(1.dp, CardBorder, RoundedCornerShape(8.dp))
                                .clickable { onCopyUid() }
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("FF UID: ${profile.ffUid}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = FireOrange)
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy UID", tint = FireOrange, modifier = Modifier.size(12.dp))
                        }

                        // Level & XP Bar
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Level ${profile.level} • ${profile.rankTier}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text("${profile.xp} / ${profile.maxXp} XP", fontSize = 11.sp, color = TextMuted)
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(ChipBg)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(xpProgress)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(Brush.horizontalGradient(listOf(ElectricViolet, NeonLime)))
                                )
                            }
                        }
                    }
                }
            }
        }

        // Stats Grid (6 Items)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "LIFETIME ESPORTS STATS",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = 0.5.sp
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    stats.chunked(3).forEach { rowStats ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowStats.forEach { (label, value) ->
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(14.dp))
                                        .border(1.dp, CardBorder, RoundedCornerShape(14.dp)),
                                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp, horizontal = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                                        Text(
                                            value,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (label == "COINS") GoldPrize else TextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Achievements / Badges Strip
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "HONOR BADGES & TROPHIES",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = 0.5.sp
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(badges) { (name, icon) ->
                        Column(
                            modifier = Modifier
                                .width(88.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(CardSurface)
                                .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
                                .padding(vertical = 12.dp, horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(NeonLime.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(icon, contentDescription = null, tint = NeonLime, modifier = Modifier.size(20.dp))
                            }
                            Text(
                                name,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        // Referral Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Brush.horizontalGradient(listOf(NeonLime.copy(alpha = 0.4f), ElectricViolet.copy(alpha = 0.4f))), RoundedCornerShape(16.dp))
                    .testTag("referral_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("INVITE FRIENDS", fontSize = 13.sp, fontWeight = FontWeight.Black, color = NeonLime)
                        Text("Share your code and both of you get 100 bonus coins.", fontSize = 11.sp, color = TextMuted)
                    }

                    Button(
                        onClick = onCopyReferral,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonLime,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.testTag("copy_referral_button")
                    ) {
                        Text(profile.referralCode, fontSize = 12.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
        }

        // Settings & Info List
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "SETTINGS & SUPPORT",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = 0.5.sp
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardSurface)
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                ) {
                    SettingItem(
                        icon = Icons.Default.Edit,
                        title = "Edit Gamer Profile",
                        subtitle = "IGN, Free Fire UID, Avatar",
                        onClick = onOpenEditProfile
                    )
                    Divider(color = CardBorder, thickness = 0.5.dp)

                    SettingItem(
                        icon = Icons.Default.Notifications,
                        title = "Notification Preferences",
                        subtitle = "Match reminders, room passwords, alerts",
                        onClick = { onOpenSettings("notifications") }
                    )
                    Divider(color = CardBorder, thickness = 0.5.dp)

                    SettingItem(
                        icon = Icons.Default.Security,
                        title = "Fair Play & Anti-Cheat Policy",
                        subtitle = "Free entry. Prizes are sponsor-funded.",
                        onClick = { onOpenSettings("fairplay") }
                    )
                    Divider(color = CardBorder, thickness = 0.5.dp)

                    SettingItem(
                        icon = Icons.Default.Policy,
                        title = "Terms of Service & Privacy Policy",
                        subtitle = "18+ age verification & legal disclaimers",
                        onClick = { onOpenSettings("terms") }
                    )
                    Divider(color = CardBorder, thickness = 0.5.dp)

                    SettingItem(
                        icon = Icons.Default.SupportAgent,
                        title = "24/7 Gamer Support & Telegram",
                        subtitle = "Fast ticket response for room issues",
                        onClick = { onOpenSettings("support") }
                    )
                    Divider(color = CardBorder, thickness = 0.5.dp)

                    SettingItem(
                        icon = Icons.Default.Logout,
                        title = "Log Out",
                        subtitle = "Sign out of your session",
                        isDestructive = true,
                        onClick = { onOpenSettings("logout") }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isDestructive) LiveRed.copy(alpha = 0.12f) else ChipBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isDestructive) LiveRed else NeonLime,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDestructive) LiveRed else TextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextMuted
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
