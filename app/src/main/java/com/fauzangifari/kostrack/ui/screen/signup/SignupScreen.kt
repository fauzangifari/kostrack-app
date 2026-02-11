package com.fauzangifari.kostrack.ui.screen.signup

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
fun SignupScreen(
    modifier: Modifier = Modifier,
    viewModel: SignupViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onSignupSuccess: () -> Unit = {},
    onGoogleSignupClick: () -> Unit = {},
    onSignInClick: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val uiState by viewModel.uiState.collectAsState()
    
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }
    var showToast by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        when (uiState) {
            is SignupUiState.Success -> {
                toastMessage = (uiState as SignupUiState.Success).message
                toastType = ToastType.SUCCESS
                showToast = true
                delay(1500)
                onSignupSuccess()
                viewModel.resetState()
            }
            is SignupUiState.Error -> {
                toastMessage = (uiState as SignupUiState.Error).message
                toastType = ToastType.ERROR
                showToast = true
                viewModel.resetState()
            }
            else -> {}
        }
    }

    fun validateAll(): Boolean {
        val isNameValid = name.isNotBlank()
        val isEmailValid = email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 6
        
        if (!isNameValid) nameError = "Nama lengkap wajib diisi"
        if (!isEmailValid) emailError = if (email.isBlank()) "Email wajib diisi" else "Format email tidak valid"
        if (!isPasswordValid) passwordError = if (password.isBlank()) "Kata sandi wajib diisi" else "Kata sandi minimal 6 karakter"
        
        return isNameValid && isEmailValid && isPasswordValid
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                enabled = uiState !is SignupUiState.Loading,
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
                text = "Daftar sekarang!",
                textAlign = TextAlign.Start,
                fontSize = 32.sp,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Lengkapi data dirimu untuk mulai menggunakan KosTrack.",
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextInput(
                label = "Nama Lengkap",
                value = name,
                placeholder = "Masukkan Nama Lengkap",
                onValueChange = { 
                    name = it
                    nameError = if (it.isBlank()) "Nama lengkap wajib diisi" else null
                },
                isEnabled = uiState !is SignupUiState.Loading,
                errorText = nameError
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                isEnabled = uiState !is SignupUiState.Loading,
                errorText = emailError
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextInput(
                label = "Kata Sandi",
                value = password,
                placeholder = "Masukkan Kata Sandi",
                onValueChange = { 
                    password = it
                    passwordError = when {
                        it.isBlank() -> "Kata sandi wajib diisi"
                        it.length < 6 -> "Kata sandi minimal 6 karakter"
                        else -> null
                    }
                },
                isPassword = true,
                isEnabled = uiState !is SignupUiState.Loading,
                errorText = passwordError
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (uiState is SignupUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Green500
                )
            } else {
                ButtonCustom(
                    value = "Daftar",
                    onClick = { if (validateAll()) viewModel.signUp(email, password, name) },
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
                value = "Daftar dengan Google",
                onClick = onGoogleSignupClick,
                isEnabled = uiState !is SignupUiState.Loading,
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

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Sudah punya akun?",
                    fontFamily = PlusJakartaSans
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sign in",
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    color = Green500,
                    modifier = Modifier.clickable(
                        enabled = uiState !is SignupUiState.Loading,
                        onClick = onSignInClick
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
fun SignupScreenPreview() {
    SignupScreen()
}
