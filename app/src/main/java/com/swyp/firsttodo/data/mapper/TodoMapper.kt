package com.swyp.firsttodo.data.mapper

import com.swyp.firsttodo.data.remote.dto.response.todo.TodoCategoryDto
import com.swyp.firsttodo.data.remote.dto.response.todo.TodoCategoryListDto
import com.swyp.firsttodo.data.remote.dto.response.todo.TodoListResponseDto
import com.swyp.firsttodo.data.remote.dto.response.todo.TodoResponseDto
import com.swyp.firsttodo.domain.model.todo.TodoCategoryModel
import com.swyp.firsttodo.domain.model.todo.TodoColor
import com.swyp.firsttodo.domain.model.todo.TodoListModel
import com.swyp.firsttodo.domain.model.todo.TodoModel

fun String.toTodoColor(): TodoColor = runCatching { TodoColor.valueOf(this) }.getOrDefault(TodoColor.GRAY)

fun TodoResponseDto.toModel(): TodoModel =
    TodoModel(
        todoId = todoId,
        title = title,
        category = category,
        color = color.toTodoColor(),
        isCompleted = iscompleted,
    )

fun TodoCategoryDto.toModel(): TodoCategoryModel =
    TodoCategoryModel(
        name = name,
        label = label,
    )

fun TodoCategoryListDto.toModel(): List<TodoCategoryModel> = categories.map { it.toModel() }

fun TodoListResponseDto.toModel(): TodoListModel =
    TodoListModel(
        totalCount = totalCount,
        completedCount = completedCount,
        remainingCount = remainingCount,
        progressPercent = progressPercent,
        todos = todos.map { it.toModel() },
    )
