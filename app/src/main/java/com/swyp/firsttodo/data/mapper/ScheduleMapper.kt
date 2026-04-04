package com.swyp.firsttodo.data.mapper

import com.swyp.firsttodo.data.remote.dto.response.schedule.ScheduleResponseDto
import com.swyp.firsttodo.domain.model.ScheduleCategory
import com.swyp.firsttodo.domain.model.ScheduleModel

fun ScheduleResponseDto.toModel(): ScheduleModel =
    ScheduleModel(
        scheduleId = scheduleId,
        title = title,
        category = ScheduleCategory.entries.firstOrNull { it.request == category } ?: ScheduleCategory.SCHOOL_EXAM,
        scheduleDate = scheduleDate,
        dDay = dDay,
    )
