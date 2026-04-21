package com.swyp.firsttodo.data.remote.datasource

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.request.habit.FailedHabitRequestDto
import com.swyp.firsttodo.data.remote.dto.request.habit.HabitPatchRequestDto
import com.swyp.firsttodo.data.remote.dto.request.habit.HabitPostRequestDto
import com.swyp.firsttodo.data.remote.dto.response.habit.FailedHabitListResponseDto
import com.swyp.firsttodo.data.remote.dto.response.habit.HabitListResponseDto
import com.swyp.firsttodo.data.remote.dto.response.habit.HabitResponseDto

interface HabitDataSource {
    suspend fun postHabit(request: HabitPostRequestDto): BaseResponse<HabitResponseDto>

    suspend fun patchHabit(
        habitId: Long,
        request: HabitPatchRequestDto,
    ): BaseResponse<Unit>

    suspend fun deleteHabit(habitId: Long): BaseResponse<Unit>

    suspend fun getHabitList(): BaseResponse<HabitListResponseDto>

    suspend fun getFailedHabitList(): BaseResponse<FailedHabitListResponseDto>

    suspend fun patchFailedHabit(
        habitId: Long,
        request: FailedHabitRequestDto,
    ): BaseResponse<Unit>
}
