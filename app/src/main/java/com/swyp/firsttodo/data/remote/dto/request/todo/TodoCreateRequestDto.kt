package com.swyp.firsttodo.data.remote.dto.request.todo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoCreateRequestDto(
    @SerialName("title")
    val title: String,
    @SerialName("category")
    val category: String,
    @SerialName("color")
    val color: String,
)
