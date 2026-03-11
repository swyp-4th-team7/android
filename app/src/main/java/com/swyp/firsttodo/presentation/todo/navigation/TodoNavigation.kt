package com.swyp.firsttodo.presentation.todo.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.swyp.firsttodo.presentation.todo.TodoRoute
import kotlinx.serialization.Serializable

sealed interface TodoRoute {
    @Serializable
    data object Todo : TodoRoute
}

fun NavGraphBuilder.todoNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    composable<TodoRoute.Todo> {
        TodoRoute(
            modifier = Modifier.padding(paddingValues),
        )
    }
}
