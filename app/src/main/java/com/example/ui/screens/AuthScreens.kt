package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.model.AuthService
import com.example.ui.model.UserProfile
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        delay(2200)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(CardSurface)
                    .border(3.dp, NeonLime, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SportsEsports,
                    contentDescription = null,
                    tint = NeonLime,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "FF ARENA",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                color = NeonLime.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonLime.copy(alpha = 0.5f))
            ) {
                Text(
                    text = "100% FREE ENTRY • SPONSOR-FUNDED ESPORTS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonLime,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun OnboardingFlow(
    onFinish: () -> Unit
) {
    var step by remember { mutableIntStateOf(0) }
    val pages = listOf(
        Triple("Zero Entry Fee", "100% free-to-play esports tournaments. Strictly no entry fees, deposits, or wagers.", Icons.Default.Verified),
        Triple("Compete with Squads", "Join Battle Royale and Clash Squad rooms. Room ID & password reveal 15 minutes before kick-off.", Icons.Default.Group),
        Triple("Win Sponsor Prizes", "Exclusive gaming kits, headsets, and sponsor trophies delivered directly to winners.", Icons.Default.EmojiEvents)
    )

    val current = pages[step]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(ElevatedSurface)
                    .border(2.dp, NeonLime, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = current.third,
                    contentDescription = null,
                    tint = NeonLime,
                    modifier = Modifier.size(52.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = current.first,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = current.second,
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Step Indicator Dots
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                pages.indices.forEach { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == step) 24.dp else 8.dp, 8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (index == step) NeonLime else CardBorder)
                    )
                }
            }
        }

        // Bottom Actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onFinish) {
                Text("Skip", color = TextSecondary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    if (step < pages.size - 1) {
                        step++
                    } else {
                        onFinish()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = if (step == pages.size - 1) "Get Started" else "Next",
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: (UserProfile) -> Unit,
    onNavigateToSignUp: () -> Unit,
    onOpenOrganizerLogin: () -> Unit,
    onShowToast: (String) -> Unit
) {
    var email by remember { mutableStateOf("aritra@ffarena.com") }
    var password by remember { mutableStateOf("neon99") }
    var showForgotDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.SportsEsports,
            contentDescription = null,
            tint = NeonLime,
            modifier = Modifier.size(54.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Welcome to FF Arena",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
        Text(
            text = "Free Fire MAX Esports Tournaments",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Demo Accounts Fill
        Text(
            text = "TAP TO FILL DEMO CREDENTIALS:",
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            color = NeonLime,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(
                modifier = Modifier.clickable {
                    email = "aritra@ffarena.com"
                    password = "neon99"
                },
                color = CardSurface,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonLime.copy(alpha = 0.5f))
            ) {
                Text("Player (Aritra)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonLime, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
            }

            Surface(
                modifier = Modifier.clickable {
                    email = "organizer@demo.com"
                    password = "demo123"
                },
                color = CardSurface,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet.copy(alpha = 0.5f))
            ) {
                Text("Organizer Demo", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ElectricViolet, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
            }

            Surface(
                modifier = Modifier.clickable {
                    email = "admin@demo.com"
                    password = "admin123"
                },
                color = CardSurface,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldCoins.copy(alpha = 0.5f))
            ) {
                Text("Admin Demo", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldCoins, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = TextSecondary) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonLime,
                unfocusedBorderColor = CardBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = TextSecondary) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonLime,
                unfocusedBorderColor = CardBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Forgot Password?",
                fontSize = 11.sp,
                color = ElectricViolet,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { showForgotDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val res = AuthService.login(email, password)
                res.onSuccess { user ->
                    onShowToast("Logged in as ${user.name} (${user.role.uppercase()})")
                    onLoginSuccess(user)
                }.onFailure { err ->
                    onShowToast(err.message ?: "Login failed")
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Login", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Don't have an account? ", color = TextSecondary, fontSize = 12.sp)
            Text(
                text = "Sign Up Free",
                color = NeonLime,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToSignUp() }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Organizer Portal Link
        Surface(
            modifier = Modifier.clickable { onOpenOrganizerLogin() },
            color = CardSurface,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = NeonLime, modifier = Modifier.size(18.dp))
                Text("Organizer & Admin Login", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showForgotDialog) {
        Dialog(onDismissRequest = { showForgotDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Password Recovery", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Enter your email address to receive password reset instructions.", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showForgotDialog = false }) {
                            Text("Cancel", color = TextSecondary)
                        }
                        Button(
                            onClick = {
                                showForgotDialog = false
                                onShowToast("Reset link dispatched to $email!")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonLime)
                        ) {
                            Text("Send Link", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SignUpScreen(
    onSignUpSuccess: (UserProfile) -> Unit,
    onNavigateToLogin: () -> Unit,
    onShowToast: (String) -> Unit
) {
    var name by remember { mutableStateOf("Aritra Sarkar") }
    var email by remember { mutableStateOf("aritra@ffarena.com") }
    var password by remember { mutableStateOf("neon99") }
    var ign by remember { mutableStateOf("NEON_SOUL") }
    var ffUid by remember { mutableStateOf("994821034") }
    var is18Plus by remember { mutableStateOf(true) }
    var acceptTerms by remember { mutableStateOf(true) }

    val isUidValid = remember(ffUid) { ffUid.matches(Regex("^[0-9]{8,12}$")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text("Create Player Profile", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color.White)
        Text("100% Free Entry • Sponsor-Funded Prizes", fontSize = 12.sp, color = NeonLime, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password (min 6 chars)") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = ign,
            onValueChange = { ign = it },
            label = { Text("Free Fire In-Game Name (IGN)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = ffUid,
            onValueChange = { ffUid = it },
            label = { Text("Free Fire UID (8-12 digits)") },
            supportingText = {
                Text(
                    text = if (isUidValid) "✓ Valid Free Fire UID format" else "Must be 8-12 numeric digits",
                    color = if (isUidValid) NeonLime else LiveRed,
                    fontSize = 11.sp
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 18+ Checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { is18Plus = !is18Plus }
        ) {
            Checkbox(
                checked = is18Plus,
                onCheckedChange = { is18Plus = it },
                colors = CheckboxDefaults.colors(checkedColor = NeonLime, checkmarkColor = Color.Black)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("I confirm that I am 18 years of age or older", fontSize = 12.sp, color = Color.White)
        }

        // Terms Checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { acceptTerms = !acceptTerms }
        ) {
            Checkbox(
                checked = acceptTerms,
                onCheckedChange = { acceptTerms = it },
                colors = CheckboxDefaults.colors(checkedColor = NeonLime, checkmarkColor = Color.Black)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("I agree to Fair Play Anti-Cheat & Esports Terms", fontSize = 12.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val res = AuthService.register(name, email, password, ign, ffUid, is18Plus, acceptTerms)
                res.onSuccess { user ->
                    onShowToast("Account created! Welcome, ${user.ign}!")
                    onSignUpSuccess(user)
                }.onFailure { err ->
                    onShowToast(err.message ?: "Registration error")
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = NeonLime),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Sign Up & Join Arena", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Already registered? ", color = TextSecondary, fontSize = 12.sp)
            Text(
                text = "Log In",
                color = NeonLime,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
