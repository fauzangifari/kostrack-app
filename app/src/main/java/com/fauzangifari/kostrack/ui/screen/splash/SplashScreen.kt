package com.fauzangifari.kostrack.ui.screen.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.fauzangifari.kostrack.R
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.KosTrackTheme

@Composable
fun SplashScreen(onSplashEnd: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    var endAnimation by remember { mutableStateOf(false) }

    val transition = updateTransition(targetState = startAnimation, label = "Splash Animation")

    val alphaAnim by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000) },
        label = "Alpha Animation"
    ) { state -> if (state) 1f else 0f }

    val scaleAnim by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000, easing = EaseOutCubic) },
        label = "Scale Animation"
    ) { state -> if (state) 1f else 0.8f }

    val exitAlpha by animateFloatAsState(
        targetValue = if (endAnimation) 0f else alphaAnim,
        animationSpec = tween(durationMillis = 500),
        label = "Exit Alpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500)
        endAnimation = true
        delay(500)
        onSplashEnd()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Green500),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_kostrack),
            contentDescription = "Splash Logo",
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer(scaleX = scaleAnim, scaleY = scaleAnim)
                .alpha(exitAlpha)
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
