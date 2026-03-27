package com.swyp.firsttodo.presentation.onboarding.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.presentation.onboarding.OnboardingRoute
import kotlinx.serialization.Serializable

sealed interface OnboardingRoute : Route {
    @Serializable
    data object Onboarding : OnboardingRoute
}

fun NavController.navigateToOnboarding() {
    navigate(OnboardingRoute.Onboarding) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.onboardingNavGraph(
    paddingValues: PaddingValues,
    navigateToTodo: () -> Unit,
) {
    composable<OnboardingRoute.Onboarding> (
        enterTransition = { fadeIn(tween(160)) },
        exitTransition = { fadeOut(tween(160)) },
        popEnterTransition = { fadeIn(tween(160)) },
        popExitTransition = { fadeOut(tween(160)) },
    ) {
        OnboardingRoute(
            navigateToTodo = navigateToTodo,
            modifier = Modifier.padding(paddingValues),
        )
    }
}
