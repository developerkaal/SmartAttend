package com.smartattend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartattend.ui.theme.SmartAttendHeroGradientEnd
import com.smartattend.ui.theme.SmartAttendHeroGradientStart
import com.smartattend.ui.theme.SmartAttendMutedForeground
import com.smartattend.ui.theme.SmartAttendPrimary
import com.smartattend.ui.theme.SmartAttendPrimaryForeground
import com.smartattend.ui.theme.SmartAttendPrimaryGradientEnd
import com.smartattend.ui.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.smartattend.ui.viewmodel.UiEvent

@Composable
fun LoginScreen(
    onNavigateBack: () -> Unit,
    onNavigateToApp: () -> Unit,
) {
    val viewModel: AuthViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val bannerMessage = remember { mutableStateOf<String?>(null) }
    val bannerIsError = remember { mutableStateOf(false) }
    val fullName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val showPassword = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            val message = when (event) {
                is UiEvent.Success -> event.message
                is UiEvent.Error -> event.message
                is UiEvent.Offline -> event.message
            }
            bannerMessage.value = message
            bannerIsError.value = event is UiEvent.Error
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(WindowInsets.statusBars.asPaddingValues()),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
            bannerMessage.value?.let { message ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (bannerIsError.value) {
                            MaterialTheme.colorScheme.errorContainer
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        },
                    ),
                ) {
                    Text(
                        text = message,
                        color = if (bannerIsError.value) {
                            MaterialTheme.colorScheme.onErrorContainer
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        },
                        modifier = Modifier.padding(12.dp),
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = if (uiState.value.isLogin) "Sign In" else "Create Account",
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(SmartAttendPrimary, SmartAttendPrimaryGradientEnd),
                            ),
                            shape = RoundedCornerShape(18.dp),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.School,
                        contentDescription = null,
                        tint = SmartAttendPrimaryForeground,
                        modifier = Modifier.size(32.dp),
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "SmartAttend", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "Smart Attendance Management System",
                    color = SmartAttendMutedForeground,
                    textAlign = TextAlign.Center,
                )
            }

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = if (uiState.value.isLogin) "Welcome back" else "Create your account",
                        style = MaterialTheme.typography.titleLarge,
                    )

                    if (!uiState.value.isLogin) {
                        LabeledField(
                            label = "Full Name",
                            value = fullName.value,
                            placeholder = "John Doe",
                            onValueChange = { fullName.value = it },
                        )
                    }

                    LabeledField(
                        label = "Email",
                        value = email.value,
                        placeholder = "you@example.com",
                        keyboardType = KeyboardType.Email,
                        onValueChange = { email.value = it },
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = "Password", fontWeight = FontWeight.Medium)
                        OutlinedTextField(
                            value = password.value,
                            onValueChange = { password.value = it },
                            placeholder = { Text("••••••••") },
                            trailingIcon = {
                                IconButton(onClick = { showPassword.value = !showPassword.value }) {
                                    Icon(
                                        imageVector = if (showPassword.value) {
                                            Icons.Outlined.VisibilityOff
                                        } else {
                                            Icons.Outlined.Visibility
                                        },
                                        contentDescription = null,
                                    )
                                }
                            },
                            visualTransformation = if (showPassword.value) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = SmartAttendPrimary,
                                focusedLabelColor = SmartAttendPrimary,
                            ),
                            textStyle = TextStyle(fontSize = 14.sp),
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.submit(
                                context = context,
                                fullName = fullName.value,
                                email = email.value,
                                password = password.value,
                                onSuccess = onNavigateToApp,
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        enabled = !uiState.value.isLoading,
                    ) {
                        Text(text = if (uiState.value.isLogin) "Sign In" else "Create Account")
                    }

                    uiState.value.errorMessage?.let { message ->
                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }

                    TextButton(
                        onClick = { viewModel.toggleMode() },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    ) {
                        Text(
                            text = if (uiState.value.isLogin) {
                                "Don't have an account? Sign up"
                            } else {
                                "Already have an account? Sign in"
                            },
                            color = SmartAttendMutedForeground,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            listOf(SmartAttendHeroGradientStart, SmartAttendHeroGradientEnd),
                        ),
                        shape = RoundedCornerShape(24.dp),
                    )
                    .padding(20.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Streamline Your Attendance Tracking",
                        color = SmartAttendPrimaryForeground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = "Manage classes, students, and attendance effortlessly. Generate detailed reports and analytics with just a few taps.",
                        color = SmartAttendPrimaryForeground.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(top = 8.dp),
                    ) {
                        MetricChip(label = "100%", subLabel = "Accurate")
                        MetricChip(label = "Fast", subLabel = "Efficient")
                        MetricChip(label = "Secure", subLabel = "Protected")
                    }
                }
            }
        }
    }
}

@Composable
private fun LabeledField(
    label: String,
    value: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = label, fontWeight = FontWeight.Medium)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = SmartAttendPrimary,
                focusedLabelColor = SmartAttendPrimary,
            ),
            textStyle = TextStyle(fontSize = 14.sp),
        )
    }
}

@Composable
private fun MetricChip(label: String, subLabel: String) {
    Box(
        modifier = Modifier
            .background(
                color = SmartAttendPrimaryForeground.copy(alpha = 0.16f),
                shape = RoundedCornerShape(14.dp),
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, color = SmartAttendPrimaryForeground, fontWeight = FontWeight.Bold)
            Text(text = subLabel, color = SmartAttendPrimaryForeground.copy(alpha = 0.85f), fontSize = 11.sp)
        }
    }
}
