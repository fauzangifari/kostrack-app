package com.fauzangifari.kostrack

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fauzangifari.kostrack.ui.navigation.MainFlowScreen
import com.fauzangifari.kostrack.ui.navigation.Screen
import com.fauzangifari.kostrack.ui.screen.login.LoginScreen
import com.fauzangifari.kostrack.ui.screen.signup.SignupScreen
import com.fauzangifari.kostrack.ui.screen.splash.SplashScreen
import com.fauzangifari.kostrack.ui.screen.splash.SplashViewModel
import com.fauzangifari.kostrack.ui.screen.welcome.WelcomeScreen
import com.fauzangifari.kostrack.ui.screen.rooms.RoomEditorScreen
import com.fauzangifari.kostrack.ui.screen.tenants.TenantDetailScreen
import com.fauzangifari.kostrack.ui.screen.tenants.TenantEditorScreen
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.KosTrackTheme
import com.fauzangifari.kostrack.ui.theme.White
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KosTrackTheme {
                RootApp()
            }
        }
    }
}

@Composable
fun RootApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val isSplash = currentRoute == Screen.Splash.route
            
            window.statusBarColor = if (isSplash) Green500.toArgb() else White.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isSplash
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = Modifier.fillMaxSize(),
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
            val splashViewModel: SplashViewModel = koinViewModel()
            val isLoggedIn by splashViewModel.isLoggedIn.collectAsState(initial = false)

            SplashScreen(
                onSplashEnd = {
                    val nextRoute = if (isLoggedIn) Screen.Main.route else Screen.Welcome.route
                    navController.navigate(nextRoute) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onGetStartedClick = { navController.navigate(Screen.Signup.route) },
                onLoginClick = { navController.navigate(Screen.Login.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onBackClick = { navController.navigateUp() },
                onSignupClick = { navController.navigate(Screen.Signup.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onBackClick = { navController.navigateUp() },
                onSignInClick = { navController.navigate(Screen.Login.route) },
                onSignupSuccess = { navController.navigate(Screen.Login.route) }
            )
        }

        composable(Screen.Main.route) {
            MainFlowScreen(rootNavController = navController)
        }

        composable(
            route = Screen.PropertyDetail.route,
            arguments = listOf(
                androidx.navigation.navArgument("propertyId") {
                    type = androidx.navigation.NavType.StringType
                }
            )
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: ""
            com.fauzangifari.kostrack.ui.screen.properties.PropertyDetailScreen(
                propertyId = propertyId,
                onBackClick = { navController.navigateUp() },
                onEditPropertyClick = { id -> 
                    navController.navigate(Screen.AddEditProperty.createRoute(id)) 
                },
                onAddRoomClick = { id -> 
                    navController.navigate(Screen.AddEditRoom.createRoute(id)) 
                },
                onRoomClick = { propId, roomId -> 
                    navController.navigate(Screen.AddEditRoom.createRoute(propId, roomId)) 
                },
                onAssignTenantClick = { propId, roomId ->
                    navController.navigate(Screen.AssignTenant.createRoute(propId, roomId))
                },
                onTenantClick = { tenantId ->
                    navController.navigate(Screen.TenantDetail.createRoute(tenantId))
                }
            )
        }

        composable(
            route = Screen.AddEditProperty.route,
            arguments = listOf(
                androidx.navigation.navArgument("propertyId") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId")
            com.fauzangifari.kostrack.ui.screen.properties.PropertyEditorScreen(
                propertyId = propertyId,
                onBackClick = { navController.navigateUp() }
            )
        }

        composable(
            route = Screen.AddEditRoom.route,
            arguments = listOf(
                androidx.navigation.navArgument("propertyId") {
                    type = androidx.navigation.NavType.StringType
                },
                androidx.navigation.navArgument("roomId") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: ""
            val roomId = backStackEntry.arguments?.getString("roomId")
            RoomEditorScreen(
                propertyId = propertyId,
                roomId = roomId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AssignTenant.route,
            arguments = listOf(
                androidx.navigation.navArgument("propertyId") {
                    type = androidx.navigation.NavType.StringType
                },
                androidx.navigation.navArgument("roomId") {
                    type = androidx.navigation.NavType.StringType
                }
            )
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: ""
            val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
            TenantEditorScreen(
                propertyId = propertyId,
                roomId = roomId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TenantDetail.route,
            arguments = listOf(
                androidx.navigation.navArgument("tenantId") {
                    type = androidx.navigation.NavType.StringType
                }
            )
        ) { backStackEntry ->
            val tenantId = backStackEntry.arguments?.getString("tenantId") ?: ""
            TenantDetailScreen(
                tenantId = tenantId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}