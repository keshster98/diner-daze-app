package com.keshen.dinerdazeapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val uid: String = "",
    val title: String = "",
    val description: String = "",
    val tag: PostTag = PostTag.OPERATING_HOURS,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null
)

enum class PostTag {
    OPERATING_HOURS,
    MENU,
    PROMOTION,
    DISRUPTION
}