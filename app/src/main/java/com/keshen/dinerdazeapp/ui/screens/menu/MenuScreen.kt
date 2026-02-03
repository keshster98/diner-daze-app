package com.keshen.dinerdazeapp.ui.screens.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.keshen.dinerdazeapp.ui.components.MenuFilterChips
import com.keshen.dinerdazeapp.ui.components.MenuGridCard

@Composable
fun MenuScreen(
    viewModel: MenuViewModel = hiltViewModel()
) {
    val menus by viewModel.menus.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val search by viewModel.searchQuery.collectAsState()
    val category by viewModel.category.collectAsState()
    val diet by viewModel.diet.collectAsState()
    val spiciness by viewModel.spiciness.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        /* ---------- SEARCH ---------- */

        OutlinedTextField(
            value = search,
            onValueChange = viewModel::onSearchChange,
            label = { Text("Search menu") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        /* ---------- FILTER CHIPS ---------- */

        MenuFilterChips(
            selectedCategory = category,
            selectedDiet = diet,
            selectedSpiciness = spiciness,
            onCategorySelected = viewModel::onCategorySelected,
            onDietSelected = viewModel::onDietSelected,
            onSpicinessSelected = viewModel::onSpicinessSelected
        )

        Spacer(Modifier.height(16.dp))

        /* ---------- CONTENT ---------- */

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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(menus) { menu ->
                        MenuGridCard(
                            menu = menu,
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}
