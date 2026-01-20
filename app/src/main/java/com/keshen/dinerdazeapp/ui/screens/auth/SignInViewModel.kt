package com.keshen.dinerdazeapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.dinerdazeapp.core.utils.MessageType
import com.keshen.dinerdazeapp.core.utils.UiMessage
import com.keshen.dinerdazeapp.service.AuthService
import com.keshen.dinerdazeapp.service.UserProfileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authService: AuthService,
    private val profileService: UserProfileService
) : ViewModel() {

    private val _isSigningIn = MutableStateFlow(false)
    val isSigningIn = _isSigningIn.asStateFlow()

    private val _message = MutableStateFlow<UiMessage?>(null)
    val message = _message.asStateFlow()

    fun signIn(
        email: String,
        password: String,
        onSuccess: (Boolean) -> Unit
    ) {
        _message.value = null
        _isSigningIn.value = true

        if (email.isBlank() || password.isBlank()) {
            _message.value = UiMessage(
                "Email and/or password cannot be empty!",
                MessageType.ERROR
            )
            clearMessage()
            return
        }

        performSignIn(email, password, onSuccess)
    }

    private fun performSignIn(
        email: String,
        password: String,
        onSuccess: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            runCatching {
                authService.signIn(email, password)

                val uid = authService.uid()
                val name = profileService.getUserFirstName(uid, email)
                val completed = profileService.isProfileFilled(uid)

                name to completed
            }.onSuccess { (name, completed) ->
                _message.value = UiMessage(
                    "Success! Signing you in, $name",
                    MessageType.SUCCESS
                )

                delay(2000)
                onSuccess(completed)
            }.onFailure {
                _message.value = UiMessage(
                    it.message ?: "Something went wrong. Please try again.",
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
            _isSigningIn.value = false
        }
    }
}