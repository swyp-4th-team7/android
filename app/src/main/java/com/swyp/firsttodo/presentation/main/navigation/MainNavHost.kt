package com.swyp.firsttodo.presentation.main.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.swyp.firsttodo.presentation.auth.navigation.authNavGraph
import com.swyp.firsttodo.presentation.growth.navigation.growthNavGraph
import com.swyp.firsttodo.presentation.habit.navigation.habitNavGraph
import com.swyp.firsttodo.presentation.reward.navigation.rewardNavGraph
import com.swyp.firsttodo.presentation.todo.navigation.TodoRoute
import com.swyp.firsttodo.presentation.todo.navigation.todoNavGraph

@Composable
fun MainNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = TodoRoute.Todo,
        modifier = modifier.fillMaxSize(),
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { -it / 3 } },
        popEnterTransition = { slideInHorizontally { -it / 3 } },
        popExitTransition = { slideOutHorizontally { it } },
    ) {
        authNavGraph(
            navController = navController,
        )
        todoNavGraph(
            paddingValues = paddingValues,
            navController = navController,
        )
        habitNavGraph(
            paddingValues = paddingValues,
            navController = navController,
        )
        rewardNavGraph(
            paddingValues = paddingValues,
            navController = navController,
        )
        growthNavGraph(
            paddingValues = paddingValues,
            navController = navController,
        )
    }
}
