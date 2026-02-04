package com.keshen.dinerdazeapp.ui.screens.admin.menu.add

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun AdminAddMenuScreen(
    navController: NavHostController,
    viewModel: AdminAddMenuViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    var category by remember { mutableStateOf<MenuCategory?>(null) }
    var diet by remember { mutableStateOf<Diet?>(null) }
    var spiciness by remember { mutableStateOf<Spiciness?>(null) }

    var preparationTime by remember { mutableStateOf("") }
    var servingSize by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }

    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val message by viewModel.message.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Add Menu") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                CardSection {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 420.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        item {
                            Field(
                                label = "Menu Name",
                                value = name,
                                onValueChange = { name = it }
                            )
                        }

                        item {
                            Field(
                                label = "Description",
                                value = description,
                                onValueChange = { description = it }
                            )
                        }

                        item {
                            Field(
                                label = "Price (RM)",
                                value = price,
                                onValueChange = { price = it }
                            )
                        }

                        item {
                            EnumDropdownField(
                                label = "Category",
                                options = MenuCategory.entries,
                                selected = category,
                                onSelected = { category = it }
                            )
                        }

                        item {
                            EnumDropdownField(
                                label = "Diet",
                                options = Diet.entries.filter { it != Diet.ANY },
                                selected = diet,
                                onSelected = { diet = it }
                            )
                        }

                        item {
                            EnumDropdownField(
                                label = "Spiciness",
                                options = Spiciness.entries.filter { it != Spiciness.ANY },
                                selected = spiciness,
                                onSelected = { spiciness = it }
                            )
                        }

                        item {
                            Field(
                                label = "Preparation Time",
                                value = preparationTime,
                                onValueChange = { preparationTime = it }
                            )
                        }

                        item {
                            Field(
                                label = "Serving Size",
                                value = servingSize,
                                onValueChange = { servingSize = it }
                            )
                        }

                        item {
                            Field(
                                label = "Ingredients",
                                value = ingredients,
                                onValueChange = { ingredients = it }
                            )
                        }

                        // small bottom padding so last field isn’t cut off
                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }

            }

            item {
                message?.let { uiMessage ->
                    val isError = uiMessage.type == MessageType.ERROR

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isError) Color(0xFFFFEBEE) else Color(0xFFE8F5E9),
                                MaterialTheme.shapes.medium
                            )
                            .border(
                                1.dp,
                                if (isError) Color(0xFFD32F2F) else Color(0xFF388E3C),
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
                    viewModel.addMenu(
                        name = name,
                        description = description,
                        price = price,
                        category = category ?: MenuCategory.MAIN,
                        diet = diet ?: Diet.ANY,
                        spiciness = spiciness ?: Spiciness.ANY,
                        preparationTime = preparationTime,
                        servingSize = servingSize,
                        ingredientsRaw = ingredients,
                        onSuccess = {
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("menu_added", true)

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
                    Text("Add Menu")
                }
            }
        }
    }
}

