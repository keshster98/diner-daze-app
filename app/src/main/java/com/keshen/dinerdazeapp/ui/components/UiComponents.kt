package com.keshen.dinerdazeapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.keshen.dinerdazeapp.data.model.Diet
import com.keshen.dinerdazeapp.data.model.Gender
import com.keshen.dinerdazeapp.data.model.Menu
import com.keshen.dinerdazeapp.data.model.MenuCategory
import com.keshen.dinerdazeapp.data.model.Post
import com.keshen.dinerdazeapp.data.model.PostSort
import com.keshen.dinerdazeapp.data.model.PostTag
import com.keshen.dinerdazeapp.data.model.Spiciness
import com.keshen.dinerdazeapp.data.model.User
import com.keshen.dinerdazeapp.ui.screens.admin.deletion.RequestSort
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        modifier = Modifier.fillMaxWidth()
    ) {

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

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Diet",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Spiciness",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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


@OptIn(ExperimentalMaterial3Api::class)
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
        Box(
            modifier = Modifier.padding(12.dp)
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = menu.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "RM ${"%.2f".format(menu.price)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete menu",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun MenuGridCard(
    menu: Menu,
    quantity: Int,
    isLoggedIn: Boolean,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onAddToCart: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {

            Text(
                text = menu.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "RM %.2f".format(menu.price),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isLoggedIn) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onDecrease,
                            enabled = quantity > 0
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease")
                        }

                        Text(
                            text = quantity.toString(),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(
                            onClick = onIncrease,
                            enabled = quantity < 5
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase")
                        }
                    }

                    Spacer(Modifier.weight(1f))

                    IconButton(
                        onClick = onAddToCart,
                        enabled = quantity > 0
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Add to cart",
                            tint =
                                if (quantity > 0)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PostTagFilterChips(
    selectedTag: PostTag?,
    onTagSelected: (PostTag?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(PostTag.entries.toTypedArray()) { tag ->
            FilterChip(
                selected = selectedTag == tag,
                onClick = {
                    onTagSelected(
                        if (selectedTag == tag) null else tag
                    )
                },
                label = {
                    Text(
                        text = tag.name.lowercase()
                            .replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
        }
    }
}

@Composable
fun PostSortFilterChips(
    selectedSort: PostSort,
    onSortSelected: (PostSort) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PostSort.entries.forEach { sort ->
            FilterChip(
                selected = selectedSort == sort,
                onClick = { onSortSelected(sort) },
                label = {
                    Text(
                        text = when (sort) {
                            PostSort.LATEST_POST -> "Latest Post"
                            PostSort.LATEST_UPDATE -> "Latest Update"
                            PostSort.EARLIEST_POST -> "Earliest Post"
                        },
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
        }
    }
}

@Composable
fun AdminPostRowCard(
    post: Post,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = post.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = post.description,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Post",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun UserPostRowCard(
    post: Post,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = post.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AdminDeletionRequestRow(
    user: User,
    onDelete: () -> Unit
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = "${user.firstName} ${user.lastName}",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "${user.email} | ${user.phone}",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Deletion Requested On: ${formatDate(user.deleteRequestedAt)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

fun formatDate(time: Long?): String {
    if (time == null) return "—"

    val sdf = SimpleDateFormat("hh:mm a, dd/MM/yy", Locale.getDefault())
    return sdf.format(Date(time))
}

@Composable
fun GenderFilterChips(
    selected: Gender?,
    onSelected: (Gender?) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Gender.entries.filter { it != Gender.PREFER_NOT_TO_SAY }.forEach {
            FilterChip(
                selected = selected == it,
                onClick = { onSelected(if (selected == it) null else it) },
                label = { Text(it.name.lowercase().replaceFirstChar(Char::uppercase)) }
            )
        }
    }
}

@Composable
fun RequestSortChips(
    selected: RequestSort,
    onSelected: (RequestSort) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        RequestSort.entries.forEach {
            FilterChip(
                selected = selected == it,
                onClick = { onSelected(it) },
                label = {
                    Text(
                        if (it == RequestSort.EARLIEST)
                            "Earliest Request"
                        else
                            "Latest Request"
                    )
                }
            )
        }
    }
}