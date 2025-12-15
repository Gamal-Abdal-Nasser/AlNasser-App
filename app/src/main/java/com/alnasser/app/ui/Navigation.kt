package com.alnasser.app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alnasser.app.ui.screens.InputScreen
import com.alnasser.app.ui.screens.MannequinScreen
import com.alnasser.app.ui.screens.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Input : Screen("input")
    object Mannequin : Screen("mannequin")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToInput = {
                    navController.navigate(Screen.Input.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Input.route) {
            InputScreen(
                onNavigateToMannequin = {
                    navController.navigate(Screen.Mannequin.route) {
                        popUpTo(Screen.Input.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Mannequin.route) {
            MannequinScreen()
        }
    }
}
