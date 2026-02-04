package com.keshen.dinerdazeapp.ui.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.dinerdazeapp.data.model.Menu
import com.keshen.dinerdazeapp.service.MenuService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuDetailsViewModel @Inject constructor(
    private val menuService: MenuService
) : ViewModel() {

    private val _menu = MutableStateFlow<Menu?>(null)
    val menu = _menu.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadMenu(menuId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            runCatching {
                menuService.getMenu(menuId)
            }.onSuccess {
                _menu.value = it
            }.onFailure {
                _error.value = it.message ?: "Failed to load menu"
            }

            _isLoading.value = false
        }
    }
}
