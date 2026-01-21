package com.keshen.dinerdazeapp.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.keshen.dinerdazeapp.service.AuthService

@Composable
fun HomeScreen(
    authService: AuthService,
    viewModel: HomeViewModel = hiltViewModel(),
    onSignOutClick: () -> Unit
) {
    // val displayName by viewModel.displayName.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Welcome to the home screen!",
                style = MaterialTheme.typography.headlineSmall
            )

            if (authService.isLoggedIn()) {
                Button(
                    onClick = {
                        viewModel.signOut()
                        onSignOutClick()
                    }
                ) {
                    Text("Sign Out")
                }
            }
        }
    }
}