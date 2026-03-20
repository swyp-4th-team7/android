package com.swyp.firsttodo.data.remote.dto.request.todo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoEditRequestDto(
    @SerialName("title")
    val title: String? = null,
    @SerialName("category")
    val category: String? = null,
    @SerialName("color")
    val color: String? = null,
    @SerialName("completed")
    val completed: Boolean? = null,
)
