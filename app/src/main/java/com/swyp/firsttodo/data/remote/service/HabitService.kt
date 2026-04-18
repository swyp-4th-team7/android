package com.swyp.firsttodo.data.remote.service

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.request.habit.HabitPatchRequestDto
import com.swyp.firsttodo.data.remote.dto.request.habit.HabitPostRequestDto
import com.swyp.firsttodo.data.remote.dto.response.habit.FailedHabitListResponseDto
import com.swyp.firsttodo.data.remote.dto.response.habit.HabitListResponseDto
import com.swyp.firsttodo.data.remote.dto.response.habit.HabitResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface HabitService {
    @POST("/api/v1/habits")
    suspend fun postHabit(
        @Body request: HabitPostRequestDto,
    ): BaseResponse<HabitResponseDto>

    @PATCH("/api/v1/habits/{habitId}")
    suspend fun patchHabit(
        @Path("habitId") habitId: Long,
        @Body request: HabitPatchRequestDto,
    ): BaseResponse<Unit>

    @DELETE("/api/v1/habits/{habitId}")
    suspend fun deleteHabit(
        @Path("habitId") habitId: Long,
    ): BaseResponse<Unit>

    @GET("/api/v1/habits")
    suspend fun getHabitList(): BaseResponse<HabitListResponseDto>

    @GET("/api/v1/habits/failed")
    suspend fun getFailedHabitList(): BaseResponse<FailedHabitListResponseDto>
}
