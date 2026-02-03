package com.keshen.dinerdazeapp.ui.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.keshen.dinerdazeapp.service.AuthService
import com.keshen.dinerdazeapp.ui.components.CardSection
import com.keshen.dinerdazeapp.ui.components.DashboardTile
import com.keshen.dinerdazeapp.ui.components.SectionTitle
import com.keshen.dinerdazeapp.ui.navigation.Screen

@Composable
fun AdminScreen(
    navController: NavHostController,
    authService: AuthService
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            SectionTitle("Admin Dashboard")
        }

        item {
            CardSection {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DashboardTile(
                            title = "Users",
                            modifier = Modifier.weight(1f),
                            onClick = {navController.navigate(Screen.AdminTotalUsers)}
                        )

                        DashboardTile(
                            title = "Menu",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                navController.navigate(Screen.AdminMenu)
                            }
                        )
                    }

                    // Future expansion row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DashboardTile(
                            title = "Posts",
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(Screen.AdminPosts) }
                        )

                        DashboardTile(
                            title = "—",
                            modifier = Modifier.weight(1f),
                            enabled = false
                        )
                    }
                }
            }
        }
    }
}