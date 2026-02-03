package com.keshen.dinerdazeapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.keshen.dinerdazeapp.data.model.Diet
import com.keshen.dinerdazeapp.data.model.Gender
import com.keshen.dinerdazeapp.data.model.Menu
import com.keshen.dinerdazeapp.data.model.MenuCategory
import com.keshen.dinerdazeapp.data.model.Spiciness
import com.keshen.dinerdazeapp.data.model.User

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CardSection(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}

@Composable
fun Field(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.weight(1f),
            singleLine = true,
            enabled = enabled
        )
    }
}

@Composable
fun InfoNote(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Enum<T>> EnumDropdownField(
    label: String,
    options: Iterable<T>,
    selected: T?,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected?.name
                ?.lowercase()
                ?.split("_")
                ?.joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option.name
                                .lowercase()
                                .split("_")
                                .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                        )
                    },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun EditableSection(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            content()
        }
    }
}

@Composable
fun EditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun DashboardTile(
    title: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(enabled = enabled) { onClick() },
        colors = CardDefaults.cardColors(
            containerColor =
                if (enabled)
                    MaterialTheme.colorScheme.surface
                else
                    MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color =
                    if (enabled)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun FilterChipsRow(
    selectedGender: Gender?,
    selectedDiet: Diet?,
    selectedSpiciness: Spiciness?,
    onGenderSelected: (Gender?) -> Unit,
    onDietSelected: (Diet?) -> Unit,
    onSpicinessSelected: (Spiciness?) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        // -------- Gender --------
        Text(
            text = "Gender",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Gender.entries.forEach { gender ->
                FilterChip(
                    selected = selectedGender == gender,
                    onClick = {
                        onGenderSelected(
                            if (selectedGender == gender) null else gender
                        )
                    },
                    label = {
                        Text(
                            gender.name
                                .lowercase()
                                .split("_")
                                .joinToString(" ") {
                                    it.replaceFirstChar { c -> c.uppercase() }
                                }
                        )
                    }
                )
            }
        }

        // -------- Diet --------
        Text(
            text = "Diet",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Diet.entries.forEach { diet ->
                FilterChip(
                    selected = selectedDiet == diet,
                    onClick = {
                        onDietSelected(
                            if (selectedDiet == diet) null else diet
                        )
                    },
                    label = {
                        Text(
                            diet.name
                                .lowercase()
                                .split("_")
                                .joinToString(" ") {
                                    it.replaceFirstChar { c -> c.uppercase() }
                                }
                        )
                    }
                )
            }
        }

        // -------- Spiciness --------
        Text(
            text = "Spiciness",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Spiciness.entries.forEach { spiciness ->
                FilterChip(
                    selected = selectedSpiciness == spiciness,
                    onClick = {
                        onSpicinessSelected(
                            if (selectedSpiciness == spiciness) null else spiciness
                        )
                    },
                    label = {
                        Text(
                            spiciness.name
                                .lowercase()
                                .split("_")
                                .joinToString(" ") {
                                    it.replaceFirstChar { c -> c.uppercase() }
                                }
                        )
                    }
                )
            }
        }
    }
}


@Composable
fun UserRowCard(
    user: User,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "${user.firstName} ${user.lastName}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = user.email,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = user.phone,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MenuFilterChips(
    selectedCategory: MenuCategory?,
    selectedDiet: Diet?,
    selectedSpiciness: Spiciness?,
    onCategorySelected: (MenuCategory?) -> Unit,
    onDietSelected: (Diet?) -> Unit,
    onSpicinessSelected: (Spiciness?) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        /* -------- CATEGORY -------- */

        Text(
            text = "Category",
            style = MaterialTheme.typography.labelMedium
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MenuCategory.entries.forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = {
                        onCategorySelected(
                            if (selectedCategory == category) null else category
                        )
                    },
                    label = {
                        Text(
                            category.name
                                .lowercase()
                                .replaceFirstChar { it.uppercase() }
                        )
                    }
                )
            }
        }

        /* -------- DIET -------- */

        Text(
            text = "Diet",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Diet.entries
                .filter { it != Diet.ANY }
                .forEach { diet ->
                    FilterChip(
                        selected = selectedDiet == diet,
                        onClick = {
                            onDietSelected(
                                if (selectedDiet == diet) null else diet
                            )
                        },
                        label = {
                            Text(
                                diet.name
                                    .lowercase()
                                    .split("_")
                                    .joinToString(" ") {
                                        it.replaceFirstChar { c -> c.uppercase() }
                                    }
                            )
                        }
                    )
                }
        }

        /* -------- SPICINESS -------- */

        Text(
            text = "Spiciness",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Spiciness.entries
                .filter { it != Spiciness.ANY }
                .forEach { spiciness ->
                    FilterChip(
                        selected = selectedSpiciness == spiciness,
                        onClick = {
                            onSpicinessSelected(
                                if (selectedSpiciness == spiciness) null else spiciness
                            )
                        },
                        label = {
                            Text(
                                spiciness.name
                                    .lowercase()
                                    .split("_")
                                    .joinToString(" ") {
                                        it.replaceFirstChar { c -> c.uppercase() }
                                    }
                            )
                        }
                    )
                }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MenuRowCard(
    menu: Menu,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            /* -------- TITLE -------- */
            Text(
                text = menu.name,
                style = MaterialTheme.typography.titleMedium
            )

            /* -------- TAGS + DELETE -------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {

                    FilterChip(
                        selected = false,
                        enabled = false,
                        onClick = {},
                        label = {
                            Text(
                                menu.category.name
                                    .lowercase()
                                    .replaceFirstChar { it.uppercase() }
                            )
                        }
                    )

                    FilterChip(
                        selected = false,
                        enabled = false,
                        onClick = {},
                        label = {
                            Text(
                                menu.diet.name
                                    .lowercase()
                                    .split("_")
                                    .joinToString(" ") {
                                        it.replaceFirstChar { c -> c.uppercase() }
                                    }
                            )
                        }
                    )

                    FilterChip(
                        selected = false,
                        enabled = false,
                        onClick = {},
                        label = {
                            Text(
                                menu.spiciness.name
                                    .lowercase()
                                    .split("_")
                                    .joinToString(" ") {
                                        it.replaceFirstChar { c -> c.uppercase() }
                                    }
                            )
                        }
                    )
                }

                IconButton(
                    onClick = onDelete
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete menu",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            /* -------- PRICE -------- */
            Text(
                text = "RM ${"%.2f".format(menu.price)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun MenuGridCard(
    menu: Menu,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // keeps grid tiles uniform
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            /* ---------- TITLE + DESCRIPTION ---------- */

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = menu.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )

                Text(
                    text = menu.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3
                )
            }

            /* ---------- PRICE ---------- */

            Text(
                text = "RM ${"%.2f".format(menu.price)}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}