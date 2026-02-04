package com.keshen.dinerdazeapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.keshen.dinerdazeapp.core.utils.MessageType
import com.keshen.dinerdazeapp.data.model.Diet
import com.keshen.dinerdazeapp.ui.components.EditField
import com.keshen.dinerdazeapp.ui.components.EnumDropdownField
import com.keshen.dinerdazeapp.data.model.Gender
import com.keshen.dinerdazeapp.data.model.Spiciness
import com.keshen.dinerdazeapp.ui.components.EditableSection
import com.keshen.dinerdazeapp.ui.components.SectionTitle

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val message by viewModel.message.collectAsState()
    var e by remember(user) { mutableStateOf(user) }

    when {
        isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }

        e == null -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text("Profile not found")
        }

        else -> LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { SectionTitle("Personal Information") }
            item {
                EditableSection {
                    EditField("First Name", e!!.firstName) { e = e!!.copy(firstName = it) }
                    EditField("Last Name", e!!.lastName) { e = e!!.copy(lastName = it) }
                    EnumDropdownField(
                        label = "Gender",
                        options = Gender.entries,
                        selected = e!!.gender,
                        onSelected = { e = e!!.copy(gender = it) }
                    )

                    Spacer(modifier = Modifier.height(1.dp))

                    Text(
                        "To change your email, please contact the admin",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = e!!.email,
                        onValueChange = {},
                        enabled = false,
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    EditField("Phone", e!!.phone) { e = e!!.copy(phone = it) }
                }
            }

            item { SectionTitle("Diet Information") }
            item {
                EditableSection {
                    EnumDropdownField(
                        label = "Dietary Preference",
                        options = Diet.entries,
                        selected = e!!.diet,
                        onSelected = { e = e!!.copy(diet = it) }
                    )
                    EnumDropdownField(
                        label = "Spiciness Preference",
                        options = Spiciness.entries,
                        selected = e!!.spiciness,
                        onSelected = { e = e!!.copy(spiciness = it) }
                    )
                }
            }

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
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = viewModel.isInfoDirty(e!!) && !isSaving,
                    onClick = { viewModel.updateProfile(e!!) }
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Save")
                    }
                }
            }
        }
    }
}