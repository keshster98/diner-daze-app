package com.keshen.dinerdazeapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.keshen.dinerdazeapp.data.model.User
import com.keshen.dinerdazeapp.service.AuthService
import com.keshen.dinerdazeapp.service.UserProfileService
import com.keshen.dinerdazeapp.ui.screens.admin.AdminAddMenuScreen
import com.keshen.dinerdazeapp.ui.screens.admin.AdminEditMenuScreen
import com.keshen.dinerdazeapp.ui.screens.admin.AdminMenuScreen
import com.keshen.dinerdazeapp.ui.screens.admin.AdminScreen
import com.keshen.dinerdazeapp.ui.screens.admin.AdminTotalUsersScreen
import com.keshen.dinerdazeapp.ui.screens.admin.AdminUserEditScreen
import com.keshen.dinerdazeapp.ui.screens.auth.SignInScreen
import com.keshen.dinerdazeapp.ui.screens.auth.SignUpScreen
import com.keshen.dinerdazeapp.ui.screens.home.HomeScreen
import com.keshen.dinerdazeapp.ui.screens.menu.MenuScreen
import com.keshen.dinerdazeapp.ui.screens.profile.ProfileScreen
import com.keshen.dinerdazeapp.ui.screens.registration.RegistrationFormScreen
import com.keshen.dinerdazeapp.ui.screens.settings.SettingsScreen

@Composable
fun AppNav(
    authService: AuthService,
    profileService: UserProfileService
) {
    val navController = rememberNavController()

    val uid by authService.uidFlow.collectAsState()
    var isProfileFilled by remember { mutableStateOf<Boolean?>(null) }
    var isAdmin by remember { mutableStateOf(false) }

    LaunchedEffect(uid) {
        if (uid == null) {
            isProfileFilled = null
            isAdmin = false
            return@LaunchedEffect
        }

        isProfileFilled = profileService.isProfileFilled(uid!!)
        isAdmin = profileService.isAdmin(uid!!)
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val backStack by navController.currentBackStackEntryAsState()
                val currentRoute = backStack?.destination?.route

                // Home Tab
                NavigationBarItem(
                    selected = currentRoute == Screen.Home::class.qualifiedName,
                    onClick = {
                        navController.navigate(Screen.Home) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Filled.Home, null) },
                    label = { Text("Home") }
                )

                // Menu Tab
                NavigationBarItem(
                    selected = currentRoute == Screen.Menu::class.qualifiedName,
                    onClick = {
                        navController.navigate(Screen.Menu) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Filled.RestaurantMenu, null) },
                    label = { Text("Menu") }
                )

                // Profile and Settings Tab
                if (uid != null && isProfileFilled == true) {
                    NavigationBarItem(
                        selected = currentRoute == Screen.Profile::class.qualifiedName,
                        onClick = {
                            navController.navigate(Screen.Profile) {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Filled.Person, null) },
                        label = { Text("Profile") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == Screen.Settings::class.qualifiedName,
                        onClick = {
                            navController.navigate(Screen.Settings) {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Filled.Settings, null) },
                        label = { Text("Settings") }
                    )

                    if (isAdmin) {
                        NavigationBarItem(
                            selected = currentRoute == Screen.Admin::class.qualifiedName,
                            onClick = {
                                navController.navigate(Screen.Admin) {
                                    launchSingleTop = true
                                }
                            },
                            icon = { Icon(Icons.Filled.AdminPanelSettings, null) },
                            label = { Text("Admin") }
                        )
                    }
                }

                // Access Tab (Sign In / Sign Up / Registration Form)
                if (isProfileFilled != true) {
                    NavigationBarItem(
                        selected =
                            currentRoute == Screen.SignIn::class.qualifiedName ||
                                    currentRoute == Screen.SignUp::class.qualifiedName ||
                                    currentRoute == Screen.RegistrationForm::class.qualifiedName,
                        onClick = {
                            val destination = when {
                                uid == null ->
                                    Screen.SignIn

                                isProfileFilled != true ->
                                    Screen.RegistrationForm

                                else ->
                                    Screen.SignIn // defensive fallback
                            }

                            navController.navigate(destination) {
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(Icons.Filled.Login, null) },
                        label = { Text("Access App") }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home,
            modifier = Modifier.padding(padding)
        ) {

            composable<Screen.Home> {
                HomeScreen(
                    authService = authService,
                    onSignOutClick = {
                        navController.navigate(Screen.SignIn) {
                            popUpTo(Screen.Home) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<Screen.Menu> {
                MenuScreen()
            }

            composable<Screen.SignIn> {
                SignInScreen(
                    onSuccess = { completed ->
                        if (completed) {
                            navController.navigate(Screen.Home) {
                                popUpTo(Screen.SignIn) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Screen.RegistrationForm)
                        }
                    },
                    onSignUpClick = {
                        navController.navigate(Screen.SignUp)
                    }
                )
            }

            composable<Screen.SignUp> {
                SignUpScreen(
                    onSuccess = {
                        navController.navigate(Screen.RegistrationForm)
                    },
                    onSignInClick = {
                        navController.navigate(Screen.SignIn)
                    }
                )
            }

            composable<Screen.RegistrationForm> {
                RegistrationFormScreen(
                    authService = authService,
                    onCompleted = {
                        isProfileFilled = true

                        navController.navigate(Screen.Home) {
                            popUpTo(Screen.RegistrationForm) { inclusive = true }
                        }
                    },
                    onLoggedOut = {
                        navController.navigate(Screen.SignIn) {
                            popUpTo(Screen.RegistrationForm) { inclusive = true }
                        }
                    }
                )
            }

            composable<Screen.Profile> {
                ProfileScreen()
            }

            composable<Screen.Admin> {
                if (!isAdmin) {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Admin) { inclusive = true }
                    }
                    return@composable
                }

                AdminScreen(
                    navController = navController,
                    authService = authService
                )
            }

            composable<Screen.Settings> {
                SettingsScreen(
                    onLogout = {
                        navController.navigate(Screen.Home) {
                            popUpTo(Screen.Settings) { inclusive = true }
                        }
                    }
                )
            }

            composable<Screen.AdminTotalUsers> {
                if (!isAdmin) {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.AdminMenu) { inclusive = true }
                    }
                    return@composable
                }

                AdminTotalUsersScreen(
                    navController = navController
                )
            }

            composable<Screen.AdminUserEdit> {
                if (!isAdmin) {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.AdminMenu) { inclusive = true }
                    }
                    return@composable
                }

                AdminUserEditScreen(
                    navController = navController
                )
            }

            composable<Screen.AdminMenu> {
                if (!isAdmin) {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.AdminMenu) { inclusive = true }
                    }
                    return@composable
                }

                AdminMenuScreen(
                    navController = navController
                )
            }

            composable<Screen.AdminAddMenu> {
                if (!isAdmin) {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.AdminAddMenu) { inclusive = true }
                    }
                    return@composable
                }

                AdminAddMenuScreen(
                    navController = navController
                )
            }

            composable<Screen.AdminEditMenu> {
                if (!isAdmin) {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.AdminEditMenu) { inclusive = true }
                    }
                    return@composable
                }

                AdminEditMenuScreen(
                    navController = navController
                )
            }
        }
    }
}