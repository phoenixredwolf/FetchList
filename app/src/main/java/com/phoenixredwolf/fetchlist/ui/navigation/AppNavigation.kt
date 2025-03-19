package com.phoenixredwolf.fetchlist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.phoenixredwolf.fetchlist.SplashPage
import com.phoenixredwolf.fetchlist.ui.screens.HomeScreen

/**
 * A composable that manages navigation within the application.
 *
 * This composable uses the Jetpack Navigation Component to define and manage navigation between different screens.
 * It sets up a [NavHost] with two destinations: "splash" and "home".
 * The "splash" destination displays the [SplashPage], and the "home" destination displays the [HomeScreen].
 * The navigation starts at the "splash" destination.
 */
@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashPage(navController)
        }
        composable("home") {
            HomeScreen()
        }
    }
}
