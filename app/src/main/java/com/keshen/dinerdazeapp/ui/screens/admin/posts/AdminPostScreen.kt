package com.keshen.dinerdazeapp.ui.screens.admin.posts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.keshen.dinerdazeapp.ui.components.AdminPostRowCard
import com.keshen.dinerdazeapp.ui.components.PostSortFilterChips
import com.keshen.dinerdazeapp.ui.components.PostTagFilterChips
import com.keshen.dinerdazeapp.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPostScreen(
    navController: NavHostController,
    viewModel: AdminPostViewModel = hiltViewModel()
) {

    val posts by viewModel.posts.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val search by viewModel.searchQuery.collectAsState()
    val selectedTag by viewModel.tag.collectAsState()
    val selectedSort by viewModel.sort.collectAsState()

    val postAdded =
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow("post_added", false)
            ?.collectAsState()

    val postEdited =
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow("post_edited", false)
            ?.collectAsState()

    LaunchedEffect(postAdded?.value, postEdited?.value) {
        if (postAdded?.value == true || postEdited?.value == true) {

            viewModel.reloadPosts()

            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("post_added", false)

            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("post_edited", false)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Posts") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = { navController.navigate(Screen.AdminAddPost) }
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Post"
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = search,
                onValueChange = viewModel::onSearchChange,
                label = { Text("Search post title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Tags",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            PostTagFilterChips(
                selectedTag = selectedTag,
                onTagSelected = viewModel::onTagSelected
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Sort By",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            PostSortFilterChips(
                selectedSort = selectedSort,
                onSortSelected = viewModel::onSortSelected
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> {
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                posts.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No posts found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(posts) { post ->
                            AdminPostRowCard(
                                post = post,
                                onClick = {
                                    navController.currentBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("selectedPostId", post.uid)

                                    navController.navigate(Screen.AdminEditPost)
                                },
                                onDelete = {
                                    viewModel.deletePost(post.uid)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}