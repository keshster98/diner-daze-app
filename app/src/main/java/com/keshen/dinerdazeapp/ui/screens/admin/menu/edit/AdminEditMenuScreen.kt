package com.keshen.dinerdazeapp.ui.screens.admin.menu.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.keshen.dinerdazeapp.core.utils.MessageType
import com.keshen.dinerdazeapp.data.model.Diet
import com.keshen.dinerdazeapp.data.model.MenuCategory
import com.keshen.dinerdazeapp.data.model.Spiciness
import com.keshen.dinerdazeapp.ui.components.CardSection
import com.keshen.dinerdazeapp.ui.components.EnumDropdownField
import com.keshen.dinerdazeapp.ui.components.Field

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun AdminEditMenuScreen(
    navController: NavHostController,
    viewModel: AdminEditMenuViewModel = hiltViewModel()
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
    val isSaving by viewModel.isSaving.collectAsState()
    val message by viewModel.message.collectAsState()

    var e by remember(menu) { mutableStateOf(menu) }

    Column(modifier = Modifier.fillMaxSize()) {

        /* ---------------- TOP BAR ---------------- */

        TopAppBar(
            title = { Text("Edit Menu") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        /* ---------------- CONTENT ---------------- */

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {

            when {
                isLoading -> {
                    CircularProgressIndicator()
                }

                e == null -> {
                    Text("Menu not found")
                }

                else -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        CardSection {
                            Field(
                                label = "Name",
                                value = e!!.name,
                                onValueChange = { e = e!!.copy(name = it) }
                            )

                            Field(
                                label = "Description",
                                value = e!!.description,
                                onValueChange = { e = e!!.copy(description = it) }
                            )

                            Field(
                                label = "Price",
                                value = e!!.price.toString(),
                                onValueChange = {
                                    e = e!!.copy(price = it.toDoubleOrNull() ?: 0.0)
                                }
                            )

                            EnumDropdownField(
                                label = "Category",
                                options = MenuCategory.entries,
                                selected = e!!.category,
                                onSelected = { e = e!!.copy(category = it) }
                            )

                            EnumDropdownField(
                                label = "Diet",
                                options = Diet.entries.filter { it != Diet.ANY },
                                selected = e!!.diet,
                                onSelected = { e = e!!.copy(diet = it) }
                            )

                            EnumDropdownField(
                                label = "Spiciness",
                                options = Spiciness.entries.filter { it != Spiciness.ANY },
                                selected = e!!.spiciness,
                                onSelected = { e = e!!.copy(spiciness = it) }
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
            }
        }

        /* ---------------- BOTTOM ACTIONS ---------------- */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = { navController.popBackStack() }
            ) {
                Text("Cancel")
            }

            Button(
                modifier = Modifier.weight(1f),
                enabled = e != null && viewModel.isDirty(e!!) && !isSaving,
                onClick = {
                    viewModel.updateMenu(e!!) {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("menu_edited", true)

                        navController.popBackStack()
                    }
                }
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Edit Menu")
                }
            }
        }
    }
}

