package com.example.a5046a3.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.a5046a3.ui.screens.auth.LoginScreen
import com.example.a5046a3.ui.screens.auth.RegisterScreen
import com.example.a5046a3.ui.screens.main.AccountScreen
import com.example.a5046a3.ui.screens.main.DataEntryScreen
import com.example.a5046a3.ui.screens.main.HistoryScreen
import com.example.a5046a3.ui.screens.main.HomeScreen
import com.example.a5046a3.ui.screens.main.QuickAccessScreen
import com.example.a5046a3.ui.screens.main.ReportScreen

/**
 * App navigation component that sets up navigation routes
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    Log.d("AppNavigation", "Current route: $currentRoute")
    
    // Check if on a main screen where bottom navigation should be displayed
    val showBottomNav = currentRoute in listOf(
        Screen.Home.route,
        Screen.History.route,
        Screen.Report.route,
        Screen.Account.route
    )
    
    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomAppBar(
                    containerColor = Color(0xFF5C6BC0),
                    contentColor = Color.White,
                    tonalElevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
                    ) {
                        // Home tab
                        IconButton(
                            onClick = { 
                                if (currentRoute != Screen.Home.route) {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Home.route) { inclusive = false }
                                        launchSingleTop = true
                                    }
                                }
                            }
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = if (currentRoute == Screen.Home.route) 
                                        Icons.Filled.Home else Icons.Outlined.Home,
                                    contentDescription = "Home",
                                    tint = Color.White
                                )
                                Text(
                                    "Home", 
                                    color = Color.White, 
                                    fontSize = 12.sp
                                )
                            }
                        }
                        
                        // History tab
                        IconButton(
                            onClick = { 
                                if (currentRoute != Screen.History.route) {
                                    navController.navigate(Screen.History.route) {
                                        launchSingleTop = true
                                    }
                                }
                            }
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = if (currentRoute == Screen.History.route) 
                                        Icons.Filled.History else Icons.Outlined.History,
                                    contentDescription = "History",
                                    tint = Color.White
                                )
                                Text(
                                    "History", 
                                    color = Color.White, 
                                    fontSize = 12.sp
                                )
                            }
                        }
                        
                        // Reports tab
                        IconButton(
                            onClick = { 
                                if (currentRoute != Screen.Report.route) {
                                    navController.navigate(Screen.Report.route) {
                                        launchSingleTop = true
                                    }
                                }
                            }
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = if (currentRoute == Screen.Report.route) 
                                        Icons.Filled.BarChart else Icons.Outlined.BarChart,
                                    contentDescription = "Reports",
                                    tint = Color.White
                                )
                                Text(
                                    "Reports", 
                                    color = Color.White, 
                                    fontSize = 12.sp
                                )
                            }
                        }
                        
                        // Account tab
                        IconButton(
                            onClick = { 
                                if (currentRoute != Screen.Account.route) {
                                    navController.navigate(Screen.Account.route) {
                                        launchSingleTop = true
                                    }
                                }
                            }
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = if (currentRoute == Screen.Account.route) 
                                        Icons.Filled.Person else Icons.Outlined.Person,
                                    contentDescription = "Account",
                                    tint = Color.White
                                )
                                Text(
                                    "Account", 
                                    color = Color.White, 
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Login.route
            ) {
                // Authentication screens
                composable(Screen.Login.route) {
                    LoginScreen(navController = navController)
                }
                
                composable(Screen.Register.route) {
                    RegisterScreen(navController = navController)
                }
                
                // Main screens
                composable(Screen.Home.route) {
                    HomeScreen(navController = navController)
                }
                
                composable(Screen.DataEntry.route) {
                    DataEntryScreen(navController = navController)
                }
                
                composable(Screen.History.route) {
                    HistoryScreen(navController = navController)
                }
                
                composable(Screen.Report.route) {
                    ReportScreen(navController = navController)
                }
                
                composable(Screen.Account.route) {
                    AccountScreen(navController = navController)
                }
                
                composable(Screen.QuickAccess.route) {
                    QuickAccessScreen(navController = navController)
                }
            }
        }
    }
} 