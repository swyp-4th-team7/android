package com.swyp.firsttodo.domain.repository

import com.swyp.firsttodo.domain.model.ScheduleModel

interface ScheduleRepository {
    suspend fun createSchedule(
        title: String,
        category: String,
        scheduleDate: String,
    ): Result<Unit>

    suspend fun getSchedules(): Result<List<ScheduleModel>>

    suspend fun updateSchedule(
        scheduleId: Long,
        title: String,
        category: String,
        scheduleDate: String,
    ): Result<Unit>

    suspend fun deleteSchedule(scheduleId: Long): Result<Unit>
}
