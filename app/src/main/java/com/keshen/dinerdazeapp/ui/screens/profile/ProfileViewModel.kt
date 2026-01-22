package com.keshen.dinerdazeapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.dinerdazeapp.core.utils.MessageType
import com.keshen.dinerdazeapp.core.utils.UiMessage
import com.keshen.dinerdazeapp.core.utils.ValidationException
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
class ProfileViewModel @Inject constructor(
    private val authService: AuthService,
    private val profileService: UserProfileService
): ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private var originalUser: User? = null

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _message = MutableStateFlow<UiMessage?>(null)
    val message = _message.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            runCatching {
                profileService.getProfile(authService.uid())
            }.onSuccess {
                originalUser = it
                _user.value = it
            }.onFailure {
                _message.value = UiMessage(it.message ?: "Failed to load profile", MessageType.ERROR)
            }
            _isLoading.value = false
        }
    }

    fun isInfoDirty(e: User): Boolean {
        return originalUser?.let {
            e.firstName != it.firstName ||
                    e.lastName != it.lastName ||
                    e.phone != it.phone ||
                    e.gender != it.gender ||
                    e.diet != it.diet ||
                    e.spiciness != it.spiciness
        } ?: false
    }

    private fun validate(user: User) {
        if (
            user.firstName.isBlank() ||
            user.lastName.isBlank() ||
            user.phone.isBlank()
        ) {
            throw ValidationException(
                "First name, last name and phone number cannot be left blank!"
            )
        }

        if (!user.phone.all { it.isDigit() }) {
            throw ValidationException(
                "Phone number must contain digits only!"
            )
        }
    }

    fun updateProfile(e: User) = saveNewProfile {
        validate(e)

        profileService.updateProfile(e.uid, e.firstName, e.lastName, e.gender, e.phone, e.diet, e.spiciness)

        originalUser = originalUser!!.copy(
            firstName = e.firstName,
            lastName = e.lastName,
            phone = e.phone,
            gender = e.gender,
            diet = e.diet,
            spiciness = e.spiciness
        )
    }

    private fun saveNewProfile(block: suspend () -> Unit) {
        _isSaving.value = true
        viewModelScope.launch {
            runCatching { block() }
                .onSuccess {
                    _user.value = originalUser
                    _message.value = UiMessage("Changes saved", MessageType.SUCCESS)
                }
                .onFailure {
                    _message.value = UiMessage(it.message ?: "Save failed", MessageType.ERROR)
                }
            delay(1500)
            _message.value = null
            _isSaving.value = false
        }
    }
}