package com.fauzangifari.kostrack.ui.screen.signup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
fun SignupScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSignupClick: () -> Unit = {},
    onGoogleSignupClick: () -> Unit = {},
    onSignInClick: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
//        IconButton(
//            onClick = onBackClick,
//            modifier = Modifier
//                .size(36.dp)
//                .background(Color(0xFFD6F4E7), shape = CircleShape)
//        ) {
//            Icon(
//                imageVector = Icons.Default.ArrowBack,
//                contentDescription = "Back",
//                tint = Color.Black
//            )
//        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Daftar sekarang!",
            textAlign = TextAlign.Start,
            fontSize = 32.sp,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

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
            onValueChange = { name = it },
            singleLine = true,
            isError = false,
            supportingText = null,
            isPassword = false,
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextInput(
            label = "Alamat Email",
            value = email,
            placeholder = "Masukkan Email",
            onValueChange = { email = it },
            singleLine = true,
            isError = false,
            supportingText = null,
            isPassword = false,
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextInput(
            label = "Kata Sandi",
            value = password,
            placeholder = "Masukkan Kata Sandi",
            onValueChange = { password = it },
            singleLine = true,
            isError = false,
            supportingText = null,
            isPassword = true,
        )

        Spacer(modifier = Modifier.height(40.dp))

        ButtonCustom(
            value = "Daftar",
            onClick = onSignupClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            buttonType = ButtonType.REGULAR,
            buttonStyle = ButtonStyle.FILLED,
            fontSize = 16
        )

        Spacer(modifier = Modifier.height(20.dp))

        ButtonCustom(
            value = "Daftar dengan Google",
            onClick = onGoogleSignupClick,
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
            Text("Sudah punya akun?")
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Sign in",
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.clickable(onClick = onSignInClick)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    MaterialTheme {
        SignupScreen()
    }
}
