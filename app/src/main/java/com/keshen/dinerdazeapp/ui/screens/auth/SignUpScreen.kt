package com.keshen.dinerdazeapp.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.keshen.dinerdazeapp.core.utils.MessageType

@Composable
fun SignUpScreen(
    onSuccess: () -> Unit,
    onSignInClick: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var showConfirmPassword by remember { mutableStateOf(false) }

    val isSigningUp by viewModel.isSigningUp.collectAsState()
    val message by viewModel.message.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Sign Up",
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSigningUp
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSigningUp,
                visualTransformation =
                    if (showPassword) {
                        VisualTransformation.None
                    }
                    else {
                        PasswordVisualTransformation()
                    },
                trailingIcon = {
                    IconButton(
                        onClick = { showPassword = !showPassword }
                    ) {
                        Icon(
                            imageVector = if (showPassword)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription =
                                if (showPassword) {
                                    "Hide password"
                                }
                                else {
                                    "Show password"
                                }
                        )
                    }
                }
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSigningUp,
                visualTransformation =
                    if (showConfirmPassword) {
                        VisualTransformation.None
                    }
                    else {
                        PasswordVisualTransformation()
                    },
                trailingIcon = {
                    IconButton(
                        onClick = { showConfirmPassword = !showConfirmPassword }
                    ) {
                        Icon(
                            imageVector = if (showConfirmPassword)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription =
                                if (showConfirmPassword) {
                                    "Hide confirm password"
                                }
                                else {
                                    "Show confirm password"
                                }
                        )
                    }
                }
            )

            Spacer(Modifier.height(20.dp))

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
                            1.dp,
                            if (isError) Color(0xFFD32F2F) else Color(0xFF388E3C),
                            MaterialTheme.shapes.medium
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

                Spacer(Modifier.height(16.dp))
            }

            Button(
                enabled = !isSigningUp,
                onClick = {
                    viewModel.signUp(
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword,
                        onSuccess = onSuccess
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSigningUp) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Sign Up")
                }
            }

            Spacer(Modifier.height(16.dp))

            TextButton(
                enabled = !isSigningUp,
                onClick = onSignInClick
            ) {
                Text("Already have an account? Sign In")
            }
        }
    }
}