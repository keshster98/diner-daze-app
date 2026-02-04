package com.keshen.dinerdazeapp.ui.screens.admin.deletion

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.keshen.dinerdazeapp.ui.components.AdminDeletionRequestRow
import com.keshen.dinerdazeapp.ui.components.GenderFilterChips
import com.keshen.dinerdazeapp.ui.components.RequestSortChips

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDeletionRequestsScreen(
    navController: NavHostController,
    viewModel: AdminDeletionRequestsViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val search by viewModel.search.collectAsState()
    val gender by viewModel.gender.collectAsState()
    val sort by viewModel.sort.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Deletion Requests") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, "Back")
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
                label = { Text("Search user name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Gender",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            GenderFilterChips(
                selected = gender,
                onSelected = viewModel::onGenderSelected
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Sort By",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            RequestSortChips(
                selected = sort,
                onSelected = viewModel::onSortSelected
            )

            Spacer(Modifier.height(16.dp))

            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> {
                    Text(error!!, color = MaterialTheme.colorScheme.error)
                }

                users.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text("No deletion requests")
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(users) { user ->
                            AdminDeletionRequestRow(
                                user = user,
                                onDelete = { viewModel.deleteUser(user.uid) }
                            )
                        }
                    }
                }
            }
        }
    }
}
