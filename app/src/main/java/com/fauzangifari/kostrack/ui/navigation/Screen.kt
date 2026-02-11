package com.fauzangifari.kostrack.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Signup : Screen("signup")
    
    // Root for Bottom Bar screens
    object Main : Screen("main")
    
    // Tabs
    object Dashboard : Screen("dashboard")
    object Properties : Screen("properties")
    object Transactions : Screen("transactions")
}
