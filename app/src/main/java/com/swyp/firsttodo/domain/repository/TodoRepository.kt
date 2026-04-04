package com.swyp.firsttodo.domain.repository

import com.swyp.firsttodo.domain.model.todo.TodoCategoryModel
import com.swyp.firsttodo.domain.model.todo.TodoColor
import com.swyp.firsttodo.domain.model.todo.TodoListModel

interface TodoRepository {
    suspend fun createTodo(
        title: String,
        category: String,
        color: TodoColor,
    ): Result<Unit>

    suspend fun editTodo(
        todoId: Long,
        title: String? = null,
        category: String? = null,
        color: TodoColor? = null,
        completed: Boolean? = null,
    ): Result<Unit>

    suspend fun deleteTodo(todoId: Long): Result<Unit>

    suspend fun getTodos(): Result<TodoListModel>

    suspend fun getTodoCategories(): Result<List<TodoCategoryModel>>
}
