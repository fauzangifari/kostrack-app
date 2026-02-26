package com.fauzangifari.kostrack.ui.screen.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fauzangifari.kostrack.R
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.Green700
import com.fauzangifari.kostrack.ui.theme.KosTrackTheme
import com.fauzangifari.kostrack.ui.theme.NeoBg
import com.fauzangifari.kostrack.ui.theme.NeoShadowDark
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashEnd: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "Alpha"
    )

    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.6f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "Scale"
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 600, delayMillis = 400, easing = FastOutSlowInEasing),
        label = "TextAlpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500)
        onSplashEnd()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NeoBg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo with neomorphic raised effect
            Box(
                modifier = Modifier
                    .scale(scaleAnim)
                    .alpha(alphaAnim)
                    .shadow(12.dp, RoundedCornerShape(32.dp), ambientColor = NeoShadowDark, spotColor = NeoShadowDark)
                    .clip(RoundedCornerShape(32.dp))
                    .background(NeoBg)
                    .padding(24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_kostrack),
                    contentDescription = "KosTrack Logo",
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "KosTrack",
                fontSize = 30.sp,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                color = Green700,
                modifier = Modifier.alpha(textAlpha)
            )

            Text(
                text = "Kelola kos-mu dengan mudah",
                fontSize = 14.sp,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Normal,
                color = Green500.copy(alpha = 0.7f),
                modifier = Modifier.alpha(textAlpha)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    KosTrackTheme {
        SplashScreen {}
    }
}
