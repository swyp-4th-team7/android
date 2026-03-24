package com.swyp.firsttodo.data.remote.dto.response.schedule

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponseDto(
    @SerialName("scheduleId")
    val scheduleId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("category")
    val category: String,
    @SerialName("scheduleDate")
    val scheduleDate: String,
    @SerialName("dDay")
    val dDay: Long,
)
