package com.keshen.dinerdazeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.keshen.dinerdazeapp.service.AuthService
import com.keshen.dinerdazeapp.service.UserProfileService
import com.keshen.dinerdazeapp.ui.screens.auth.SignInScreen
import com.keshen.dinerdazeapp.ui.screens.auth.SignUpScreen
import com.keshen.dinerdazeapp.ui.screens.home.HomeScreen

@Composable
fun AppNav(
    authService: AuthService,
    profileService: UserProfileService
) {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = Screen.SignIn
        ) {

        composable<Screen.Home> {
            HomeScreen(
                onSignOutClick = {
                    rootNavController.navigate(Screen.SignIn) {
                        popUpTo(Screen.Home) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Screen.SignIn> {
            SignInScreen(
                onSuccess = { completed ->
                    rootNavController.navigate(
                        if (completed) Screen.Home else Screen.Home
                    ) {
                        popUpTo(Screen.SignIn) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    rootNavController.navigate(Screen.SignUp)
                }
            )
        }

        composable<Screen.SignUp> {
            SignUpScreen(
                onSuccess = {
                    rootNavController.navigate(Screen.SignIn) {
                        popUpTo(Screen.SignUp) { inclusive = true }
                    }
                },
                onSignInClick = {
                    rootNavController.navigate(Screen.SignIn)
                }
            )
        }
    }
}