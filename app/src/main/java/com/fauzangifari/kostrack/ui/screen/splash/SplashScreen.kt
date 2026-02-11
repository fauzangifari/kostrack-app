package com.fauzangifari.kostrack.ui.screen.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fauzangifari.kostrack.R
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.KosTrackTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashEnd: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "Alpha"
    )

    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.7f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "Scale"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500)
        onSplashEnd()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Green500),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_kostrack),
            contentDescription = "Splash Logo",
            modifier = Modifier
                .size(180.dp)
                .scale(scaleAnim)
                .alpha(alphaAnim)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    KosTrackTheme {
        SplashScreen {}
    }
}
