package com.keshen.dinerdazeapp.ui.screens.admin.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keshen.dinerdazeapp.data.model.Post
import com.keshen.dinerdazeapp.data.model.PostTag
import com.keshen.dinerdazeapp.data.model.PostSort
import com.keshen.dinerdazeapp.service.PostService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminPostViewModel @Inject constructor(
    private val postService: PostService
) : ViewModel() {

    /* ---------- RAW DATA ---------- */

    private val _allPosts = MutableStateFlow<List<Post>>(emptyList())

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    /* ---------- FILTER STATE ---------- */

    private val _searchQuery = MutableStateFlow("")
    private val _tag = MutableStateFlow<PostTag?>(null)
    private val _sort = MutableStateFlow(PostSort.LATEST_POST)

    val searchQuery = _searchQuery.asStateFlow()
    val tag = _tag.asStateFlow()
    val sort = _sort.asStateFlow()

    /* ---------- FILTERED RESULT ---------- */

    val posts = combine(
        _allPosts,
        _searchQuery,
        _tag,
        _sort
    ) { posts, query, tag, sort ->

        posts
            .filter { post ->

                val matchesSearch =
                    query.isBlank() ||
                            post.title.contains(query, ignoreCase = true)

                val matchesTag =
                    tag == null || post.tag == tag

                matchesSearch && matchesTag
            }
            .let { filtered ->
                when (sort) {
                    PostSort.LATEST_POST ->
                        filtered.sortedByDescending { it.createdAt }

                    PostSort.LATEST_UPDATE ->
                        filtered.sortedByDescending { it.updatedAt }

                    PostSort.EARLIEST_POST ->
                        filtered.sortedBy { it.createdAt }
                }
            }
    }

    /* ---------- INIT ---------- */

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _isLoading.value = true

            runCatching {
                postService.getAllPosts()
            }.onSuccess {
                _allPosts.value = it
            }.onFailure {
                _error.value = it.message ?: "Failed to load posts"
            }

            _isLoading.value = false
        }
    }

    /* ---------- UI EVENTS ---------- */

    fun onSearchChange(value: String) {
        _searchQuery.value = value
    }

    fun onTagSelected(value: PostTag?) {
        _tag.value = value
    }

    fun onSortSelected(value: PostSort) {
        _sort.value = value
    }

    fun reloadPosts() {
        loadPosts()
    }
}