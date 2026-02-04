package com.keshen.dinerdazeapp.ui.screens.admin.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.dinerdazeapp.data.model.Diet
import com.keshen.dinerdazeapp.data.model.Menu
import com.keshen.dinerdazeapp.data.model.MenuCategory
import com.keshen.dinerdazeapp.data.model.Spiciness
import com.keshen.dinerdazeapp.service.MenuService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminMenuViewModel @Inject constructor(
    private val menuService: MenuService
) : ViewModel() {

    /* ---------------- RAW DATA ---------------- */

    private val _allMenus = MutableStateFlow<List<Menu>>(emptyList())

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    /* ---------------- FILTER STATE ---------------- */

    private val _searchQuery = MutableStateFlow("")
    private val _category = MutableStateFlow<MenuCategory?>(null)
    private val _diet = MutableStateFlow<Diet?>(null)
    private val _spiciness = MutableStateFlow<Spiciness?>(null)

    val searchQuery = _searchQuery.asStateFlow()
    val category = _category.asStateFlow()
    val diet = _diet.asStateFlow()
    val spiciness = _spiciness.asStateFlow()

    /* ---------------- FILTERED RESULT ---------------- */

    val menus = combine(
        _allMenus,
        _searchQuery,
        _category,
        _diet,
        _spiciness
    ) { menus, query, category, diet, spiciness ->

        menus.filter { menu ->

            val matchesName =
                query.isBlank() ||
                        menu.name.contains(query, ignoreCase = true)

            val matchesCategory = category == null || menu.category == category
            val matchesDiet = diet == null || menu.diet == diet
            val matchesSpiciness = spiciness == null || menu.spiciness == spiciness

            matchesName &&
                    matchesCategory &&
                    matchesDiet &&
                    matchesSpiciness
        }
    }

    /* ---------------- INIT ---------------- */

    init {
        loadMenus()
    }

    /* ---------------- DATA LOAD ---------------- */

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

    /* ---------------- UI EVENTS ---------------- */

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

    fun reloadMenu() {
        loadMenus()
    }

    fun deleteMenu(menuId: String) {
        viewModelScope.launch {
            runCatching {
                menuService.deleteMenu(menuId)
            }.onSuccess {
                _allMenus.value = _allMenus.value.filterNot { it.uid == menuId }
            }.onFailure {
                _error.value = it.message ?: "Failed to delete menu"
            }
        }
    }

}