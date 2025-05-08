package com.fauzangifari.kostrack.ui.screen.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.kostrack.R
import com.fauzangifari.kostrack.ui.components.ButtonCustom
import com.fauzangifari.kostrack.ui.components.ButtonStyle
import com.fauzangifari.kostrack.ui.components.ButtonType
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Image(
            painter = painterResource(id = R.drawable.welcome_ilustration),
            contentDescription = "Ilustrasi Selamat Datang",
            modifier = Modifier
                .height(350.dp)
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            contentScale = ContentScale.Fit
        )


        Text(
            text = "Selamat Datang!",
            textAlign = TextAlign.Start,
            fontSize = 32.sp,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Text(
            text = "Login dan lanjutkan pencatatan kos harianmu dengan mudah.",
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        ButtonCustom(
            value = "Sign up",
            onClick = {},
            fontSize = 16,
            buttonType = ButtonType.REGULAR,
            buttonStyle = ButtonStyle.FILLED,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        ButtonCustom(
            value = "Sign in",
            onClick = {},
            fontSize = 16,
            buttonType = ButtonType.REGULAR,
            buttonStyle = ButtonStyle.OUTLINED,
            textColor = Green500,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        )
    }
}
