package com.fauzangifari.kostrack.ui.screen.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.fauzangifari.kostrack.ui.components.GlassCard
import com.fauzangifari.kostrack.ui.components.KosTrackTopAppBar
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.Green700
import com.fauzangifari.kostrack.ui.theme.KosTrackTheme
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onGetStartedClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    Scaffold(
        topBar = { KosTrackTopAppBar(title = "KosTrack") },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.welcome_ilustration),
                contentDescription = "Ilustrasi Selamat Datang",
                modifier = Modifier
                    .height(260.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            GlassCard {
                Column {
                    Text(
                        text = "Selamat Datang!",
                        textAlign = TextAlign.Start,
                        fontSize = 26.sp,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Bold,
                        color = Green700,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Login dan lanjutkan pencatatan kos harianmu dengan mudah.",
                        textAlign = TextAlign.Start,
                        fontSize = 14.sp,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    ButtonCustom(
                        value = "Mulai Sekarang",
                        onClick = onGetStartedClick,
                        fontSize = 16,
                        buttonType = ButtonType.PILL,
                        buttonStyle = ButtonStyle.FILLED,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ButtonCustom(
                        value = "Sudah punya akun",
                        onClick = onLoginClick,
                        fontSize = 16,
                        buttonType = ButtonType.PILL,
                        buttonStyle = ButtonStyle.OUTLINED,
                        textColor = Green500,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    KosTrackTheme {
        WelcomeScreen()
    }
}
