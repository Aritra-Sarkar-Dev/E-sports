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
import androidx.compose.ui.platform.testTag
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
    val weekDays = listOf(
        1 to 20,
        2 to 30,
        3 to 40,
        4 to 50,
        5 to 60,
        6 to 80,
        7 to 200 // Mega Day 7
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
    ) {
        // Top Title
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "REWARDS & COINS",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Earn Free Coins, Unlock Frames & Free Custom Room Tickets",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
        }

        // Coin Hero Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(
                        1.dp,
                        Brush.linearGradient(listOf(GoldPrize.copy(alpha = 0.6f), ElectricViolet.copy(alpha = 0.3f))),
                        RoundedCornerShape(24.dp)
                    )
                    .testTag("rewards_coin_hero"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF262013), CardSurface)
                            )
                        )
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("YOUR COIN BALANCE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = GoldPrize, modifier = Modifier.size(32.dp))
                                Text(
                                    text = "$coins",
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Black,
                                    color = GoldPrize
                                )
                                Text("COINS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                            }
                        }

                        Button(
                            onClick = onSpinWheelClick,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FireOrange,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.testTag("rewards_spin_now_button")
                        ) {
                            Icon(Icons.Default.RotateRight, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Spin Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Disclaimer notice required by prompt
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ChipBg)
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "ℹ Rewards coins are non-cash loyalty points and cannot be bought or converted to cash. Use them to redeem profile cosmetics and tournament tickets.",
                            fontSize = 10.sp,
                            color = TextMuted,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }

        // 7-Day Streak Calendar
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(CardBorder, CardBorder)))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("7-DAY LOGIN STREAK", fontSize = 14.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                            Text("Check-in daily without missing to claim Day 7 Mega Chest!", fontSize = 11.sp, color = TextMuted)
                        }

                        Button(
                            onClick = onDailyCheckIn,
                            enabled = !hasCheckedInToday,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonLime,
                                contentColor = Color.Black,
                                disabledContainerColor = ChipBg,
                                disabledContentColor = TextMuted
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("streak_claim_button")
                        ) {
                            Text(
                                if (hasCheckedInToday) "Claimed ✓" else "Claim Today",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // 7 Days Row
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(weekDays) { (day, reward) ->
                            val isPast = day < streakDay || (day == streakDay && hasCheckedInToday)
                            val isCurrent = day == streakDay && !hasCheckedInToday
                            val isDay7 = day == 7

                            Column(
                                modifier = Modifier
                                    .width(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        when {
                                            isPast -> NeonLime.copy(alpha = 0.15f)
                                            isCurrent -> FireOrange.copy(alpha = 0.2f)
                                            isDay7 -> GoldPrize.copy(alpha = 0.15f)
                                            else -> ChipBg
                                        }
                                    )
                                    .border(
                                        1.dp,
                                        when {
                                            isPast -> NeonLime
                                            isCurrent -> FireOrange
                                            isDay7 -> GoldPrize
                                            else -> CardBorder
                                        },
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "D$day",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPast) NeonLime else TextMuted
                                )

                                Icon(
                                    imageVector = if (isDay7) Icons.Default.CardGiftcard else Icons.Default.MonetizationOn,
                                    contentDescription = null,
                                    tint = if (isPast) NeonLime else if (isDay7) GoldPrize else TextPrimary,
                                    modifier = Modifier.size(18.dp)
                                )

                                Text(
                                    text = "+$reward",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPast) NeonLime else TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Daily Missions
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "DAILY MISSIONS",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = 0.5.sp
                )

                missions.forEach { mission ->
                    val isComplete = mission.current >= mission.target
                    val progress = (mission.current.toFloat() / mission.target.toFloat()).coerceIn(0f, 1f)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(CardSurface)
                            .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(mission.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(GoldPrize.copy(alpha = 0.15f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("+${mission.rewardCoins} Coins", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldPrize)
                                }
                            }

                            // Progress bar
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(ChipBg)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(progress)
                                            .fillMaxHeight()
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(if (isComplete) NeonLime else ElectricViolet)
                                    )
                                }
                                Text("${mission.current}/${mission.target}", fontSize = 11.sp, color = TextMuted)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = { onClaimMission(mission) },
                            enabled = isComplete && !mission.isClaimed,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonLime,
                                contentColor = Color.Black,
                                disabledContainerColor = ChipBg,
                                disabledContentColor = TextMuted
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("claim_mission_${mission.id}")
                        ) {
                            Text(
                                when {
                                    mission.isClaimed -> "Claimed"
                                    isComplete -> "Claim"
                                    else -> "In Progress"
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Coin Store
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "COIN STORE (REDEEM PRIZES)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = 0.5.sp
                )

                storeItems.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { item ->
                            val canAfford = coins >= item.coins

                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                                    .testTag("store_item_${item.id}"),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = CardSurface)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(56.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(ChipBg),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = when (item.category) {
                                                "Ticket" -> Icons.Default.ConfirmationNumber
                                                "Frame" -> Icons.Default.AccountBox
                                                "Badge" -> Icons.Default.MilitaryTech
                                                else -> Icons.Default.Palette
                                            },
                                            contentDescription = null,
                                            tint = when (item.category) {
                                                "Ticket" -> NeonLime
                                                "Frame" -> ElectricViolet
                                                "Badge" -> GoldPrize
                                                else -> FireOrange
                                            },
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }

                                    Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text(item.description, fontSize = 10.sp, color = TextMuted, minLines = 2, maxLines = 2)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("${item.coins} Coins", fontSize = 12.sp, fontWeight = FontWeight.Black, color = GoldPrize)

                                        Button(
                                            onClick = { onRedeemStoreItem(item) },
                                            enabled = canAfford,
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (canAfford) NeonLime else ChipBg,
                                                contentColor = Color.Black,
                                                disabledContainerColor = ChipBg,
                                                disabledContentColor = TextMuted
                                            ),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                            modifier = Modifier.height(30.dp)
                                        ) {
                                            Text(if (canAfford) "Redeem" else "Locked", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        // Fill empty slot if odd
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
