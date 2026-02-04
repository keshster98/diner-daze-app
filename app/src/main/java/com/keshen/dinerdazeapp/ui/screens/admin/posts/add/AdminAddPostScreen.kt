package com.keshen.dinerdazeapp.ui.screens.admin.posts.add

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
fun AdminAddPostScreen(
    navController: NavHostController,
    viewModel: AdminAddPostViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf<PostTag?>(null) }

    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val message by viewModel.message.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Add Post") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                CardSection {

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false,
                        minLines = 3,
                        maxLines = 10
                    )

                    EnumDropdownField(
                        label = "Tag",
                        options = PostTag.entries,
                        selected = tag,
                        onSelected = { tag = it }
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedButton(
                modifier = Modifier.weight(1f),
                enabled = !isSubmitting,
                onClick = { navController.popBackStack() }
            ) {
                Text("Cancel")
            }

            Button(
                modifier = Modifier.weight(1f),
                enabled = !isSubmitting,
                onClick = {
                    viewModel.addPost(
                        title = title,
                        description = description,
                        tag = tag,
                        onSuccess = {
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("post_added", true)

                            navController.popBackStack()
                        }
                    )
                }
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Add Post")
                }
            }
        }
    }
}
