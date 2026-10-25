package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.model.MissionRecord
import com.example.ui.model.StoreRewardItem
import com.example.ui.theme.*

@Composable
fun RewardsScreen(
    coins: Int,
    streakDay: Int,
    hasCheckedInToday: Boolean,
    missions: List<MissionRecord>,
    storeItems: List<StoreRewardItem>,
    onDailyCheckIn: () -> Unit,
    onSpinWheelClick: () -> Unit,
    onClaimMission: (MissionRecord) -> Unit,
    onRedeemStoreItem: (StoreRewardItem) -> Unit
) {
    var activeSubTab by remember { mutableStateOf("Missions") }
    val streakDays = listOf(25, 30, 40, 50, 60, 80, 150)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
    ) {
        // Top Header
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = GoldCoins, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Loyalty Rewards",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Earn coins through matches, streaks & missions. Zero deposits.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        // Non-Cash policy notice
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = ElevatedSurface,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = GoldCoins, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Loyalty coins are non-cash game credits earned by playing and cannot be purchased or converted to real money.",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        // Coin Balance Hero Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldCoins.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("AVAILABLE LOYALTY COINS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldCoins)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "$coins", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = GoldCoins, modifier = Modifier.size(24.dp))
                        }
                        Text("Spendable on cosmetic items, badges and bonus tickets", fontSize = 10.sp, color = TextSecondary)
                    }

                    Button(
                        onClick = { onSpinWheelClick() },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldCoins.copy(alpha = 0.15f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldCoins.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Refresh, contentDescription = null, tint = GoldCoins, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("DAILY SPIN", fontSize = 9.sp, fontWeight = FontWeight.Black, color = GoldCoins)
                        }
                    }
                }
            }
        }

        // FIXED 7-DAY LOGIN STREAK (Compact layout: title, subtitle, D1-D7 chips in row, Claim button, progress line)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = NeonLime, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("7-DAY LOGIN STREAK", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                        Text(
                            text = if (hasCheckedInToday) "Checked in today!" else "Tap to claim today",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonLime
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // D1 to D7 chips in a single row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        streakDays.forEachIndexed { index, reward ->
                            val dayNum = index + 1
                            val isPast = dayNum < streakDay || (dayNum == streakDay && hasCheckedInToday)
                            val isCurrent = dayNum == streakDay && !hasCheckedInToday

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        when {
                                            isPast -> NeonLime.copy(alpha = 0.15f)
                                            isCurrent -> ElevatedSurface
                                            else -> BgDark
                                        }
                                    )
                                    .border(
                                        1.dp,
                                        when {
                                            isPast -> NeonLime.copy(alpha = 0.4f)
                                            isCurrent -> GoldCoins
                                            else -> CardBorder
                                        },
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("D$dayNum", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                                Spacer(modifier = Modifier.height(2.dp))
                                if (isPast) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonLime, modifier = Modifier.size(14.dp))
                                } else {
                                    Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = GoldCoins, modifier = Modifier.size(14.dp))
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("+$reward", fontSize = 9.sp, fontWeight = FontWeight.Black, color = if (isPast) NeonLime else Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Claim Today Button
                    Button(
                        onClick = { onDailyCheckIn() },
                        enabled = !hasCheckedInToday,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonLime,
                            disabledContainerColor = ElevatedSurface
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        if (hasCheckedInToday) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Streak Claimed Today (+40 Coins)", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Claim Day $streakDay Streak (+40 Coins)", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // Sub Tabs: Missions vs Store
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardSurface)
                    .padding(4.dp)
            ) {
                listOf("Missions", "Cosmetics Store").forEach { subTab ->
                    val isSelected = activeSubTab == subTab
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { activeSubTab = subTab },
                        color = if (isSelected) NeonLime else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = subTab,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                color = if (isSelected) Color.Black else TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Content: Missions list
        if (activeSubTab == "Missions") {
            items(missions) { mission ->
                val progress = (mission.current.toFloat() / mission.target).coerceIn(0f, 1f)
                val isComplete = mission.current >= mission.target

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(mission.type.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ElectricViolet)
                                Text(mission.title, fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                            }
                            Surface(
                                color = GoldCoins.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GoldCoins.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = GoldCoins, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("+${mission.rewardCoins}", fontSize = 11.sp, fontWeight = FontWeight.Black, color = GoldCoins)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(BgDark)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(progress)
                                            .fillMaxHeight()
                                            .background(NeonLime)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("${mission.current}/${mission.target}", fontSize = 10.sp, color = TextSecondary)
                            }

                            if (mission.isClaimed) {
                                Text("Claimed", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                            } else if (isComplete) {
                                Button(
                                    onClick = { onClaimMission(mission) },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text("Claim", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Black)
                                }
                            } else {
                                Text("In Progress", fontSize = 11.sp, color = TextSecondary)
                            }
                        }
                    }
                }
            }
        } else {
            // Cosmetics Store
            items(storeItems) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
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
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                color = ElevatedSurface,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = item.category.uppercase(),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonLime,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Text(item.description, fontSize = 11.sp, color = TextSecondary)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(horizontalAlignment = Alignment.End) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = GoldCoins, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("${item.coins}", fontSize = 13.sp, fontWeight = FontWeight.Black, color = GoldCoins)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = { onRedeemStoreItem(item) },
                                enabled = coins >= item.coins,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NeonLime,
                                    disabledContainerColor = ElevatedSurface
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text("Redeem", color = if (coins >= item.coins) Color.Black else TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}
