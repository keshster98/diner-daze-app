package com.keshen.dinerdazeapp.data.model

data class User (
    // Firebase details
    val uid: String = "",
    val role: Role = Role.USER,
    val profileFilled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),

    // Personal details
    val firstName: String = "",
    val lastName: String = "",
    val gender: Gender = Gender.PREFER_NOT_TO_SAY,
    val email: String = "",
    val phone: String = "",

    // Extra details
    val diet: Diet = Diet.ANY,
    val spiciness: Spiciness = Spiciness.ANY
)

enum class Role {
    USER, CHEF, ADMIN
}

enum class Gender {
    PREFER_NOT_TO_SAY, MALE, FEMALE
}

enum class Diet {
    ANY, VEGETARIAN, NON_VEGETARIAN
}

enum class Spiciness {
    ANY, SPICY, NON_SPICY
}