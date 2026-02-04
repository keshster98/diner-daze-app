package com.keshen.dinerdazeapp.ui.screens.admin.deletion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.dinerdazeapp.data.model.Gender
import com.keshen.dinerdazeapp.data.model.User
import com.keshen.dinerdazeapp.service.UserProfileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDeletionRequestsViewModel @Inject constructor(
    private val profileService: UserProfileService
) : ViewModel() {

    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)

    val isLoading = _isLoading.asStateFlow()
    val error = _error.asStateFlow()

    private val _search = MutableStateFlow("")
    private val _gender = MutableStateFlow<Gender?>(null)
    private val _sort = MutableStateFlow(RequestSort.LATEST)

    val search = _search.asStateFlow()
    val gender = _gender.asStateFlow()
    val sort = _sort.asStateFlow()

    val users = combine(
        _allUsers, _search, _gender, _sort
    ) { users, search, gender, sort ->

        users
            .filter {
                val nameMatch =
                    search.isBlank() ||
                            "${it.firstName} ${it.lastName}"
                                .contains(search, ignoreCase = true)

                val genderMatch =
                    gender == null || it.gender == gender

                nameMatch && genderMatch
            }
            .sortedWith(
                when (sort) {
                    RequestSort.EARLIEST ->
                        compareBy { it.deleteRequestedAt }
                    RequestSort.LATEST ->
                        compareByDescending { it.deleteRequestedAt }
                }
            )
    }

    init {
        loadRequests()
    }

    fun loadRequests() {
        viewModelScope.launch {
            _isLoading.value = true

            runCatching {
                profileService.getDeletionRequests()
            }.onSuccess {
                _allUsers.value = it
            }.onFailure {
                _error.value = it.message ?: "Failed to load deletion requests"
            }

            _isLoading.value = false
        }
    }

    fun deleteUser(uid: String) {
        viewModelScope.launch {
            profileService.deleteUserDocument(uid)
            loadRequests()
        }
    }

    fun onSearchChange(v: String) { _search.value = v }
    fun onGenderSelected(v: Gender?) { _gender.value = v }
    fun onSortSelected(v: RequestSort) { _sort.value = v }
}

enum class RequestSort {
    EARLIEST, LATEST
}
