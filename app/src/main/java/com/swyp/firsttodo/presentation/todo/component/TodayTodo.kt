package com.swyp.firsttodo.presentation.todo.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.swyp.firsttodo.core.base.Async

data class TodayTodo(
    val id: Long,
    val todo: String,
    val isDone: Boolean,
    val category: String,
    val color: Color,
)

@Composable
fun TodayTodo(
    todos: Async<List<TodayTodo>>,
    onCheckClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
}
