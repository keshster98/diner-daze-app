package com.keshen.dinerdazeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.keshen.dinerdazeapp.ui.screens.home.HomeScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home
        ) {
        composable<Screen.Home> {
            HomeScreen(navController)
        }
    }
}