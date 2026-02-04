package com.keshen.dinerdazeapp.ui.screens.admin.posts.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.dinerdazeapp.core.utils.MessageType
import com.keshen.dinerdazeapp.core.utils.UiMessage
import com.keshen.dinerdazeapp.core.utils.ValidationException
import com.keshen.dinerdazeapp.data.model.Post
import com.keshen.dinerdazeapp.data.model.PostTag
import com.keshen.dinerdazeapp.service.PostService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminAddPostViewModel @Inject constructor(
    private val postService: PostService
) : ViewModel() {

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting = _isSubmitting.asStateFlow()

    private val _message = MutableStateFlow<UiMessage?>(null)
    val message = _message.asStateFlow()

    fun addPost(
        title: String,
        description: String,
        tag: PostTag?,
        onSuccess: () -> Unit
    ) {
        _message.value = null
        _isSubmitting.value = true

        viewModelScope.launch {
            runCatching {

                validate(title, description, tag)

                val post = Post(
                    uid = "",
                    title = title,
                    description = description,
                    tag = tag!!,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = null
                )

                postService.savePost(post)

            }.onSuccess {
                _message.value = UiMessage(
                    "Post added successfully",
                    MessageType.SUCCESS
                )
                delay(1200)
                onSuccess()
            }.onFailure { throwable ->
                showError(
                    throwable.message ?: "Failed to add post"
                )
            }
        }
    }

    private fun validate(
        title: String,
        description: String,
        tag: PostTag?
    ) {
        if (
            title.isBlank() ||
            description.isBlank() ||
            tag == null
        ) {
            throw ValidationException("Please fill up all the fields")
        }
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
