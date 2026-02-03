package com.keshen.dinerdazeapp.ui.screens.admin.menu.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.dinerdazeapp.core.utils.MessageType
import com.keshen.dinerdazeapp.core.utils.UiMessage
import com.keshen.dinerdazeapp.core.utils.ValidationException
import com.keshen.dinerdazeapp.data.model.Diet
import com.keshen.dinerdazeapp.data.model.Menu
import com.keshen.dinerdazeapp.data.model.MenuCategory
import com.keshen.dinerdazeapp.data.model.Spiciness
import com.keshen.dinerdazeapp.service.MenuService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminAddMenuViewModel @Inject constructor(
    private val menuService: MenuService
) : ViewModel() {

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting = _isSubmitting.asStateFlow()

    private val _message = MutableStateFlow<UiMessage?>(null)
    val message = _message.asStateFlow()

    fun addMenu(
        name: String,
        description: String,
        price: String,
        category: MenuCategory,
        diet: Diet,
        spiciness: Spiciness,
        onSuccess: () -> Unit
    ) {
        _message.value = null
        _isSubmitting.value = true

        viewModelScope.launch {
            runCatching {
                val parsedPrice = validate(name, description, price)

                val menu = Menu(
                    uid = "",
                    name = name,
                    description = description,
                    price = parsedPrice,
                    category = category,
                    diet = diet,
                    spiciness = spiciness,
                    isAvailable = true,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = null
                )

                menuService.saveMenu(menu)
            }.onSuccess {
                _message.value = UiMessage(
                    "Menu added successfully",
                    MessageType.SUCCESS
                )
                delay(1200)
                onSuccess()
            }.onFailure { throwable ->
                showError(
                    throwable.message ?: "Failed to add menu"
                )
            }
        }
    }

    private fun validate(
        name: String,
        description: String,
        price: String
    ): Double {
        if (name.isBlank() || description.isBlank() || price.isBlank()) {
            throw ValidationException("Name, description and price must be filled")
        }

        return price.toDoubleOrNull()
            ?: throw ValidationException("Price must be a valid number")
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