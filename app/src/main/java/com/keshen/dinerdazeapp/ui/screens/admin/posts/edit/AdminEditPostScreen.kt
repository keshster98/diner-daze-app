package com.keshen.dinerdazeapp.ui.screens.admin.posts.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.keshen.dinerdazeapp.core.utils.MessageType
import com.keshen.dinerdazeapp.data.model.PostTag
import com.keshen.dinerdazeapp.ui.components.CardSection
import com.keshen.dinerdazeapp.ui.components.EnumDropdownField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditPostScreen(
    navController: NavHostController,
    viewModel: AdminEditPostViewModel = hiltViewModel()
) {

    val postId =
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<String>("selectedPostId")

    LaunchedEffect(postId) {
        if (postId == null) {
            navController.popBackStack()
        } else {
            viewModel.loadPost(postId)
        }
    }

    val post by viewModel.post.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val message by viewModel.message.collectAsState()

    var e by remember(post) { mutableStateOf(post) }

    Column(modifier = Modifier.fillMaxSize()) {

        /* ---------------- TOP BAR ---------------- */

        TopAppBar(
            title = { Text("Edit Post") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        /* ---------------- CONTENT ---------------- */

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator()
                }

                e == null -> {
                    Text("Post not found")
                }

                else -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        CardSection {

                            OutlinedTextField(
                                value = e!!.title,
                                onValueChange = { e = e!!.copy(title = it) },
                                label = { Text("Title") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = e!!.description,
                                onValueChange = { e = e!!.copy(description = it) },
                                label = { Text("Description") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = false,
                                minLines = 3,
                                maxLines = 10
                            )

                            EnumDropdownField(
                                label = "Tag",
                                options = PostTag.entries,
                                selected = e!!.tag,
                                onSelected = { e = e!!.copy(tag = it) }
                            )
                        }

                        message?.let { uiMessage ->
                            val isError = uiMessage.type == MessageType.ERROR

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isError)
                                            Color(0xFFFFEBEE)
                                        else
                                            Color(0xFFE8F5E9),
                                        MaterialTheme.shapes.medium
                                    )
                                    .border(
                                        1.dp,
                                        if (isError)
                                            Color(0xFFD32F2F)
                                        else
                                            Color(0xFF388E3C),
                                        MaterialTheme.shapes.medium
                                    )
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = uiMessage.text,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }

        /* ---------------- BOTTOM ACTIONS ---------------- */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = { navController.popBackStack() }
            ) {
                Text("Cancel")
            }

            Button(
                modifier = Modifier.weight(1f),
                enabled = e != null && viewModel.isDirty(e!!) && !isSaving,
                onClick = {
                    viewModel.updatePost(e!!) {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("post_edited", true)

                        navController.popBackStack()
                    }
                }
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Edit Post")
                }
            }
        }
    }
}
