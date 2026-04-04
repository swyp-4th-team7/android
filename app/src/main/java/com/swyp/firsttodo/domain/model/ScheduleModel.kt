package com.swyp.firsttodo.domain.model

data class ScheduleModel(
    val scheduleId: Long,
    val title: String,
    val category: ScheduleCategory,
    val scheduleDate: String,
    val dDay: Long,
)
