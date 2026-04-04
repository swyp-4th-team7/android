package com.swyp.firsttodo.presentation.habit.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.domain.model.habit.HabitModel
import com.swyp.firsttodo.presentation.habit.detail.HabitDetailRoute
import com.swyp.firsttodo.presentation.habit.list.HabitListRoute
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

const val HABIT_DETAIL_RESULT_KEY = "habit_detail_result"

sealed interface HabitRoute : Route {
    @Serializable
    data object Habit : HabitRoute

    @Serializable
    data class HabitDetail(val habitNavArgs: HabitNavArgs? = null) : HabitRoute
}

fun NavController.navigateToHabitDetail(habit: HabitModel?) {
    navigate(HabitRoute.HabitDetail(habit?.toNavArgs()))
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
    ) { backStackEntry ->
        val habitDetailResult by backStackEntry.savedStateHandle
            .getStateFlow<String?>(HABIT_DETAIL_RESULT_KEY, null)
            .collectAsStateWithLifecycle()

        HabitListRoute(
            navigateToHabitDetail = navController::navigateToHabitDetail,
            habitDetailResult = habitDetailResult,
            onDetailResultConsumed = {
                backStackEntry.savedStateHandle[HABIT_DETAIL_RESULT_KEY] = null
            },
            modifier = Modifier.padding(paddingValues),
        )
    }

    composable<HabitRoute.HabitDetail>(
        typeMap = mapOf(typeOf<HabitNavArgs?>() to HabitNavArgsNavType),
    ) {
        HabitDetailRoute(
            popBackStack = { resultMessage ->
                resultMessage?.let {
                    navController.previousBackStackEntry?.savedStateHandle?.set(HABIT_DETAIL_RESULT_KEY, it)
                }
                navController.popBackStack()
            },
        )
    }
}
