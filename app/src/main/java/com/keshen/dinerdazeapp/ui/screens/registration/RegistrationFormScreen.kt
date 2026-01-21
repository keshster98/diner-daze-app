package com.keshen.dinerdazeapp.ui.screens.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.keshen.dinerdazeapp.core.utils.MessageType
import com.keshen.dinerdazeapp.data.model.Diet
import com.keshen.dinerdazeapp.data.model.Gender
import com.keshen.dinerdazeapp.data.model.Spiciness
import com.keshen.dinerdazeapp.data.model.User
import com.keshen.dinerdazeapp.service.AuthService
import com.keshen.dinerdazeapp.ui.components.CardSection
import com.keshen.dinerdazeapp.ui.components.EnumDropdownField
import com.keshen.dinerdazeapp.ui.components.Field
import com.keshen.dinerdazeapp.ui.components.InfoNote
import com.keshen.dinerdazeapp.ui.components.SectionTitle

@Composable
fun RegistrationFormScreen(
    authService: AuthService,
    onCompleted: () -> Unit,
    onLoggedOut: () -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val uid = authService.uid()
    val email = authService.email()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf<Gender?>(null) }
    var phone by remember { mutableStateOf("") }

    var diet by remember { mutableStateOf<Diet?>(null) }
    var spiciness by remember { mutableStateOf<Spiciness?>(null) }

    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val message by viewModel.message.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { SectionTitle("Personal Information") }
        item {
            CardSection {
                Field(
                    label = "First Name",
                    value = firstName,
                    onValueChange = { firstName = it }
                )

                Field(
                    label = "Last Name",
                    value = lastName,
                    onValueChange = { lastName = it }
                )

                InfoNote(
                    text = "If you don't pick your gender, the default \"Prefer Not To Say\" will be applied. You can change this later in your profile."
                )

                EnumDropdownField(
                    label = "Gender",
                    options = Gender.entries,
                    selected = gender,
                    onSelected = { gender = it }
                )

                Field(
                    label = "Email",
                    value = email,
                    onValueChange = {},
                    enabled = false,
                )

                Field(
                    label = "Phone",
                    value = phone,
                    onValueChange = { phone = it }
                )
            }
        }

        item { SectionTitle("Diet Information") }
        item {
            CardSection {

                InfoNote(
                    text = "If you don't pick for either of the two below, the default \"Any\" will be applied. You can change this later in your profile."
                )

                EnumDropdownField(
                    label = "Dietary Preference",
                    options = Diet.entries,
                    selected = diet,
                    onSelected = { diet = it }
                )

                EnumDropdownField(
                    label = "Spiciness Preference",
                    options = Spiciness.entries,
                    selected = spiciness,
                    onSelected = { spiciness = it }
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.logout()
                        onLoggedOut()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFB56576)
                    )
                ) {
                    Text("Log Out")
                }

                Button(
                    modifier = Modifier.weight(1f),
                    enabled = !isSubmitting,
                    onClick = {

                        val user = User(
                            uid = uid,
                            firstName = firstName,
                            lastName = lastName,
                            gender = gender ?: Gender.PREFER_NOT_TO_SAY,
                            email = email,
                            phone = phone,
                            diet = diet ?: Diet.ANY,
                            spiciness = spiciness ?: Spiciness.ANY,
                            profileFilled = true
                        )

                        viewModel.submitRegistration(user, onCompleted)
                    }
                )
                {
                    Text("Submit")
                }
            }
        }
    }
}