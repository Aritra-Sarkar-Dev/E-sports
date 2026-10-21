package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.components.FFArenaChip
import com.example.core.components.PrimaryButton
import com.example.core.config.AppConfig
import com.example.core.model.WalletTransaction
import com.example.ui.FFArenaViewModel
import com.example.ui.theme.AcidLime
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardShape
import com.example.ui.theme.CardSurface
import com.example.ui.theme.SecondaryViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun WalletScreen(
    viewModel: FFArenaViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentUser.collectAsState()
    var selectedAmount by remember { mutableDoubleStateOf(100.0) }
    var selectedUpiApp by remember { mutableStateOf("Google Pay") }

    val amountChips = listOf(50.0, 100.0, 250.0, 500.0)
    val upiOptions = listOf("PhonePe", "Google Pay", "Paytm", "BHIM UPI")

    val transactions = remember {
        listOf(
            WalletTransaction("tx_01", "ROG Titan Finals Prize", "Sponsor Disbursement #FF-902", 2500.0, true, "Yesterday, 08:15 PM"),
            WalletTransaction("tx_02", "Monster Energy Duo Brawl", "Podium 3rd Place Finish", 800.0, true, "3 Days Ago"),
            WalletTransaction("tx_03", "Daily Streak Sponsor Bonus", "Day 5 Login Reward", 25.0, true, "Today, 10:00 AM"),
            WalletTransaction("tx_04", "Withdrawal to UPI", "Transfer to Axis Bank Account", 1500.0, false, "5 Days Ago")
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("wallet_screen"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Esports Wallet",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
            }
        }

        // Balance Card
        item {
            Card(
                shape = CardShape,
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = BorderStroke(1.5.dp, SecondaryViolet),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TOTAL WALLET BALANCE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextMuted,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Box(
                            modifier = Modifier
                                .background(AcidLime.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "INR (₹)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AcidLime
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${AppConfig.CURRENCY_SYMBOL}${String.format("%.2f", user?.sponsorEarningsBalance ?: 3450.0)}",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = AcidLime
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Available for instant UPI withdrawal or sponsored tournament entries",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                    )
                }
            }
        }

        // Amount Chips
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "SELECT RECHARGE AMOUNT",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    amountChips.forEach { amt ->
                        val isSelected = selectedAmount == amt
                        FFArenaChip(
                            label = "${AppConfig.CURRENCY_SYMBOL}${amt.toInt()}",
                            isSelected = isSelected,
                            onClick = { selectedAmount = amt },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // UPI Options (PhonePe, Google Pay, Paytm, BHIM UPI)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "UPI PAYMENT METHOD",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = CardShape,
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    border = BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        upiOptions.forEachIndexed { index, appName ->
                            val isSelected = selectedUpiApp == appName
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedUpiApp = appName }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) AcidLime.copy(alpha = 0.2f) else Color(0xFF1F1F30)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Payment,
                                            contentDescription = null,
                                            tint = if (isSelected) AcidLime else SecondaryViolet,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = appName,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) AcidLime else TextPrimary
                                        )
                                    )
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = AcidLime,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            if (index < upiOptions.size - 1) {
                                Divider(color = CardBorder, thickness = 0.8.dp)
                            }
                        }
                    }
                }
            }
        }

        // Deposit CTA Button
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                PrimaryButton(
                    text = "DEPOSIT ${AppConfig.CURRENCY_SYMBOL}${selectedAmount.toInt()} VIA $selectedUpiApp",
                    onClick = {
                        viewModel.showSnackbar("Simulated UPI intent launched for $selectedUpiApp (${AppConfig.CURRENCY_SYMBOL}${selectedAmount.toInt()})")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    testTag = "wallet_deposit_btn"
                )
            }
        }

        // Transactions Header
        item {
            Text(
                text = "RECENT TRANSACTIONS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                ),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 6.dp)
            )
        }

        // Transaction list
        items(transactions, key = { it.id }) { tx ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = tx.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "${tx.subtitle} • ${tx.dateFormatted}",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                        )
                    }

                    Text(
                        text = if (tx.isCredit) "+${AppConfig.CURRENCY_SYMBOL}${tx.amount.toInt()}" else "-${AppConfig.CURRENCY_SYMBOL}${tx.amount.toInt()}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if (tx.isCredit) AcidLime else Color(0xFFFF7A8A)
                        )
                    )
                }
            }
        }
    }
}
