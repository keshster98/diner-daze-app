package com.keshen.dinerdazeapp.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.dinerdazeapp.core.constants.PHONE_REGEX
import com.keshen.dinerdazeapp.core.utils.*
import com.keshen.dinerdazeapp.data.model.User
import com.keshen.dinerdazeapp.service.UserProfileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminUserEditViewModel @Inject constructor(
    private val profileService: UserProfileService
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private var originalUser: User? = null

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _message = MutableStateFlow<UiMessage?>(null)
    val message = _message.asStateFlow()

    fun loadUser(uid: String) {
        viewModelScope.launch {
            _isLoading.value = true

            runCatching {
                profileService.getProfile(uid)
            }.onSuccess {
                originalUser = it
                _user.value = it
            }.onFailure {
                _message.value = UiMessage(
                    it.message ?: "Failed to load user",
                    MessageType.ERROR
                )
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

        if (!PHONE_REGEX.matches(user.phone)) {
            throw ValidationException(
                "Please enter a valid Malaysian phone number"
            )
        }
    }

    fun updateUser(e: User) = save {
        validate(e)

        profileService.updateProfile(
            uid = e.uid,
            firstName = e.firstName,
            lastName = e.lastName,
            gender = e.gender,
            phone = e.phone,
            diet = e.diet,
            spiciness = e.spiciness
        )

        originalUser = originalUser!!.copy(
            firstName = e.firstName,
            lastName = e.lastName,
            phone = e.phone,
            gender = e.gender,
            diet = e.diet,
            spiciness = e.spiciness
        )
    }

    private fun save(block: suspend () -> Unit) {
        _isSaving.value = true

        viewModelScope.launch {
            runCatching { block() }
                .onSuccess {
                    _user.value = originalUser
                    _message.value =
                        UiMessage("User updated successfully", MessageType.SUCCESS)
                }
                .onFailure {
                    _message.value =
                        UiMessage(it.message ?: "Update failed", MessageType.ERROR)
                }

            delay(1500)
            _message.value = null
            _isSaving.value = false
        }
    }
}
