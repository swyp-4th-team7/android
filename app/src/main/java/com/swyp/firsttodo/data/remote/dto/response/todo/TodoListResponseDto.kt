package com.swyp.firsttodo.data.remote.dto.response.todo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoListResponseDto(
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("completedCount")
    val completedCount: Int,
    @SerialName("remainingCount")
    val remainingCount: Int,
    @SerialName("progressPercent")
    val progressPercent: Int,
    @SerialName("todos")
    val todos: List<TodoResponseDto>,
)
