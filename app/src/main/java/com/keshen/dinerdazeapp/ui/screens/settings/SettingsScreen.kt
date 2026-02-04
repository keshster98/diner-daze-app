package com.keshen.dinerdazeapp.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.keshen.dinerdazeapp.core.utils.MessageType
import com.keshen.dinerdazeapp.ui.components.CardSection
import com.keshen.dinerdazeapp.ui.components.SectionTitle

@Composable
fun SettingsScreen(
    onLogout: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val message by viewModel.message.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { if (!isSubmitting) showDeleteDialog = false },
            title = { Text("Request Account Deletion") },
            text = {
                Text(
                    "Are you sure you want to request account deletion?\n\n" +
                            "This is a non-reversible process. You will be logged out immediately and " +
                            "will not be able to sign in or sign up until the admin deletes the account."
                )
            },
            confirmButton = {
                TextButton(
                    enabled = !isSubmitting,
                    onClick = {
                        viewModel.requestDeletion(
                            onDone = {
                                showDeleteDialog = false
                                onLogout()
                            }
                        )
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !isSubmitting,
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item { SectionTitle("Account") }

        item {
            CardSection {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Request Account Deletion",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    IconButton(
                        enabled = !isSubmitting,
                        onClick = { showDeleteDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Request deletion",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Sign Out",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    IconButton(
                        enabled = !isSubmitting,
                        onClick = {
                            viewModel.logout()
                            onLogout()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Sign out",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        item {
            message?.let { uiMessage ->
                val isError = uiMessage.type == MessageType.ERROR

                androidx.compose.foundation.layout.Box(
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
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}