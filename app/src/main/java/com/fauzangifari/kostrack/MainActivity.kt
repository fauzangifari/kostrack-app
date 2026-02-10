package com.fauzangifari.kostrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fauzangifari.kostrack.ui.navigation.Screen
import com.fauzangifari.kostrack.ui.screen.login.LoginScreen
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
                val navController = rememberNavController()
                
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Splash.route,
                        modifier = Modifier.padding(innerPadding),
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(400)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(400)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(400)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(400)
                            )
                        }
                    ) {
                        composable(Screen.Splash.route) {
                            SplashScreen(
                                onSplashEnd = {
                                    navController.navigate(Screen.Welcome.route) {
                                        popUpTo(Screen.Splash.route) { inclusive = true }
                                    }
                                }
                            )
                        }
                        
                        composable(Screen.Welcome.route) {
                            WelcomeScreen(
                                onGetStartedClick = {
                                    navController.navigate(Screen.Signup.route)
                                },
                                onLoginClick = {
                                    navController.navigate(Screen.Login.route)
                                }
                            )
                        }
                        
                        composable(Screen.Login.route) {
                            LoginScreen(
                                onBackClick = {
                                    navController.navigateUp()
                                },
                                onSignupClick = {
                                    navController.navigate(Screen.Signup.route)
                                },
                                onLoginClick = {
                                    // Handle login logic
                                }
                            )
                        }
                        
                        composable(Screen.Signup.route) {
                            SignupScreen(
                                onBackClick = {
                                    navController.navigateUp()
                                },
                                onSignInClick = {
                                    navController.navigate(Screen.Login.route)
                                },
                                onSignupClick = {
                                    // Handle signup logic
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}