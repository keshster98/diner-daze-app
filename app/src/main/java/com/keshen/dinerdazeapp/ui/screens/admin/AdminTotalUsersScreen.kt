package com.keshen.dinerdazeapp.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.keshen.dinerdazeapp.ui.components.FilterChipsRow
import com.keshen.dinerdazeapp.ui.components.UserRowCard
import com.keshen.dinerdazeapp.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTotalUsersScreen(
    navController: NavHostController,
    viewModel: AdminTotalUsersViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val search by viewModel.searchQuery.collectAsState()
    val gender by viewModel.gender.collectAsState()
    val diet by viewModel.diet.collectAsState()
    val spiciness by viewModel.spiciness.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Total Users") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            /* ---------------- SEARCH ---------------- */

            OutlinedTextField(
                value = search,
                onValueChange = viewModel::onSearchChange,
                label = { Text("Search by name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            /* ---------------- FILTER CHIPS ---------------- */

            FilterChipsRow(
                selectedGender = gender,
                selectedDiet = diet,
                selectedSpiciness = spiciness,
                onGenderSelected = viewModel::onGenderSelected,
                onDietSelected = viewModel::onDietSelected,
                onSpicinessSelected = viewModel::onSpicinessSelected,
            )

            Spacer(Modifier.height(16.dp))

            /* ---------------- CONTENT ---------------- */

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

                users.isEmpty() -> {
                    Text(
                        text = "No users found",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(users) { user ->
                            UserRowCard(
                                user = user,
                                onClick = {
                                    navController.currentBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("selectedUserId", user.uid)
                                    navController.navigate(Screen.AdminUserEdit)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}