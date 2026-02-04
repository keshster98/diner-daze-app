package com.keshen.dinerdazeapp.ui.screens.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.keshen.dinerdazeapp.ui.components.SectionTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuDetailsScreen(
    navController: NavHostController,
    viewModel: MenuDetailsViewModel = hiltViewModel()
) {
    val menuId =
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<String>("selectedMenuId")

    LaunchedEffect(menuId) {
        if (menuId == null) {
            navController.popBackStack()
        } else {
            viewModel.loadMenu(menuId)
        }
    }

    val menu by viewModel.menu.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Menu Details") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {

            when {
                isLoading -> CircularProgressIndicator()

                error != null -> Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error
                )

                menu == null -> Text("Menu not found")

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        item {
                            Text(
                                text = menu!!.name,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }

                        item {
                            Text(
                                text = "RM %.2f".format(menu!!.price),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        item {
                            SectionTitle("Description")
                            Text(menu!!.description)
                        }

                        item {
                            SectionTitle("Preparation Time")
                            Text(menu!!.preparationTime)
                        }

                        item {
                            SectionTitle("Serving Size")
                            Text(menu!!.servingSize)
                        }

                        item {
                            SectionTitle("Ingredients")
                            Column {
                                menu!!.ingredients.forEach {
                                    Text("• $it")
                                }
                            }
                        }

                        item {
                            SectionTitle("Dietary Info")
                            Text("Diet: ${menu!!.diet.name}")
                            Text("Spiciness: ${menu!!.spiciness.name}")
                        }

                        item {
                            SectionTitle("Category")
                            Text(menu!!.category.name)
                        }
                    }
                }
            }
        }
    }
}
