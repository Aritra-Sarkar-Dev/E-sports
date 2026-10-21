package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.components.InputField
import com.example.core.components.PrimaryButton
import com.example.ui.AuthScreen
import com.example.ui.FFArenaViewModel
import com.example.ui.theme.AcidLime
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.ErrorDanger
import com.example.ui.theme.SecondaryViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun SignUpScreen(
    viewModel: FFArenaViewModel,
    onSignUpSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var ffUid by remember { mutableStateOf("") }
    var inGameName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isAgreed by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        // Shield Icon Header
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(CardSurface)
                .border(1.5.dp, SecondaryViolet, RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = AcidLime,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Create Fighter Account",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Join India's premiere Free Fire esports community",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(28.dp))

        // 1. Free Fire UID
        InputField(
            label = "FREE FIRE UID",
            value = ffUid,
            onValueChange = {
                ffUid = it
                errorMessage = null
            },
            placeholder = "e.g. 839472819 (Found on profile banner)",
            leadingIcon = Icons.Default.Badge,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            testTag = "signup_uid_input"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 2. In-Game Name (IGN)
        InputField(
            label = "IN-GAME NAME (IGN)",
            value = inGameName,
            onValueChange = {
                inGameName = it
                errorMessage = null
            },
            placeholder = "e.g. ⚡ViperSniper⚡",
            leadingIcon = Icons.Default.Person,
            testTag = "signup_ign_input"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Mobile Phone
        InputField(
            label = "WHATSAPP / PHONE NUMBER",
            value = phone,
            onValueChange = {
                phone = it
                errorMessage = null
            },
            placeholder = "+91 98765 43210",
            leadingIcon = Icons.Default.Phone,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            testTag = "signup_phone_input"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 4. Password
        InputField(
            label = "PASSWORD",
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            placeholder = "At least 6 characters",
            leadingIcon = Icons.Default.Lock,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            testTag = "signup_password_input"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Checkbox: "I am 18 or older and agree to the Terms and Fair Play Policy"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable { isAgreed = !isAgreed }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isAgreed,
                onCheckedChange = { isAgreed = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = AcidLime,
                    checkmarkColor = Color(0xFF0A0A0F),
                    uncheckedColor = CardBorder
                ),
                modifier = Modifier.testTag("signup_terms_checkbox")
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "I am 18 or older and agree to the Terms, Privacy, and Esports Fair Play Policy.",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    color = if (isAgreed) TextPrimary else TextMuted,
                    lineHeight = 16.sp
                )
            )
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage ?: "",
                style = MaterialTheme.typography.bodySmall.copy(color = ErrorDanger),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Sign Up button - BLOCKED unless checkbox is checked!
        PrimaryButton(
            text = "CREATE ACCOUNT",
            enabled = isAgreed && ffUid.isNotBlank() && inGameName.isNotBlank() && phone.isNotBlank(),
            onClick = {
                if (!isAgreed) {
                    errorMessage = "You must confirm you are 18+ and accept the Fair Play policy"
                    return@PrimaryButton
                }
                viewModel.signUp(
                    ffUid = ffUid,
                    ign = inGameName,
                    phone = phone,
                    pass = password,
                    onSuccess = onSignUpSuccess,
                    onError = { errorMessage = it }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            testTag = "signup_submit_btn"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Already have an account? Log in
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 36.dp)
        ) {
            Text(
                text = "Already have an account? ",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
            )
            Text(
                text = "Log in",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = AcidLime
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { viewModel.navigateAuth(AuthScreen.LOGIN) }
                    .padding(4.dp)
                    .testTag("nav_to_login_btn")
            )
        }
    }
}
