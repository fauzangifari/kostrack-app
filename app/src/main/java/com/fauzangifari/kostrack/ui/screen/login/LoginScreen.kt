package com.fauzangifari.kostrack.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.kostrack.R
import com.fauzangifari.kostrack.ui.components.ButtonCustom
import com.fauzangifari.kostrack.ui.components.ButtonStyle
import com.fauzangifari.kostrack.ui.components.ButtonType
import com.fauzangifari.kostrack.ui.components.CustomToast
import com.fauzangifari.kostrack.ui.components.TextInput
import com.fauzangifari.kostrack.ui.components.ToastType
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    onGoogleLoginClick: () -> Unit = {},
    onSignupClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    
    val uiState by viewModel.uiState.collectAsState()
    
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }
    var showToast by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUiState.Success -> {
                toastMessage = (uiState as LoginUiState.Success).message
                toastType = ToastType.SUCCESS
                showToast = true
                delay(1500)
                onLoginSuccess()
                viewModel.resetState()
            }
            is LoginUiState.Error -> {
                toastMessage = (uiState as LoginUiState.Error).message
                toastType = ToastType.ERROR
                showToast = true
                viewModel.resetState()
            }
            else -> {}
        }
    }

    fun validateAll(): Boolean {
        val isEmailValid = email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.isNotBlank()
        
        if (!isEmailValid) emailError = if (email.isBlank()) "Email wajib diisi" else "Format email tidak valid"
        if (!isPasswordValid) passwordError = "Kata sandi wajib diisi"
        
        return isEmailValid && isPasswordValid
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                enabled = uiState !is LoginUiState.Loading,
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFD6F4E7), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Selamat Datang!",
                textAlign = TextAlign.Start,
                fontSize = 32.sp,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Masuk untuk melanjutkan pengelolaan kos kamu dengan mudah.",
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextInput(
                label = "Alamat Email",
                value = email,
                placeholder = "Masukkan Email",
                onValueChange = { 
                    email = it
                    emailError = when {
                        it.isBlank() -> "Email wajib diisi"
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() -> "Format email tidak valid"
                        else -> null
                    }
                },
                singleLine = true,
                isPassword = false,
                isEnabled = uiState !is LoginUiState.Loading,
                errorText = emailError
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextInput(
                label = "Kata Sandi",
                value = password,
                placeholder = "Masukkan Kata Sandi",
                onValueChange = { 
                    password = it
                    passwordError = if (it.isBlank()) "Kata sandi wajib diisi" else null
                },
                singleLine = true,
                isPassword = true,
                isEnabled = uiState !is LoginUiState.Loading,
                errorText = passwordError
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Lupa Kata Sandi?",
                    fontSize = 14.sp,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    color = Green500,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .clickable(
                            enabled = uiState !is LoginUiState.Loading,
                            onClick = onForgotPasswordClick
                        )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (uiState is LoginUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Green500
                )
            } else {
                ButtonCustom(
                    value = "Masuk",
                    onClick = { if (validateAll()) viewModel.signIn(email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    buttonType = ButtonType.REGULAR,
                    buttonStyle = ButtonStyle.FILLED,
                    fontSize = 16,
                    isEnabled = true
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            ButtonCustom(
                value = "Masuk dengan Google",
                onClick = onGoogleLoginClick,
                isEnabled = uiState !is LoginUiState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                buttonType = ButtonType.REGULAR,
                buttonStyle = ButtonStyle.OUTLINED,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google Icon",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                },
                fontSize = 16,
                textColor = Green500
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Belum punya akun?",
                    fontFamily = PlusJakartaSans
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Daftar",
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    color = Green500,
                    modifier = Modifier.clickable(
                        enabled = uiState !is LoginUiState.Loading,
                        onClick = onSignupClick
                    )
                )
            }
        }

        CustomToast(
            message = toastMessage,
            type = toastType,
            isVisible = showToast,
            onDismiss = { showToast = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}