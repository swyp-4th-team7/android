package com.swyp.firsttodo.presentation.splash.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.presentation.splash.SplashRouteScreen
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute : Route

fun NavGraphBuilder.splashNavGraph(
    resolvedDestination: StateFlow<Route?>,
    onNavigate: (Route) -> Unit,
) {
    composable<SplashRoute>(
        enterTransition = { fadeIn(tween(160)) },
        exitTransition = { fadeOut(tween(160)) },
        popEnterTransition = { fadeIn(tween(160)) },
        popExitTransition = { fadeOut(tween(160)) },
    ) {
        SplashRouteScreen(
            resolvedDestination = resolvedDestination,
            onNavigate = onNavigate,
        )
    }
}
