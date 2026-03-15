package com.swyp.firsttodo.presentation.onboarding.navigation

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

fun NavGraphBuilder.onboardingNavGraph(navController: NavController) {
    composable<OnboardingRoute.Onboarding> {
        OnboardingRoute(
            onNavigateToHome = {},
        )
    }
}
