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
    object Profile : Screen("profile")

    // Property Details/Forms
    object PropertyDetail : Screen("property_detail/{propertyId}") {
        fun createRoute(propertyId: String) = "property_detail/$propertyId"
    }

    object AddEditProperty : Screen("add_edit_property?propertyId={propertyId}") {
        fun createRoute(propertyId: String? = null) = 
            if (propertyId != null) "add_edit_property?propertyId=$propertyId" 
            else "add_edit_property"
    }

    object AddEditRoom : Screen("add_edit_room/{propertyId}?roomId={roomId}") {
        fun createRoute(propertyId: String, roomId: String? = null) = 
            if (roomId != null) "add_edit_room/$propertyId?roomId=$roomId"
            else "add_edit_room/$propertyId"
    }

    object AssignTenant : Screen("assign_tenant/{propertyId}/{roomId}") {
        fun createRoute(propertyId: String, roomId: String) = "assign_tenant/$propertyId/$roomId"
    }

    object TenantDetail : Screen("tenant_detail/{tenantId}") {
        fun createRoute(tenantId: String) = "tenant_detail/$tenantId"
    }
}
