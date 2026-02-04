package com.keshen.dinerdazeapp.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.dinerdazeapp.core.store.PendingDeletionStore
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
class SettingsViewModel @Inject constructor(
    private val authService: AuthService,
    private val profileService: UserProfileService,
    private val pendingStore: PendingDeletionStore
) : ViewModel() {

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting = _isSubmitting.asStateFlow()

    private val _message = MutableStateFlow<UiMessage?>(null)
    val message = _message.asStateFlow()

    fun logout() {
        authService.signOut()
    }

    fun requestDeletion(onDone: () -> Unit) {
        if (_isSubmitting.value) return

        _message.value = null
        _isSubmitting.value = true

        viewModelScope.launch {
            runCatching {
                val uid = authService.uid()
                val email = authService.email()
                val now = System.currentTimeMillis()

                // Flag Firestore profile for deletion
                profileService.requestAccountDeletion(uid)

                // Persist local pending deletion flag (for Access tab blocking)
                pendingStore.setPending(email = email, requestedAt = now)

                // Sign out immediately
                authService.signOut()
            }.onSuccess {
                _message.value = UiMessage(
                    text = "Account deletion request submitted",
                    type = MessageType.SUCCESS
                )
                delay(600)
                onDone()
            }.onFailure {
                _message.value = UiMessage(
                    text = it.message ?: "Failed to request account deletion",
                    type = MessageType.ERROR
                )
                _isSubmitting.value = false
                clearMessage()
            }
        }
    }

    private fun clearMessage() {
        viewModelScope.launch {
            delay(2000)
            _message.value = null
        }
    }
}