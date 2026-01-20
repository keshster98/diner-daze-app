package com.keshen.dinerdazeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.keshen.dinerdazeapp.service.AuthService
import com.keshen.dinerdazeapp.service.UserProfileService
import com.keshen.dinerdazeapp.ui.navigation.AppNav
import com.keshen.dinerdazeapp.ui.theme.DinerDazeAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DinerDazeAppTheme {
                ComposeApp()
            }
        }
    }
}

@Composable
fun ComposeApp() {
    val authService = remember {
        AuthService(FirebaseAuth.getInstance())
    }

    val profileService = remember {
        UserProfileService(FirebaseFirestore.getInstance())
    }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            AppNav(
                authService,
                profileService
            )
        }
    }
}