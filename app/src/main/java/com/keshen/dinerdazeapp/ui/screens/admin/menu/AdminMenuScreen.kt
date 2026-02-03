package com.keshen.dinerdazeapp.ui.screens.admin.menu

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
import com.keshen.dinerdazeapp.ui.components.MenuFilterChips
import com.keshen.dinerdazeapp.ui.components.MenuRowCard
import com.keshen.dinerdazeapp.ui.navigation.Screen
import com.keshen.dinerdazeapp.ui.screens.admin.menu.AdminMenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMenuScreen(
    navController: NavHostController,
    viewModel: AdminMenuViewModel = hiltViewModel()
) {
    val menus by viewModel.menus.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val search by viewModel.searchQuery.collectAsState()
    val category by viewModel.category.collectAsState()
    val diet by viewModel.diet.collectAsState()
    val spiciness by viewModel.spiciness.collectAsState()

    val menuAdded =
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow("menu_added", false)
            ?.collectAsState()

    val menuEdited =
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow("menu_edited", false)
            ?.collectAsState()

    LaunchedEffect(menuAdded?.value, menuEdited?.value) {
        if (menuAdded?.value == true || menuEdited?.value == true) {

            viewModel.reloadMenu()

            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("menu_added", false)

            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("menu_edited", false)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        /* ---------- TOP BAR (FLOATS ON TOP) ---------- */

        TopAppBar(
            title = { Text("Menu") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(
                    onClick = { navController.navigate(Screen.AdminAddMenu) }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Menu")
                }
            }
        )

        /* ---------- CONTENT (OFFSET BELOW TOP BAR) ---------- */

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = search,
                onValueChange = viewModel::onSearchChange,
                label = { Text("Search menu") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            MenuFilterChips(
                selectedCategory = category,
                selectedDiet = diet,
                selectedSpiciness = spiciness,
                onCategorySelected = viewModel::onCategorySelected,
                onDietSelected = viewModel::onDietSelected,
                onSpicinessSelected = viewModel::onSpicinessSelected
            )

            Spacer(Modifier.height(16.dp))

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

                menus.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No menu items found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(menus) { menu ->
                            MenuRowCard(
                                menu = menu,
                                onClick = {
                                    navController.currentBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("selectedMenuId", menu.uid)

                                    navController.navigate(Screen.AdminEditMenu)
                                },
                                onDelete = {
                                viewModel.deleteMenu(menu.uid)
                            })
                        }
                    }
                }
            }
        }
    }
}
