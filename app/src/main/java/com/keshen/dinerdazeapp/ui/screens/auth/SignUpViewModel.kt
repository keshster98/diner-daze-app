package com.keshen.dinerdazeapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.dinerdazeapp.core.utils.MessageType
import com.keshen.dinerdazeapp.core.utils.UiMessage
import com.keshen.dinerdazeapp.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    private val _isSigningUp = MutableStateFlow(false)
    val isSigningUp = _isSigningUp.asStateFlow()

    private val _message = MutableStateFlow<UiMessage?>(null)
    val message = _message.asStateFlow()

    fun signUp(
        email: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ) {
        _message.value = null
        _isSigningUp.value = true

        when {
            email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                _message.value = UiMessage(
                    "All fields are required",
                    MessageType.ERROR
                )
                clearMessage()
                return
            }

            password != confirmPassword -> {
                _message.value = UiMessage(
                    "Passwords do not match",
                    MessageType.ERROR
                )
                clearMessage()
                return
            }
        }

        performSignUp(email, password, onSuccess)
    }

    private fun performSignUp(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            runCatching {
                authService.signUp(email, password)
            }.onSuccess {
                _message.value = UiMessage(
                    "Account created successfully! Redirecting you to the registration form",
                    MessageType.SUCCESS
                )
                delay(2000)
                onSuccess()
            }.onFailure {
                _message.value = UiMessage(
                    it.message ?: "Sign up failed. Please try again.",
                    MessageType.ERROR
                )
                clearMessage()
            }
        }
    }

    fun clearMessage() {
        viewModelScope.launch {
            delay(2000)
            _message.value = null
            _isSigningUp.value = false
        }
    }
}