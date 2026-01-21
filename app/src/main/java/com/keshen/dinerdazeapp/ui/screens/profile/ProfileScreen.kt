package com.keshen.dinerdazeapp.ui.screens.profile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.keshen.dinerdazeapp.service.AuthService
import com.keshen.dinerdazeapp.service.UserProfileService

@Composable
fun ProfileScreen(
    navController: NavHostController,
    authService: AuthService,
    profileService: UserProfileService
) {
    Text("This is Menu Screen")
}