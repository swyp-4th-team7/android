package com.swyp.firsttodo.data.remote.dto.request.schedule

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleRequestDto(
    @SerialName("title")
    val title: String,
    @SerialName("category")
    val category: String,
    @SerialName("scheduleDate")
    val scheduleDate: String,
)
