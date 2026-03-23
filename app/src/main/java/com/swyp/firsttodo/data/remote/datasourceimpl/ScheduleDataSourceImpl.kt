package com.swyp.firsttodo.data.remote.datasourceimpl

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.datasource.ScheduleDataSource
import com.swyp.firsttodo.data.remote.dto.request.schedule.ScheduleRequestDto
import com.swyp.firsttodo.data.remote.dto.response.schedule.ScheduleResponseDto
import com.swyp.firsttodo.data.remote.service.ScheduleService
import javax.inject.Inject

class ScheduleDataSourceImpl
    @Inject
    constructor(
        private val scheduleService: ScheduleService,
    ) : ScheduleDataSource {
        override suspend fun createSchedule(request: ScheduleRequestDto): BaseResponse<ScheduleResponseDto> =
            scheduleService.createSchedule(request)

        override suspend fun getSchedules(): BaseResponse<List<ScheduleResponseDto>> = scheduleService.getSchedules()

        override suspend fun patchSchedule(
            scheduleId: Long,
            request: ScheduleRequestDto,
        ): BaseResponse<Unit> = scheduleService.patchSchedule(scheduleId, request)

        override suspend fun deleteSchedule(scheduleId: Long): BaseResponse<Unit> =
            scheduleService.deleteSchedule(scheduleId)
    }
