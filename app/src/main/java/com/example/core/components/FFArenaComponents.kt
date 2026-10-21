package com.example.core.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.config.AppConfig
import com.example.core.model.DivisionTier
import com.example.core.model.GameMode
import com.example.core.model.Tournament
import com.example.ui.theme.AcidLime
import com.example.ui.theme.ButtonShape
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardShape
import com.example.ui.theme.CardSurface
import com.example.ui.theme.ChipSelectedBg
import com.example.ui.theme.ChipSelectedText
import com.example.ui.theme.ChipShape
import com.example.ui.theme.ErrorDanger
import com.example.ui.theme.HeroCardGradient
import com.example.ui.theme.HeroCardShape
import com.example.ui.theme.InputBorder
import com.example.ui.theme.InputShape
import com.example.ui.theme.SecondaryViolet
import com.example.ui.theme.SoraFontFamily
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

// 1. PrimaryButton (acid lime, 18px radius, bold text, press bounce animation)
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    testTag: String = "primary_button"
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1.0f,
        animationSpec = tween(150),
        label = "button_scale"
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = ButtonShape,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = AcidLime,
            contentColor = Color(0xFF0A0A0F),
            disabledContainerColor = AcidLime.copy(alpha = 0.35f),
            disabledContentColor = Color(0xFF0A0A0F).copy(alpha = 0.6f)
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp),
        modifier = modifier
            .scale(scale)
            .defaultMinSize(minHeight = 48.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFF0A0A0F)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0A0A0F),
                    letterSpacing = 0.2.sp
                )
            )
        }
    }
}

// 2. GhostButton
@Composable
fun GhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = SecondaryViolet,
    testTag: String = "ghost_button"
) {
    TextButton(
        onClick = onClick,
        shape = ButtonShape,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        modifier = modifier
            .defaultMinSize(minHeight = 44.dp)
            .testTag(testTag)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = color
            )
        )
    }
}

// 3. OutlineButton
@Composable
fun OutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = CardBorder,
    textColor: Color = TextPrimary,
    leadingIcon: ImageVector? = null,
    testTag: String = "outline_button"
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1.0f,
        animationSpec = tween(150),
        label = "outline_scale"
    )

    OutlinedButton(
        onClick = onClick,
        shape = ButtonShape,
        interactionSource = interactionSource,
        border = BorderStroke(1.5.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = textColor
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        modifier = modifier
            .scale(scale)
            .defaultMinSize(minHeight = 46.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = textColor
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            )
        }
    }
}

// 4. Chip (selected = lime fill, unselected = card surface with border, fully rounded)
@Composable
fun FFArenaChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeCount: Int? = null,
    testTag: String = "chip_$label"
) {
    val bg = if (isSelected) ChipSelectedBg else CardSurface
    val textCol = if (isSelected) ChipSelectedText else TextPrimary
    val borderStroke = if (isSelected) null else BorderStroke(1.dp, CardBorder)

    Surface(
        onClick = onClick,
        shape = ChipShape,
        color = bg,
        border = borderStroke,
        modifier = modifier
            .defaultMinSize(minHeight = 44.dp)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                    color = textCol
                )
            )
            if (badgeCount != null && badgeCount > 0) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(
                            if (isSelected) Color(0xFF0A0A0F).copy(alpha = 0.2f) else SecondaryViolet,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badgeCount.toString(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color(0xFF0A0A0F) else Color.White
                    )
                }
            }
        }
    }
}

// 5. InputField (label above, 52px high, card surface, input border)
@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    testTag: String = "input_field"
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = TextMuted
            ),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        val borderColor = when {
            isError -> ErrorDanger
            value.isNotEmpty() -> SecondaryViolet
            else -> InputBorder
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(CardSurface, InputShape)
                .border(1.5.dp, borderColor, InputShape)
                .padding(horizontal = 16.dp)
                .testTag(testTag),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = if (isError) ErrorDanger else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }

                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty() && placeholder.isNotEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextMuted.copy(alpha = 0.6f)
                            )
                        )
                    }
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Medium
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(AcidLime),
                        visualTransformation = visualTransformation,
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions
                    )
                }

                if (trailingIcon != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    trailingIcon()
                }
            }
        }

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelSmall.copy(color = ErrorDanger),
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}

// 6. SectionTitle (Big bold headline with optional action)
@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
        )
        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = AcidLime
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onActionClick)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

// 7. StatTile (reusable for profile & match summaries)
@Composable
fun StatTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    accentColor: Color = AcidLime,
    subText: String? = null
) {
    Card(
        modifier = modifier,
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = accentColor
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextMuted
                )
            )
            if (subText != null) {
                Text(
                    text = subText,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        color = TextMuted.copy(alpha = 0.7f)
                    )
                )
            }
        }
    }
}

// 8. TournamentCard (Card surface #14141F, border #23233A, 22px radius, mode & division badges, prize pool in lime)
@Composable
fun TournamentCard(
    tournament: Tournament,
    onCardClick: () -> Unit,
    onJoinClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onCardClick,
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.2.dp, CardBorder),
        modifier = modifier
            .fillMaxWidth()
            .testTag("tournament_card_${tournament.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Badges row: Division badge + Mode chip + Start time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DivisionBadge(division = tournament.division)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(SecondaryViolet.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .border(1.dp, SecondaryViolet.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = tournament.mode.label.uppercase(),
                            style = TextStyle(
                                fontFamily = SoraFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = SecondaryViolet
                            )
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = TextMuted
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = tournament.startTimeFormatted,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = TextMuted
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title & Map
            Text(
                text = tournament.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                ),
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Map: ${tournament.mapName} • Sponsored by ${tournament.sponsor}",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Slots Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${tournament.joinedSlots}/${tournament.totalSlots} Players",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "${tournament.spotsLeft} spots left",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = if (tournament.spotsLeft <= 5) ErrorDanger else AcidLime
                    )
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { tournament.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (tournament.progress > 0.85f) ErrorDanger else AcidLime,
                trackColor = CardBorder,
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom bar: Prize Pool & Entry Fee + Join Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "PRIZE POOL",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                    )
                    Text(
                        text = "${AppConfig.CURRENCY_SYMBOL}${tournament.prizePool}",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = AcidLime
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ENTRY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                    )
                    Text(
                        text = if (AppConfig.enablePaidEntry && tournament.entryFee > 0) {
                            "${AppConfig.CURRENCY_SYMBOL}${tournament.entryFee}"
                        } else {
                            "FREE"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (!AppConfig.enablePaidEntry || tournament.entryFee == 0) AcidLime else TextPrimary
                        )
                    )
                }

                if (tournament.isJoined) {
                    OutlineButton(
                        text = "JOINED",
                        onClick = onCardClick,
                        borderColor = AcidLime,
                        textColor = AcidLime,
                        leadingIcon = Icons.Default.Check,
                        modifier = Modifier.height(44.dp),
                        testTag = "joined_btn_${tournament.id}"
                    )
                } else {
                    PrimaryButton(
                        text = "JOIN",
                        onClick = onJoinClick,
                        modifier = Modifier.height(44.dp),
                        testTag = "join_btn_${tournament.id}"
                    )
                }
            }
        }
    }
}

// Division Badge Component (ROOKIE, MID, PRO, OPEN)
@Composable
fun DivisionBadge(division: DivisionTier) {
    val (badgeBg, badgeBorder, badgeText) = when (division) {
        DivisionTier.ROOKIE -> Triple(Color(0xFF00E5FF).copy(alpha = 0.15f), Color(0xFF00E5FF), Color(0xFF00E5FF))
        DivisionTier.MID -> Triple(Color(0xFF7B5CFF).copy(alpha = 0.15f), Color(0xFF7B5CFF), Color(0xFFB39DDB))
        DivisionTier.PRO -> Triple(Color(0xFFFFB300).copy(alpha = 0.15f), Color(0xFFFFB300), Color(0xFFFFCA28))
        DivisionTier.OPEN -> Triple(AcidLime.copy(alpha = 0.15f), AcidLime, AcidLime)
    }

    Box(
        modifier = Modifier
            .background(badgeBg, RoundedCornerShape(8.dp))
            .border(1.dp, badgeBorder.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = division.label,
            style = TextStyle(
                fontFamily = SoraFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                letterSpacing = 0.4.sp,
                color = badgeText
            )
        )
    }
}
