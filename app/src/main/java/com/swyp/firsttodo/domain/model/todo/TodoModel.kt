package com.swyp.firsttodo.domain.model.todo

data class TodoModel(
    val todoId: Long,
    val title: String,
    val category: String,
    val color: TodoColor,
    val isCompleted: Boolean,
)

data class TodoListModel(
    val totalCount: Int,
    val completedCount: Int,
    val remainingCount: Int,
    val progressPercent: Int,
    val todos: List<TodoModel>,
)
