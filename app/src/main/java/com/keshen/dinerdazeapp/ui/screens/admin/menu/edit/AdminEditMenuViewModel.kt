package com.keshen.dinerdazeapp.ui.screens.admin.menu.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.dinerdazeapp.core.utils.MessageType
import com.keshen.dinerdazeapp.core.utils.UiMessage
import com.keshen.dinerdazeapp.core.utils.ValidationException
import com.keshen.dinerdazeapp.data.model.Menu
import com.keshen.dinerdazeapp.service.MenuService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminEditMenuViewModel @Inject constructor(
    private val menuService: MenuService
) : ViewModel() {

    private val _menu = MutableStateFlow<Menu?>(null)
    val menu = _menu.asStateFlow()

    private var originalMenu: Menu? = null

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _message = MutableStateFlow<UiMessage?>(null)
    val message = _message.asStateFlow()

    /* ---------------- LOAD ---------------- */

    fun loadMenu(menuId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            runCatching {
                menuService.getMenu(menuId)
            }.onSuccess {
                originalMenu = it
                _menu.value = it
            }.onFailure {
                _message.value =
                    UiMessage(it.message ?: "Failed to load menu", MessageType.ERROR)
            }

            _isLoading.value = false
        }
    }

    /* ---------------- DIRTY CHECK ---------------- */

    fun isDirty(e: Menu): Boolean {
        return originalMenu?.let {
            e.name != it.name ||
                    e.description != it.description ||
                    e.price != it.price ||
                    e.category != it.category ||
                    e.diet != it.diet ||
                    e.spiciness != it.spiciness ||
                    e.isAvailable != it.isAvailable
        } ?: false
    }

    /* ---------------- UPDATE ---------------- */

    fun updateMenu(e: Menu, onSuccess: () -> Unit) {
        _isSaving.value = true
        _message.value = null

        viewModelScope.launch {
            runCatching {
                validate(e)

                menuService.updateMenu(
                    uid = e.uid,
                    name = e.name,
                    description = e.description,
                    price = e.price,
                    category = e.category,
                    diet = e.diet,
                    spiciness = e.spiciness,
                    isAvailable = e.isAvailable
                )

                originalMenu = e.copy(
                    updatedAt = System.currentTimeMillis()
                )
            }.onSuccess {
                _menu.value = originalMenu
                _message.value = UiMessage("Menu updated successfully", MessageType.SUCCESS)
                delay(1200)
                onSuccess()
            }.onFailure {
                _message.value =
                    UiMessage(it.message ?: "Failed to update menu", MessageType.ERROR)
                _isSaving.value = false
                clearMessage()
            }
        }
    }

    /* ---------------- VALIDATION ---------------- */

    private fun validate(menu: Menu) {
        if (
            menu.name.isBlank() ||
            menu.description.isBlank()
        ) {
            throw ValidationException("Name and description cannot be empty")
        }

        if (menu.price <= 0.0) {
            throw ValidationException("Price must be greater than 0")
        }
    }

    private fun clearMessage() {
        viewModelScope.launch {
            delay(2000)
            _message.value = null
        }
    }
}