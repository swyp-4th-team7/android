package com.swyp.firsttodo.presentation.habit.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.swyp.firsttodo.presentation.habit.HabitRoute
import kotlinx.serialization.Serializable

sealed interface HabitRoute {
    @Serializable
    data object Habit : HabitRoute
}

fun NavGraphBuilder.habitNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    composable<HabitRoute.Habit> {
        HabitRoute(
            modifier = Modifier.padding(paddingValues),
        )
    }
}
