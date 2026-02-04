package com.keshen.dinerdazeapp.ui.screens.news

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailsScreen(
    navController: NavHostController,
    homeViewModel: PostViewModel = hiltViewModel()
) {
    var postId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        postId =
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>("selectedPostId")

        if (postId == null) {
            navController.popBackStack()
        }
    }

    val posts by homeViewModel.posts.collectAsState(initial = emptyList())

    val post = remember(posts, postId) {
        posts.firstOrNull { it.uid == postId }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Post Details") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        when {
            postId == null -> {
                // handled by popBackStack
            }

            post == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Post not found")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    item {
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    post.tag.name.lowercase()
                                        .replaceFirstChar { it.uppercase() }
                                )
                            }
                        )
                    }

                    item {
                        Text(
                            text = post.title,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    item {
                        Text(
                            text = post.description,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}


