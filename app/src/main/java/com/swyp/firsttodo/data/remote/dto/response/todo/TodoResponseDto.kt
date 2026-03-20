package com.swyp.firsttodo.data.remote.dto.response.todo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoResponseDto(
    @SerialName("todoId")
    val todoId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("category")
    val category: String,
    @SerialName("color")
    val color: String,
    @SerialName("iscompleted")
    val iscompleted: Boolean,
)
