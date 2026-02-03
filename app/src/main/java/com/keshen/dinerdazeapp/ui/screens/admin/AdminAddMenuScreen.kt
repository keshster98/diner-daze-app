package com.keshen.dinerdazeapp.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val message by viewModel.message.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item { SectionTitle("Menu Information") }

        item {
            CardSection {

                Field(
                    label = "Menu Name",
                    value = name,
                    onValueChange = { name = it }
                )

                Field(
                    label = "Description",
                    value = description,
                    onValueChange = { description = it }
                )

                Field(
                    label = "Price (RM)",
                    value = price,
                    onValueChange = { price = it }
                )

                EnumDropdownField(
                    label = "Category",
                    options = MenuCategory.entries,
                    selected = category,
                    onSelected = { category = it }
                )

                EnumDropdownField(
                    label = "Diet",
                    options = Diet.entries.filter { it != Diet.ANY },
                    selected = diet,
                    onSelected = { diet = it }
                )

                EnumDropdownField(
                    label = "Spiciness",
                    options = Spiciness.entries.filter { it != Spiciness.ANY },
                    selected = spiciness,
                    onSelected = { spiciness = it }
                )
            }
        }

        /* ---------- MESSAGE ---------- */

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

        /* ---------- ACTIONS ---------- */

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
}