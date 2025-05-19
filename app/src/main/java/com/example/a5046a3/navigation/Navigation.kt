package com.example.a5046a3.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Defines all navigation destinations in the app
 */
sealed class Screen(val route: String) {
    // Authentication screens
    object Login : Screen("login")
    object Register : Screen("register")
    
    // Main screens
    object Home : Screen("home")
    object DataEntry : Screen("data_entry")
    object History : Screen("history")
    object Report : Screen("report")
    object Account : Screen("account")
    object QuickAccess : Screen("quick_access")
}

/**
 * Defines the main bottom navigation items
 */
sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val title: String
) {
    object Home : BottomNavItem(
        route = Screen.Home.route,
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        title = "Home"
    )
    
    object History : BottomNavItem(
        route = Screen.History.route,
        icon = Icons.Outlined.History,
        selectedIcon = Icons.Filled.History,
        title = "History"
    )
    
    object Report : BottomNavItem(
        route = Screen.Report.route,
        icon = Icons.Outlined.BarChart,
        selectedIcon = Icons.Filled.BarChart,
        title = "Reports"
    )
    
    object Account : BottomNavItem(
        route = Screen.Account.route,
        icon = Icons.Outlined.Person,
        selectedIcon = Icons.Filled.Person,
        title = "Account"
    )
}

/**
 * List of all bottom navigation items
 */
val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.History,
    BottomNavItem.Report,
    BottomNavItem.Account
) 