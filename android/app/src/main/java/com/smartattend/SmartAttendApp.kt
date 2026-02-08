package com.smartattend

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smartattend.ui.screens.AppShell
import com.smartattend.ui.screens.HomeScreen
import com.smartattend.ui.screens.LoginScreen

@Composable
fun SmartAttendApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onNavigateToLogin = { navController.navigate("login") })
        }
        composable("login") {
            LoginScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToApp = { navController.navigate("app") },
            )
        }
        composable("app") {
            AppShell()
        }
    }
}
