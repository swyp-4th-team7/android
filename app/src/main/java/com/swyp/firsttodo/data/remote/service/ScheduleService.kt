package com.swyp.firsttodo.data.remote.service

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.request.schedule.ScheduleRequestDto
import com.swyp.firsttodo.data.remote.dto.response.schedule.ScheduleResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ScheduleService {
    @POST("/api/v1/schedules")
    suspend fun createSchedule(
        @Body request: ScheduleRequestDto,
    ): BaseResponse<ScheduleResponseDto>

    @GET("/api/v1/schedules")
    suspend fun getSchedules(): BaseResponse<List<ScheduleResponseDto>>

    @PATCH("/api/v1/schedules/{scheduleId}")
    suspend fun patchSchedule(
        @Path("scheduleId") scheduleId: Long,
        @Body request: ScheduleRequestDto,
    ): BaseResponse<Unit>

    @DELETE("/api/v1/schedules/{scheduleId}")
    suspend fun deleteSchedule(
        @Path("scheduleId") scheduleId: Long,
    ): BaseResponse<Unit>
}
