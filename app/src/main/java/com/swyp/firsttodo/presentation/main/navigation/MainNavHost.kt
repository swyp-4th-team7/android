package com.swyp.firsttodo.presentation.main.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.presentation.auth.navigation.authNavGraph
import com.swyp.firsttodo.presentation.habit.navigation.habitNavGraph
import com.swyp.firsttodo.presentation.hamburger.navigation.hamburgerNavGraph
import com.swyp.firsttodo.presentation.main.bottombar.MainTab
import com.swyp.firsttodo.presentation.onboarding.navigation.onboardingNavGraph
import com.swyp.firsttodo.presentation.reward.navigation.rewardNavGraph
import com.swyp.firsttodo.presentation.splash.navigation.SplashRoute
import com.swyp.firsttodo.presentation.splash.navigation.splashNavGraph
import com.swyp.firsttodo.presentation.todo.navigation.todoNavGraph
import com.swyp.firsttodo.presentation.webview.navigation.webViewNavGraph
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MainNavHost(
    navigator: MainNavigator,
    paddingValues: PaddingValues,
    resolvedDestination: StateFlow<Route?>,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navigator.navController,
        startDestination = SplashRoute,
        modifier = modifier.fillMaxSize(),
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { -it / 3 } },
        popEnterTransition = { slideInHorizontally { -it / 3 } },
        popExitTransition = { slideOutHorizontally { it } },
    ) {
        splashNavGraph(
            resolvedDestination = resolvedDestination,
            onNavigate = navigator::navigateFromSplash,
        )
        authNavGraph(
            navController = navigator.navController,
            navigateToTodo = navigator::navigateToTodo,
            paddingValues = paddingValues,
        )
        onboardingNavGraph(
            paddingValues = paddingValues,
            navigateToTodo = navigator::navigateToTodo,
        )
        todoNavGraph(
            paddingValues = paddingValues,
            navController = navigator.navController,
        )
        habitNavGraph(
            paddingValues = paddingValues,
            navController = navigator.navController,
        )
        rewardNavGraph(
            paddingValues = paddingValues,
            navController = navigator.navController,
            navigateToHabit = { navigator.navigate(MainTab.HABIT) },
        )
        webViewNavGraph(
            paddingValues = paddingValues,
            navController = navigator.navController,
        )
        hamburgerNavGraph(
            paddingValues = paddingValues,
            navController = navigator.navController,
        )
    }
}
