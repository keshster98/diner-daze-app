package com.keshen.dinerdazeapp.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.keshen.dinerdazeapp.core.store.PendingDeletionStore
import com.keshen.dinerdazeapp.core.store.PendingDeletionViewModel
import com.keshen.dinerdazeapp.service.AuthService
import com.keshen.dinerdazeapp.service.UserProfileService
import com.keshen.dinerdazeapp.ui.screens.admin.menu.add.AdminAddMenuScreen
import com.keshen.dinerdazeapp.ui.screens.admin.menu.edit.AdminEditMenuScreen
import com.keshen.dinerdazeapp.ui.screens.admin.menu.AdminMenuScreen
import com.keshen.dinerdazeapp.ui.screens.admin.AdminScreen
import com.keshen.dinerdazeapp.ui.screens.admin.deletion.AdminDeletionRequestsScreen
import com.keshen.dinerdazeapp.ui.screens.admin.posts.AdminPostScreen
import com.keshen.dinerdazeapp.ui.screens.admin.posts.add.AdminAddPostScreen
import com.keshen.dinerdazeapp.ui.screens.admin.posts.edit.AdminEditPostScreen
import com.keshen.dinerdazeapp.ui.screens.admin.user.AdminTotalUsersScreen
import com.keshen.dinerdazeapp.ui.screens.admin.user.edit.AdminUserEditScreen
import com.keshen.dinerdazeapp.ui.screens.auth.PendingDeletionScreen
import com.keshen.dinerdazeapp.ui.screens.auth.SignInScreen
import com.keshen.dinerdazeapp.ui.screens.auth.SignUpScreen
import com.keshen.dinerdazeapp.ui.screens.cart.CartScreen
import com.keshen.dinerdazeapp.ui.screens.cart.CartViewModel
import com.keshen.dinerdazeapp.ui.screens.menu.MenuDetailsScreen
import com.keshen.dinerdazeapp.ui.screens.news.PostScreen
import com.keshen.dinerdazeapp.ui.screens.menu.MenuScreen
import com.keshen.dinerdazeapp.ui.screens.news.PostDetailsScreen
import com.keshen.dinerdazeapp.ui.screens.profile.ProfileScreen
import com.keshen.dinerdazeapp.ui.screens.registration.RegistrationFormScreen
import com.keshen.dinerdazeapp.ui.screens.settings.SettingsScreen

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AppNav(
    authService: AuthService,
    profileService: UserProfileService,
    pendingDeletionStore: PendingDeletionStore
) {
    val navController = rememberNavController()
    val cartViewModel: CartViewModel = hiltViewModel()

    val uid by authService.uidFlow.collectAsState()
    var isProfileFilled by remember { mutableStateOf<Boolean?>(null) }
    var isAdmin by remember { mutableStateOf(false) }

    val pendingVm: PendingDeletionViewModel = hiltViewModel()
    val isPendingDeletion by pendingVm.store.isPending.collectAsState(initial = false)
    val pendingEmail by pendingVm.store.email.collectAsState(initial = null)
    val pendingRequestedAt by pendingVm.store.requestedAt.collectAsState(initial = null)

    LaunchedEffect(uid, isPendingDeletion) {

        /* Normal logout  */
        if (uid == null && !isPendingDeletion) {
            isProfileFilled = null
            isAdmin = false
            return@LaunchedEffect
        }

        /* Logged out + pending deletion */
        if (uid == null && isPendingDeletion) {

            val email = pendingEmail

            if (email != null) {
                val stillExists = runCatching {
                    profileService.doesUserExistByEmail(email)
                }.getOrDefault(false)

                if (!stillExists) {
                    // Admin deleted user document then clear local store
                    pendingDeletionStore.clear()
                }
            }

            isProfileFilled = null
            isAdmin = false
            return@LaunchedEffect
        }

        /* Logged-in user */
        val currentUid = uid ?: return@LaunchedEffect

        runCatching {
            Triple(
                profileService.isDeletionRequested(currentUid),
                profileService.isProfileFilled(currentUid),
                profileService.isAdmin(currentUid)
            )
        }.onSuccess { (deletionRequested, profileFilled, admin) ->
            isProfileFilled = if (deletionRequested) false else profileFilled
            isAdmin = admin
        }.onFailure {
            isProfileFilled = null
            isAdmin = false
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val backStack by navController.currentBackStackEntryAsState()
                val currentRoute = backStack?.destination?.route

                // Home Tab
                NavigationBarItem(
                    selected = currentRoute == Screen.Post::class.qualifiedName,
                    onClick = {
                        navController.navigate(Screen.Post) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Filled.Newspaper, null) },
                    label = { Text("News") }
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
                        selected = currentRoute == Screen.Cart::class.qualifiedName,
                        onClick = { navController.navigate(Screen.Cart) },
                        icon = { Icon(Icons.Default.ShoppingCart, null) },
                        label = { Text("Cart") }
                    )

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
                                isPendingDeletion -> Screen.PendingDeletion
                                uid == null -> Screen.SignIn
                                isProfileFilled != true -> Screen.RegistrationForm
                                else -> Screen.SignIn
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
            startDestination = Screen.Post,
            modifier = Modifier.padding(padding)
        ) {

            composable<Screen.Post> {
                PostScreen(navController)
            }

            composable<Screen.PostDetails> {
                PostDetailsScreen(navController)
            }

            composable<Screen.SignIn> {
                SignInScreen(
                    onSuccess = { completed ->
                        if (completed) {
                            navController.navigate(Screen.Post) {
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

                        navController.navigate(Screen.Post) {
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
                    navController.navigate(Screen.Post) {
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
                        navController.navigate(Screen.Post) {
                            popUpTo(Screen.Settings) { inclusive = true }
                        }
                    }
                )
            }

            composable<Screen.AdminTotalUsers> {
                if (!isAdmin) {
                    navController.navigate(Screen.Post) {
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
                    navController.navigate(Screen.Post) {
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
                    navController.navigate(Screen.Post) {
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
                    navController.navigate(Screen.Post) {
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
                    navController.navigate(Screen.Post) {
                        popUpTo(Screen.AdminEditMenu) { inclusive = true }
                    }
                    return@composable
                }

                AdminEditMenuScreen(
                    navController = navController
                )
            }

            composable<Screen.AdminPosts> {
                if (!isAdmin) {
                    navController.navigate(Screen.Post) {
                        popUpTo(Screen.AdminPosts) { inclusive = true }
                    }
                    return@composable
                }

                AdminPostScreen(
                    navController = navController
                )
            }

            composable<Screen.AdminAddPost> {
                if (!isAdmin) {
                    navController.navigate(Screen.Post) {
                        popUpTo(Screen.AdminAddPost) { inclusive = true }
                    }
                    return@composable
                }

                AdminAddPostScreen(
                    navController = navController
                )
            }

            composable<Screen.AdminEditPost> {
                if (!isAdmin) {
                    navController.navigate(Screen.Post) {
                        popUpTo(Screen.AdminAddPost) { inclusive = true }
                    }
                    return@composable
                }

                AdminEditPostScreen(
                    navController = navController
                )
            }

            composable<Screen.PendingDeletion> {
                val now = System.currentTimeMillis()
                val requestedAt = pendingRequestedAt
                val moreThanWeek =
                    requestedAt != null && (now - requestedAt) >= 7L * 24 * 60 * 60 * 1000

                val msg = buildString {
                    append("Your account")
                    if (!pendingEmail.isNullOrBlank()) append(" (").append(pendingEmail).append(")")
                    append(" is pending deletion.\n\n")
                    append("You can sign in or sign up again only after the admin deletes the account.")
                    if (moreThanWeek) {
                        append("\n\nIf this stays like this for more than one week, please contact the admin via email.")
                    }
                }

                PendingDeletionScreen(message = msg)
            }

            composable<Screen.AdminDeletionRequests> {
                if (!isAdmin) {
                    navController.navigate(Screen.Post) {
                        popUpTo(Screen.AdminDeletionRequests) { inclusive = true }
                    }
                    return@composable
                }

                AdminDeletionRequestsScreen(navController)
            }

            composable<Screen.Menu> {
                MenuScreen(
                    navController = navController,
                    cartViewModel = cartViewModel,
                )
            }

            composable<Screen.Cart> {
                CartScreen(
                    cartViewModel = cartViewModel
                )
            }

            composable<Screen.MenuDetails> {
                MenuDetailsScreen(navController)
            }
        }
    }
}