package com.fauzangifari.kostrack.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.fauzangifari.kostrack.ui.components.TextInput
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onGoogleLoginClick: () -> Unit = {},
    onSignupClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFD6F4E7), shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
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
            onValueChange = { email = it },
            singleLine = true,
            isPassword = false,
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextInput(
            label = "Kata Sandi",
            value = password,
            placeholder = "Masukkan Kata Sandi",
            onValueChange = { password = it },
            singleLine = true,
            isPassword = true,
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
                    .clickable(onClick = onForgotPasswordClick)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        ButtonCustom(
            value = "Masuk",
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            buttonType = ButtonType.REGULAR,
            buttonStyle = ButtonStyle.FILLED,
            fontSize = 16
        )

        Spacer(modifier = Modifier.height(20.dp))

        ButtonCustom(
            value = "Masuk dengan Google",
            onClick = onGoogleLoginClick,
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
                modifier = Modifier.clickable(onClick = onSignupClick)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
