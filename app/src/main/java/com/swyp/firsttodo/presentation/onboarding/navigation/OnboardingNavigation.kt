package com.swyp.firsttodo.presentation.onboarding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.presentation.onboarding.OnboardingRoute
import kotlinx.serialization.Serializable

sealed interface OnboardingRoute : Route {
    @Serializable
    data object Onboarding : OnboardingRoute
}

fun NavGraphBuilder.onboardingNavGraph(
    paddingValues: PaddingValues,
    navigateToTodo: () -> Unit,
) {
    composable<OnboardingRoute.Onboarding> {
        OnboardingRoute(
            navigateToTodo = navigateToTodo,
            modifier = Modifier.padding(paddingValues),
        )
    }
}
