package com.keshen.dinerdazeapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Menu(
    val uid: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,

    val category: MenuCategory = MenuCategory.MAIN,
    val diet: Diet = Diet.ANY,
    val spiciness: Spiciness = Spiciness.ANY,

    val preparationTime: String = "",
    val servingSize: String = "",
    val ingredients: List<String> = emptyList(),

    val isAvailable: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null,
)

enum class MenuCategory {
    MAIN,
    SIDE,
    DRINK,
    DESSERT
}