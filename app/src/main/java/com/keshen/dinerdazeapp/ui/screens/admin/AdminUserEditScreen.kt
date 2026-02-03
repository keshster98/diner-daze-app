package com.keshen.dinerdazeapp.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import com.keshen.dinerdazeapp.data.model.*
import com.keshen.dinerdazeapp.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUserEditScreen(
    navController: NavHostController,
    viewModel: AdminUserEditViewModel = hiltViewModel()
) {
    val userId =
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<String>("selectedUserId")

    LaunchedEffect(userId) {
        if (userId == null) {
            navController.popBackStack()
        } else {
            viewModel.loadUser(userId)
        }
    }

    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val message by viewModel.message.collectAsState()

    var e by remember(user) { mutableStateOf(user) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit User") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        when {
            isLoading -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            e == null -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                Alignment.Center
            ) {
                Text("User not found")
            }

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item { SectionTitle("Personal Information") }

                item {
                    EditableSection {
                        EditField("First Name", e!!.firstName) {
                            e = e!!.copy(firstName = it)
                        }
                        EditField("Last Name", e!!.lastName) {
                            e = e!!.copy(lastName = it)
                        }

                        EnumDropdownField(
                            label = "Gender",
                            options = Gender.entries,
                            selected = e!!.gender,
                            onSelected = { e = e!!.copy(gender = it) }
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        InfoNote("Email change feature is pending")

                        OutlinedTextField(
                            value = e!!.email,
                            onValueChange = {},
                            enabled = false,
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        EditField("Phone", e!!.phone) {
                            e = e!!.copy(phone = it)
                        }
                    }
                }

                item { SectionTitle("Diet Information") }

                item {
                    EditableSection {
                        EnumDropdownField(
                            label = "Dietary Preference",
                            options = Diet.entries,
                            selected = e!!.diet,
                            onSelected = { e = e!!.copy(diet = it) }
                        )

                        EnumDropdownField(
                            label = "Spiciness Preference",
                            options = Spiciness.entries,
                            selected = e!!.spiciness,
                            onSelected = { e = e!!.copy(spiciness = it) }
                        )
                    }
                }

                item {
                    message?.let { uiMessage ->
                        val isError = uiMessage.type == MessageType.ERROR

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = if (isError) Color(0xFFFFEBEE) else Color(0xFFE8F5E9),
                                    shape = MaterialTheme.shapes.medium
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isError) Color(0xFFD32F2F) else Color(0xFF388E3C),
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(12.dp)
                        ) {
                            Text(
                                text = uiMessage.text,
                                color = if (isError) Color(0xFFD32F2F) else Color(0xFF388E3C),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                item {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = viewModel.isInfoDirty(e!!) && !isSaving,
                        onClick = { viewModel.updateUser(e!!) }
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Save Changes")
                        }
                    }
                }
            }
        }
    }
}
