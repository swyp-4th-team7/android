package com.swyp.firsttodo.data.remote.dto.response.habit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HabitListResponseDto(
    @SerialName("habits")
    val habits: List<HabitResponseDto>,
)

@Serializable
data class HabitResponseDto(
    @SerialName("habitId")
    val habitId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("duration")
    val duration: String,
    @SerialName("reward")
    val reward: String?,
    @SerialName("isCompleted")
    val isCompleted: Boolean,
)
