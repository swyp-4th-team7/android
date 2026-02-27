package com.swyp.firsttodo.presentation.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.presentation.auth.LoginRoute
import kotlinx.serialization.Serializable

sealed interface AuthRoute : Route {
    @Serializable
    data object Login : AuthRoute
}

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    composable<AuthRoute.Login> {
        LoginRoute(
            onNavigateToHome = {},
        )
    }
}
