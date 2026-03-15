package com.swyp.firsttodo.presentation.habit.navigation

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
import com.swyp.firsttodo.domain.model.habit.Habit
import com.swyp.firsttodo.presentation.habit.detail.HabitDetailRoute
import com.swyp.firsttodo.presentation.habit.list.HabitListRoute
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

sealed interface HabitRoute : Route {
    @Serializable
    data object Habit : HabitRoute

    @Serializable
    data class HabitDetail(val habitNavArgs: HabitNavArgs? = null) : HabitRoute
}

fun NavController.navigateToHabitDetail(habit: Habit?) {
    navigate(HabitRoute.HabitDetail(habit?.toNavArgs() ?: null))
}

fun NavGraphBuilder.habitNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    composable<HabitRoute.Habit>(
        enterTransition = { fadeIn(tween(160)) },
        exitTransition = { fadeOut(tween(160)) },
        popEnterTransition = { fadeIn(tween(160)) },
        popExitTransition = { fadeOut(tween(160)) },
    ) {
        HabitListRoute(
            navigateToHabitDetail = navController::navigateToHabitDetail,
            modifier = Modifier.padding(paddingValues),
        )
    }

    composable<HabitRoute.HabitDetail>(
        typeMap = mapOf(typeOf<HabitNavArgs?>() to HabitNavArgsNavType),
    ) {
        HabitDetailRoute(
            onPopBackStack = navController::popBackStack,
            modifier = Modifier.padding(paddingValues),
        )
    }
}
