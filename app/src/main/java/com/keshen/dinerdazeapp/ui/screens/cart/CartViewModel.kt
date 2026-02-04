package com.keshen.dinerdazeapp.ui.screens.cart

import androidx.lifecycle.ViewModel
import com.keshen.dinerdazeapp.data.model.CartItem
import com.keshen.dinerdazeapp.data.model.Menu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {

    private val _cartItems = MutableStateFlow<Map<String, CartItem>>(emptyMap())
    val cartItems = _cartItems.asStateFlow()

    private val _tableNumber = MutableStateFlow("")
    val tableNumber = _tableNumber.asStateFlow()

    private val _paymentSuccess = MutableStateFlow(false)
    val paymentSuccess = _paymentSuccess.asStateFlow()

    fun increase(menu: Menu) {
        val current = _cartItems.value[menu.uid]?.quantity ?: 0
        if (current < 5) setQuantity(menu, current + 1)
    }

    fun decrease(menu: Menu) {
        val current = _cartItems.value[menu.uid]?.quantity ?: 0
        setQuantity(menu, current - 1)
    }

    private fun setQuantity(menu: Menu, quantity: Int) {
        if (quantity <= 0) {
            remove(menu.uid)
            return
        }

        val safeQty = quantity.coerceIn(1, 5)

        _cartItems.value = _cartItems.value.toMutableMap().apply {
            put(menu.uid, CartItem(menu, safeQty))
        }
    }

    fun remove(menuId: String) {
        _cartItems.value = _cartItems.value.toMutableMap().apply {
            remove(menuId)
        }
    }

    fun clearCart() {
        _cartItems.value = emptyMap()
        _tableNumber.value = ""
    }

    fun onTableNumberChange(value: String) {
        _tableNumber.value = value.filter { it.isDigit() }
    }


    fun payNow() {
        _paymentSuccess.value = true
        clearCart()
    }

    fun resetPaymentState() {
        _paymentSuccess.value = false
    }

    val totalItems: Int
        get() = _cartItems.value.values.sumOf { it.quantity }

    val totalPrice: Double
        get() = _cartItems.value.values.sumOf {
            it.menu.price * it.quantity
        }
}
