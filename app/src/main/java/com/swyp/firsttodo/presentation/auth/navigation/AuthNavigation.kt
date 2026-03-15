package com.swyp.firsttodo.presentation.auth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.presentation.auth.LoginRoute
import kotlinx.serialization.Serializable

sealed interface AuthRoute : Route {
    @Serializable
    data class Login(val isSessionExpired: Boolean = false) : AuthRoute
}

fun NavController.navigateToLogin(isSessionExpired: Boolean = false) {
    navigate(AuthRoute.Login(isSessionExpired))
}

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    navigateToTodo: () -> Unit,
    navigateToOnboarding: () -> Unit,
    paddingValues: PaddingValues,
) {
    composable<AuthRoute.Login> {
        LoginRoute(
            onPopBackStack = navController::popBackStack,
            onNavigateToTodo = navigateToTodo,
            onNavigateToOnboarding = navigateToOnboarding,
            modifier = Modifier.padding(paddingValues),
        )
    }
}
