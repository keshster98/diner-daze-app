package com.keshen.dinerdazeapp.core.utils

data class UiMessage(
    val text: String,
    val type: MessageType
)

enum class MessageType {
    ERROR, SUCCESS
}