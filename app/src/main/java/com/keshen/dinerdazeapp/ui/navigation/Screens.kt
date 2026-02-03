package com.keshen.dinerdazeapp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable object Main: Screen()
    @Serializable object Home: Screen()
    @Serializable object SignIn: Screen()
    @Serializable object SignUp: Screen()
    @Serializable object RegistrationForm: Screen()
    @Serializable object Menu: Screen()
    @Serializable object Profile: Screen()
    @Serializable object Admin: Screen()
    @Serializable object Settings: Screen()
    @Serializable object AdminTotalUsers: Screen()
    @Serializable object AdminUserEdit : Screen()
    @Serializable object AdminMenu : Screen()
    @Serializable object AdminAddMenu : Screen()
    @Serializable object AdminEditMenu : Screen()
    @Serializable object AdminPosts : Screen()
}