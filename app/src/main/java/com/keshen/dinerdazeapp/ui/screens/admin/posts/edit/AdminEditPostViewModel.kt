package com.keshen.dinerdazeapp.ui.screens.admin.posts.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.dinerdazeapp.core.utils.MessageType
import com.keshen.dinerdazeapp.core.utils.UiMessage
import com.keshen.dinerdazeapp.core.utils.ValidationException
import com.keshen.dinerdazeapp.data.model.Post
import com.keshen.dinerdazeapp.service.PostService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminEditPostViewModel @Inject constructor(
    private val postService: PostService
) : ViewModel() {

    private val _post = MutableStateFlow<Post?>(null)
    val post = _post.asStateFlow()

    private var originalPost: Post? = null

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _message = MutableStateFlow<UiMessage?>(null)
    val message = _message.asStateFlow()

    /* ---------------- LOAD ---------------- */

    fun loadPost(postId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            runCatching {
                postService.getPost(postId)
            }.onSuccess {
                originalPost = it
                _post.value = it
            }.onFailure {
                _message.value =
                    UiMessage(it.message ?: "Failed to load post", MessageType.ERROR)
            }

            _isLoading.value = false
        }
    }

    /* ---------------- DIRTY CHECK ---------------- */

    fun isDirty(e: Post): Boolean {
        return originalPost?.let {
            e.title != it.title ||
                    e.description != it.description ||
                    e.tag != it.tag
        } ?: false
    }

    /* ---------------- UPDATE ---------------- */

    fun updatePost(e: Post, onSuccess: () -> Unit) {
        _isSaving.value = true
        _message.value = null

        viewModelScope.launch {
            runCatching {
                validate(e)

                postService.updatePost(
                    uid = e.uid,
                    title = e.title,
                    description = e.description,
                    tag = e.tag
                )

                originalPost = e.copy(
                    updatedAt = System.currentTimeMillis()
                )
            }.onSuccess {
                _post.value = originalPost
                _message.value =
                    UiMessage("Post updated successfully", MessageType.SUCCESS)
                delay(1200)
                onSuccess()
            }.onFailure {
                _message.value =
                    UiMessage(it.message ?: "Failed to update post", MessageType.ERROR)
                _isSaving.value = false
                clearMessage()
            }
        }
    }

    /* ---------------- VALIDATION ---------------- */

    private fun validate(post: Post) {
        if (
            post.title.isBlank() ||
            post.description.isBlank()
        ) {
            throw ValidationException("Please fill up all the fields")
        }
    }

    private fun clearMessage() {
        viewModelScope.launch {
            delay(2000)
            _message.value = null
        }
    }
}
