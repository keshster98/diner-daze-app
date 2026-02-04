package com.keshen.dinerdazeapp.ui.screens.admin.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.dinerdazeapp.data.model.Diet
import com.keshen.dinerdazeapp.data.model.Gender
import com.keshen.dinerdazeapp.data.model.Spiciness
import com.keshen.dinerdazeapp.data.model.User
import com.keshen.dinerdazeapp.service.UserProfileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminTotalUsersViewModel @Inject constructor(
    private val profileService: UserProfileService
) : ViewModel() {

    private val _allUsers = MutableStateFlow<List<User>>(emptyList())

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _gender = MutableStateFlow<Gender?>(null)
    private val _diet = MutableStateFlow<Diet?>(null)
    private val _spiciness = MutableStateFlow<Spiciness?>(null)

    val searchQuery = _searchQuery.asStateFlow()
    val gender = _gender.asStateFlow()
    val diet = _diet.asStateFlow()
    val spiciness = _spiciness.asStateFlow()

    val users = combine(
        _allUsers,
        _searchQuery,
        _gender,
        _diet,
        _spiciness
    ) { users, query, gender, diet, spiciness ->

        users.filter { user ->

            val matchesName =
                query.isBlank() ||
                        "${user.firstName} ${user.lastName}"
                            .contains(query, ignoreCase = true)

            val matchesGender = gender == null || user.gender == gender
            val matchesDiet = diet == null || user.diet == diet
            val matchesSpiciness = spiciness == null || user.spiciness == spiciness

            matchesName &&
                    matchesGender &&
                    matchesDiet &&
                    matchesSpiciness
        }
    }

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true

            runCatching {
                profileService.getAllUsers()
            }.onSuccess {
                _allUsers.value = it
            }.onFailure {
                _error.value = it.message ?: "Failed to load users"
            }

            _isLoading.value = false
        }
    }

    fun onSearchChange(value: String) {
        _searchQuery.value = value
    }

    fun onGenderSelected(value: Gender?) {
        _gender.value = value
    }

    fun onDietSelected(value: Diet?) {
        _diet.value = value
    }

    fun onSpicinessSelected(value: Spiciness?) {
        _spiciness.value = value
    }
}