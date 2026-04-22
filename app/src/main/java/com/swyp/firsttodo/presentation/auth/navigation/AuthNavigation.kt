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
import com.swyp.firsttodo.presentation.onboarding.navigation.navigateToOnboarding
import com.swyp.firsttodo.presentation.webview.navigation.navigateToWebView
import kotlinx.serialization.Serializable

sealed interface AuthRoute : Route {
    @Serializable
    data class Login(val isSessionExpired: Boolean = false) : AuthRoute
}

fun NavController.navigateToLogin(isSessionExpired: Boolean = false) {
    navigate(AuthRoute.Login(isSessionExpired)) {
        popUpTo(graph.id) { inclusive = true }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    navigateToTodo: () -> Unit,
    paddingValues: PaddingValues,
) {
    composable<AuthRoute.Login> {
        LoginRoute(
            popBackStack = navController::popBackStack,
            navigateToTodo = navigateToTodo,
            navigateToOnboarding = navController::navigateToOnboarding,
            navigateToWebView = navController::navigateToWebView,
            modifier = Modifier.padding(paddingValues),
        )
    }
}
