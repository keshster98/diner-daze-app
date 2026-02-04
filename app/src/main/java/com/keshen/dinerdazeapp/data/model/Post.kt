package com.keshen.dinerdazeapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val uid: String = "",
    val title: String = "",
    val description: String = "",
    val tag: PostTag = PostTag.GENERAL,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null
)

enum class PostTag {
    GENERAL,
    TIMING,
    MENU,
    PROMOTION,
    DISRUPTION
}

enum class PostSort {
    LATEST_POST,
    LATEST_UPDATE,
    EARLIEST_POST
}