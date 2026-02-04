package com.keshen.dinerdazeapp.ui.screens.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.keshen.dinerdazeapp.ui.components.PostSortFilterChips
import com.keshen.dinerdazeapp.ui.components.PostTagFilterChips
import com.keshen.dinerdazeapp.ui.components.UserPostRowCard
import com.keshen.dinerdazeapp.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    navController: NavHostController,
    viewModel: PostViewModel = hiltViewModel(),
) {
    val posts by viewModel.posts.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val search by viewModel.searchQuery.collectAsState()
    val selectedTag by viewModel.tag.collectAsState()
    val selectedSort by viewModel.sort.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = search,
            onValueChange = viewModel::onSearchChange,
            label = { Text("Search post title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Tags",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        PostTagFilterChips(
            selectedTag = selectedTag,
            onTagSelected = viewModel::onTagSelected
        )

        Spacer(modifier = Modifier.height(12.dp))

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
                        text = "No posts available",
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
                        UserPostRowCard(
                            post = post,
                            onClick = {
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("selectedPostId", post.uid)

                                navController.navigate(Screen.PostDetails)
                            }
                        )
                    }
                }
            }
        }
    }
}