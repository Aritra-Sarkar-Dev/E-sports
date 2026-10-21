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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.components.InputField
import com.example.core.components.OutlineButton
import com.example.core.components.PrimaryButton
import com.example.ui.AuthScreen
import com.example.ui.FFArenaViewModel
import com.example.ui.theme.AcidLime
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.DeepPurpleEnd
import com.example.ui.theme.DeepPurpleStart
import com.example.ui.theme.SecondaryViolet
import com.example.ui.theme.SoraFontFamily
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun LoginScreen(
    viewModel: FFArenaViewModel,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var identifier by remember { mutableStateOf("arjun.esports@arena.in") }
    var password by remember { mutableStateOf("pass1234") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showOtpDialog by remember { mutableStateOf(false) }
    var otpPhone by remember { mutableStateOf("+91 98765 43210") }
    var otpCode by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // 1. Logo mark: Gradient rounded square "FF"
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(AcidLime, SecondaryViolet, DeepPurpleStart)
                    )
                )
                .border(2.dp, AcidLime.copy(alpha = 0.5f), RoundedCornerShape(22.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "FF",
                style = TextStyle(
                    fontFamily = SoraFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 34.sp,
                    letterSpacing = (-1).sp,
                    color = Color(0xFF0A0A0F)
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Headline
        Text(
            text = "Welcome back, champion.",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Log in to join verified Free Fire esports tournaments",
            style = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center,
                color = TextMuted
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Email / Phone Field
        InputField(
            label = "EMAIL OR PHONE NUMBER",
            value = identifier,
            onValueChange = {
                identifier = it
                errorMessage = null
            },
            placeholder = "e.g. player@gmail.com or +91 98765...",
            leadingIcon = Icons.Default.Email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            testTag = "login_email_input"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        InputField(
            label = "PASSWORD",
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            placeholder = "••••••••",
            leadingIcon = Icons.Default.Lock,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password visibility",
                        tint = TextMuted
                    )
                }
            },
            isError = errorMessage != null,
            errorMessage = errorMessage,
            testTag = "login_password_input"
        )

        // Forgot password
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(
                onClick = {
                    viewModel.showSnackbar("Password reset link sent to your registered email.")
                }
            ) {
                Text(
                    text = "Forgot password?",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = SecondaryViolet,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Lime Log In Button
        PrimaryButton(
            text = "LOG IN",
            onClick = {
                viewModel.login(
                    id = identifier,
                    pass = password,
                    onSuccess = onLoginSuccess,
                    onError = { errorMessage = it }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            testTag = "login_submit_btn"
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Divider "or"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(modifier = Modifier.weight(1f), color = CardBorder)
            Text(
                text = "OR",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                ),
                modifier = Modifier.padding(horizontal = 14.dp)
            )
            Divider(modifier = Modifier.weight(1f), color = CardBorder)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Continue with Google
        OutlineButton(
            text = "Continue with Google",
            onClick = {
                viewModel.loginWithGoogle(onSuccess = onLoginSuccess)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            borderColor = CardBorder,
            textColor = TextPrimary,
            testTag = "login_google_btn"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Phone OTP Option
        OutlineButton(
            text = "Log In with Phone OTP",
            onClick = { showOtpDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            borderColor = CardBorder,
            textColor = SecondaryViolet,
            leadingIcon = Icons.Default.PhoneAndroid,
            testTag = "login_otp_btn"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // New here? Create account
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text(
                text = "New here? ",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
            )
            Text(
                text = "Create account",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = AcidLime
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { viewModel.navigateAuth(AuthScreen.SIGN_UP) }
                    .padding(4.dp)
                    .testTag("nav_to_signup_btn")
            )
        }
    }

    // Phone OTP Quick Dialog
    if (showOtpDialog) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showOtpDialog = false }) {
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = CardSurface,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, CardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Phone OTP Verification",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "We will send a 6-digit code via SMS to verify your Indian mobile number.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            textAlign = TextAlign.Center,
                            color = TextMuted
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    InputField(
                        label = "MOBILE NUMBER",
                        value = otpPhone,
                        onValueChange = { otpPhone = it },
                        placeholder = "+91 98765 43210",
                        leadingIcon = Icons.Default.PhoneAndroid
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    InputField(
                        label = "ENTER OTP (Simulated: 7788)",
                        value = otpCode,
                        onValueChange = { otpCode = it },
                        placeholder = "7788",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    PrimaryButton(
                        text = "VERIFY & ENTER",
                        onClick = {
                            showOtpDialog = false
                            viewModel.login(
                                id = otpPhone,
                                pass = "otp_verified",
                                onSuccess = onLoginSuccess,
                                onError = {}
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
