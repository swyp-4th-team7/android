package com.swyp.firsttodo.data.remote.dto.response.todo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoCategoryListDto(
    @SerialName("categories")
    val categories: List<TodoCategoryDto>,
)

@Serializable
data class TodoCategoryDto(
    @SerialName("name")
    val name: String,
    @SerialName("label")
    val label: String,
)
