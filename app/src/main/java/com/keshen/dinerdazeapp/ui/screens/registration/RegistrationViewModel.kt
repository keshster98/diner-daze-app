package com.keshen.dinerdazeapp.ui.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.dinerdazeapp.core.utils.MessageType
import com.keshen.dinerdazeapp.core.utils.UiMessage
import com.keshen.dinerdazeapp.data.model.User
import com.keshen.dinerdazeapp.service.AuthService
import com.keshen.dinerdazeapp.service.UserProfileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authService: AuthService,
    private val profileService: UserProfileService
) : ViewModel() {

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting = _isSubmitting.asStateFlow()

    private val _message = MutableStateFlow<UiMessage?>(null)
    val message = _message.asStateFlow()

    fun submitRegistration(
        user: User,
        onSuccess: () -> Unit
    ) {
        _message.value = null
        _isSubmitting.value = true

        if (!isValid(user)) {
            showError("Please fill in all fields!")
            return
        }

        saveProfile(user, onSuccess)
    }

    fun logout() {
        authService.signOut()
    }

    private fun saveProfile(
        user: User,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            runCatching {
                profileService.saveProfile(user)
            }.onSuccess {
                _message.value = UiMessage(
                    text = "Profile completed successfully. Welcome, ${user.firstName}!",
                    type = MessageType.SUCCESS
                )
                delay(1200)
                onSuccess()
            }.onFailure { throwable ->
                showError(
                    throwable.message
                        ?: "Failed to complete registration. Please try again."
                )
            }
        }
    }

    private fun isValid(user: User): Boolean {
        return user.firstName.isNotBlank() &&
                user.lastName.isNotBlank() &&
                user.phone.isNotBlank()
    }

    private fun showError(text: String) {
        _message.value = UiMessage(text, MessageType.ERROR)
        _isSubmitting.value = false
        clearMessage()
    }

    private fun clearMessage() {
        viewModelScope.launch {
            delay(2000)
            _message.value = null
        }
    }
}