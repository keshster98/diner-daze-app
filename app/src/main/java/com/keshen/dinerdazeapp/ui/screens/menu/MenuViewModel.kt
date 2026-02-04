package com.keshen.dinerdazeapp.ui.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.dinerdazeapp.data.model.*
import com.keshen.dinerdazeapp.service.AuthService
import com.keshen.dinerdazeapp.service.MenuService
import com.keshen.dinerdazeapp.service.UserProfileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val menuService: MenuService,
    private val authService: AuthService,
    private val profileService: UserProfileService
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(authService.isLoggedIn())
    val isLoggedIn = _isLoggedIn.asStateFlow()


    private val _allMenus = MutableStateFlow<List<Menu>>(emptyList())

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _category = MutableStateFlow<MenuCategory?>(null)
    private val _diet = MutableStateFlow<Diet?>(null)
    private val _spiciness = MutableStateFlow<Spiciness?>(null)

    val searchQuery = _searchQuery.asStateFlow()
    val category = _category.asStateFlow()
    val diet = _diet.asStateFlow()
    val spiciness = _spiciness.asStateFlow()

    val menus = combine(
        _allMenus,
        _searchQuery,
        _category,
        _diet,
        _spiciness
    ) { menus, query, category, diet, spiciness ->
        menus.filter { menu ->
            val matchesSearch =
                query.isBlank() || menu.name.contains(query, ignoreCase = true)

            val matchesCategory =
                category == null || menu.category == category

            val matchesDiet =
                diet == null || menu.diet == Diet.ANY || menu.diet == diet

            val matchesSpiciness =
                spiciness == null || menu.spiciness == Spiciness.ANY || menu.spiciness == spiciness

            matchesSearch &&
                    matchesCategory &&
                    matchesDiet &&
                    matchesSpiciness
        }
    }

    init {
        loadMenus()
        loadUserPreferences()
    }

    private fun loadMenus() {
        viewModelScope.launch {
            _isLoading.value = true
            runCatching {
                menuService.getAllMenu()
            }.onSuccess {
                _allMenus.value = it
            }.onFailure {
                _error.value = it.message ?: "Failed to load menu"
            }
            _isLoading.value = false
        }
    }

    fun onSearchChange(value: String) {
        _searchQuery.value = value
    }

    fun onCategorySelected(value: MenuCategory?) {
        _category.value = value
    }

    fun onDietSelected(value: Diet?) {
        _diet.value = value
    }

    fun onSpicinessSelected(value: Spiciness?) {
        _spiciness.value = value
    }

    private fun loadUserPreferences() {
        viewModelScope.launch {
            if (!authService.isLoggedIn()) return@launch
            val uid = authService.uid()
            runCatching {
                profileService.getProfile(uid)
            }.onSuccess { user ->
                applyUserPreferences(user)
            }
        }
    }

    private fun applyUserPreferences(user: User) {
        _diet.value = if (user.diet == Diet.ANY) null else user.diet
        _spiciness.value = if (user.spiciness == Spiciness.ANY) null else user.spiciness
    }
}