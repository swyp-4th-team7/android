package com.swyp.firsttodo.data.remote.dto.request.habit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HabitPatchRequestDto(
    @SerialName("title")
    val title: String,
    @SerialName("duration")
    val duration: String,
    @SerialName("reward")
    val reward: String?,
    @SerialName("isCompleted")
    val isCompleted: Boolean,
)
