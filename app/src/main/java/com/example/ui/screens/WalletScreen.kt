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
import com.example.ui.model.TransactionRecord
import com.example.ui.model.UserProfile
import com.example.ui.model.WalletInfo
import com.example.ui.theme.*

@Composable
fun WalletScreen(
    wallet: WalletInfo,
    profile: UserProfile,
    transactions: List<TransactionRecord>,
    onAddMoneyClick: () -> Unit,
    onWithdrawClick: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filterTabs = listOf("All", "Deposits", "Winnings", "Entry Fees", "Withdrawals")

    val filteredTransactions = remember(transactions, selectedFilter) {
        when (selectedFilter) {
            "Deposits" -> transactions.filter { it.type == "deposit" }
            "Winnings" -> transactions.filter { it.type == "winning" }
            "Entry Fees" -> transactions.filter { it.type == "entry_fee" }
            "Withdrawals" -> transactions.filter { it.type == "withdrawal" }
            else -> transactions
        }
    }

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
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "WALLET & PAYOUTS",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Fast 100% Secure Instant UPI Withdrawals",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
        }

        // 3-Pool Gradient Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(
                        1.dp,
                        Brush.linearGradient(listOf(NeonLime.copy(alpha = 0.5f), ElectricViolet.copy(alpha = 0.3f))),
                        RoundedCornerShape(24.dp)
                    )
                    .testTag("wallet_balance_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF1E1E34),
                                    CardSurface
                                )
                            )
                        )
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Total Balance Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("TOTAL WALLET BALANCE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                            Text(
                                text = "₹${wallet.total}",
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Black,
                                color = NeonLime
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(NeonLime.copy(alpha = 0.15f))
                                .border(1.dp, NeonLime.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("100% Safe", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonLime)
                        }
                    }

                    // 3 Separated Pools
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(ChipBg)
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Deposit Balance
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Deposit", fontSize = 11.sp, color = TextMuted)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("₹${wallet.deposit}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text("For joins", fontSize = 9.sp, color = TextMuted)
                        }

                        // Divider
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(CardBorder)
                        )

                        // Winnings
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                        ) {
                            Text("Winnings", fontSize = 11.sp, color = TextMuted)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("₹${wallet.winnings}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = GoldPrize)
                            Text("Withdrawable", fontSize = 9.sp, color = GoldPrize)
                        }

                        // Divider
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(CardBorder)
                        )

                        // Bonus
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                            Text("Bonus", fontSize = 11.sp, color = TextMuted)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("₹${wallet.bonus}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ElectricViolet)
                            Text("Up to 10% used", fontSize = 9.sp, color = TextMuted)
                        }
                    }

                    // Action Buttons (Add Money & Withdraw)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onAddMoneyClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("wallet_add_money_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonLime,
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Add Money", fontSize = 14.sp, fontWeight = FontWeight.Black)
                        }

                        OutlinedButton(
                            onClick = onWithdrawClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("wallet_withdraw_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = TextPrimary
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = Brush.horizontalGradient(listOf(GoldPrize, FireOrange))
                            )
                        ) {
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = GoldPrize, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Withdraw", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldPrize)
                        }
                    }
                }
            }
        }

        // KYC Status Banner
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (profile.isKycVerified) NeonLime.copy(alpha = 0.12f) else FireOrange.copy(alpha = 0.12f)
                    )
                    .border(
                        1.dp,
                        if (profile.isKycVerified) NeonLime.copy(alpha = 0.3f) else FireOrange.copy(alpha = 0.3f),
                        RoundedCornerShape(14.dp)
                    )
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = if (profile.isKycVerified) Icons.Default.Verified else Icons.Default.Info,
                    contentDescription = null,
                    tint = if (profile.isKycVerified) NeonLime else FireOrange,
                    modifier = Modifier.size(24.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (profile.isKycVerified) "KYC Verified • Instant Payouts Active" else "KYC Pending • Verification Required",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = if (profile.isKycVerified) "Linked UPI: ${profile.linkedUpi} (Max ₹10,000/day)" else "Complete KYC to enable 1-click withdrawals above ₹500.",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
        }

        // Quick UPI Apps Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(CardBorder, CardBorder)))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "SUPPORTED UPI APPS FOR 0% FEE DEPOSITS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        listOf("PhonePe", "Google Pay", "Paytm", "BHIM UPI").forEach { appName ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(ChipBg)
                                        .border(1.dp, CardBorder, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Bolt, contentDescription = null, tint = NeonLime, modifier = Modifier.size(18.dp))
                                }
                                Text(appName, fontSize = 10.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }

        // Transactions Header & Filters
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "TRANSACTION HISTORY",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = 0.5.sp
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filterTabs) { tab ->
                        val isSel = selectedFilter == tab
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) NeonLime else ChipBg)
                                .border(1.dp, if (isSel) NeonLime else CardBorder, RoundedCornerShape(10.dp))
                                .clickable { selectedFilter = tab }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = tab,
                                fontSize = 12.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) Color.Black else TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Empty Transactions
        if (filteredTransactions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No transactions found in this category.", fontSize = 13.sp, color = TextMuted)
                }
            }
        }

        // Transactions List
        items(filteredTransactions, key = { it.id }) { tx ->
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                if (tx.isPositive) NeonLime.copy(alpha = 0.15f) else FireOrange.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (tx.type) {
                                "winning" -> Icons.Default.EmojiEvents
                                "deposit" -> Icons.Default.ArrowDownward
                                "withdrawal" -> Icons.Default.ArrowUpward
                                "bonus" -> Icons.Default.CardGiftcard
                                else -> Icons.Default.SportsEsports
                            },
                            contentDescription = null,
                            tint = if (tx.isPositive) NeonLime else FireOrange,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = tx.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = tx.timeText,
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                Text(
                    text = "${if (tx.isPositive) "+" else "-"}₹${tx.amount}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = if (tx.isPositive) NeonLime else TextPrimary
                )
            }
        }
    }
}
