package com.fauzangifari.kostrack.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apartment
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fauzangifari.kostrack.ui.screen.dashboard.DashboardScreen
import com.fauzangifari.kostrack.ui.screen.profile.ProfileScreen
import com.fauzangifari.kostrack.ui.screen.properties.PropertiesScreen
import com.fauzangifari.kostrack.ui.screen.transactions.TransactionsScreen
import com.fauzangifari.kostrack.ui.theme.Green500
import com.fauzangifari.kostrack.ui.theme.Green700
import com.fauzangifari.kostrack.ui.theme.Grey500
import com.fauzangifari.kostrack.ui.theme.NeoBg
import com.fauzangifari.kostrack.ui.theme.PlusJakartaSans

@Composable
fun MainFlowScreen(rootNavController: NavController) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarScreens = listOf(
        BottomNavItem("Beranda", Screen.Dashboard.route, Icons.Rounded.Dashboard),
        BottomNavItem("Kos Saya", Screen.Properties.route, Icons.Rounded.Apartment),
        BottomNavItem("Transaksi", Screen.Transactions.route, Icons.Rounded.ReceiptLong),
        BottomNavItem("Profil", Screen.Profile.route, Icons.Rounded.Person)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {
                bottomBarScreens.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        },
                        label = {
                            Text(
                                item.label,
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp
                            )
                        },
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Green700,
                            selectedTextColor = Green700,
                            unselectedIconColor = Grey500,
                            unselectedTextColor = Grey500,
                            indicatorColor = Green500.copy(alpha = 0.12f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding()),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onAddPropertyClick = {
                        rootNavController.navigate(Screen.AddEditProperty.createRoute())
                    },
                    onPropertyClick = { id ->
                        rootNavController.navigate(Screen.PropertyDetail.createRoute(id))
                    },
                    onViewAllPropertiesClick = {
                        navController.navigate(Screen.Properties.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onTransactionsClick = {
                        navController.navigate(Screen.Transactions.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onProfileClick = {
                        navController.navigate(Screen.Profile.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(Screen.Properties.route) {
                PropertiesScreen(onAddPropertyClick = {
                    rootNavController.navigate(Screen.AddEditProperty.createRoute())
                }, onPropertyClick = { id ->
                    rootNavController.navigate(Screen.PropertyDetail.createRoute(id))
                })
            }
            composable(Screen.Transactions.route) {
                TransactionsScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen(onLogoutSuccess = {
                    rootNavController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                })
            }
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)
