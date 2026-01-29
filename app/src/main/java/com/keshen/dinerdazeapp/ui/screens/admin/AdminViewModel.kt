package com.keshen.dinerdazeapp.ui.screens.admin

import androidx.lifecycle.ViewModel
import com.keshen.dinerdazeapp.service.AuthService
import com.keshen.dinerdazeapp.service.UserProfileService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val authService: AuthService,
    private val profileService: UserProfileService
): ViewModel(){
    // TODO: Create Admin ViewModel
}
