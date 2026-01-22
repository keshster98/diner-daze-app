package com.keshen.dinerdazeapp.ui.screens.settings

import androidx.lifecycle.ViewModel
import com.keshen.dinerdazeapp.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authService: AuthService
): ViewModel() {

    fun logout() {
        authService.signOut()
    }
}