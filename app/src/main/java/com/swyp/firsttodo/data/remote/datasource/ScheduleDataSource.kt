package com.swyp.firsttodo.data.remote.datasource

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.request.schedule.ScheduleRequestDto
import com.swyp.firsttodo.data.remote.dto.response.schedule.ScheduleResponseDto

interface ScheduleDataSource {
    suspend fun createSchedule(request: ScheduleRequestDto): BaseResponse<ScheduleResponseDto>

    suspend fun getSchedules(): BaseResponse<List<ScheduleResponseDto>>

    suspend fun patchSchedule(
        scheduleId: Long,
        request: ScheduleRequestDto,
    ): BaseResponse<Unit>

    suspend fun deleteSchedule(scheduleId: Long): BaseResponse<Unit>
}
