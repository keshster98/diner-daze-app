package com.keshen.dinerdazeapp.ui.screens.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.keshen.dinerdazeapp.ui.components.MenuFilterChips
import com.keshen.dinerdazeapp.ui.components.MenuGridCard
import com.keshen.dinerdazeapp.ui.navigation.Screen
import com.keshen.dinerdazeapp.ui.screens.cart.CartViewModel

@Composable
fun MenuScreen(
    navController: NavController,
    viewModel: MenuViewModel = hiltViewModel(),
    cartViewModel: CartViewModel
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    val menus by viewModel.menus.collectAsState(initial = emptyList())

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val search by viewModel.searchQuery.collectAsState()
    val category by viewModel.category.collectAsState()
    val diet by viewModel.diet.collectAsState()
    val spiciness by viewModel.spiciness.collectAsState()

    val cartItems by cartViewModel.cartItems.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(menus) { menu ->
                        val quantity = cartItems[menu.uid]?.quantity ?: 0

                        MenuGridCard(
                            menu = menu,
                            quantity = quantity,
                            isLoggedIn = isLoggedIn,
                            onIncrease = { cartViewModel.increase(menu) },
                            onDecrease = { cartViewModel.decrease(menu) },
                            onAddToCart = { },
                            onClick = {
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("selectedMenuId", menu.uid)

                                navController.navigate(Screen.MenuDetails)
                            }
                        )
                    }
                }
            }
        }
    }
}
