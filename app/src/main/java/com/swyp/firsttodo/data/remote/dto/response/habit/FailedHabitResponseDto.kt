package com.swyp.firsttodo.data.remote.dto.response.habit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FailedHabitListResponseDto(
    @SerialName("failedHabits")
    val failedHabits: List<FailedHabitResponseDto>,
)

@Serializable
data class FailedHabitResponseDto(
    @SerialName("habitId")
    val habitId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("duration")
    val duration: String,
    @SerialName("reward")
    val reward: String?,
)
