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
import com.swyp.firsttodo.presentation.growth.navigation.growthNavGraph
import com.swyp.firsttodo.presentation.habit.navigation.habitNavGraph
import com.swyp.firsttodo.presentation.main.bottombar.MainTab
import com.swyp.firsttodo.presentation.onboarding.navigation.onboardingNavGraph
import com.swyp.firsttodo.presentation.reward.navigation.rewardNavGraph
import com.swyp.firsttodo.presentation.todo.navigation.todoNavGraph
import com.swyp.firsttodo.presentation.webview.navigation.webViewNavGraph

@Composable
fun MainNavHost(
    navigator: MainNavigator,
    paddingValues: PaddingValues,
    startDestination: Route,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navigator.navController,
        startDestination = startDestination,
        modifier = modifier.fillMaxSize(),
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { -it / 3 } },
        popEnterTransition = { slideInHorizontally { -it / 3 } },
        popExitTransition = { slideOutHorizontally { it } },
    ) {
        authNavGraph(
            navController = navigator.navController,
            navigateToTodo = navigator::navigateToTodo,
            navigateToOnboarding = navigator::navigateToOnboarding,
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
        growthNavGraph(
            paddingValues = paddingValues,
            navController = navigator.navController,
        )
        webViewNavGraph(
            paddingValues = paddingValues,
            navController = navigator.navController,
        )
    }
}
