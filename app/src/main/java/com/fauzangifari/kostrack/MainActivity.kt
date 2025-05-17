package com.fauzangifari.kostrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.fauzangifari.kostrack.ui.screen.signup.SignupScreen
import com.fauzangifari.kostrack.ui.screen.splash.SplashScreen
import com.fauzangifari.kostrack.ui.screen.welcome.WelcomeScreen
import com.fauzangifari.kostrack.ui.theme.KosTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KosTrackTheme {
                var showSplash by rememberSaveable { mutableStateOf(true) }

                if (showSplash) {
                    SplashScreen { showSplash = false }
                } else {
                    SignupScreen()
                }
            }
        }
    }
}