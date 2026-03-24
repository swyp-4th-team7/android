package com.swyp.firsttodo.data.remote.datasource

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.request.habit.HabitPatchRequestDto
import com.swyp.firsttodo.data.remote.dto.request.habit.HabitPostRequestDto
import com.swyp.firsttodo.data.remote.dto.response.habit.HabitResponseDto
import com.swyp.firsttodo.data.remote.dto.response.habit.HabitResponseListDto

interface HabitDataSource {
    suspend fun postHabit(request: HabitPostRequestDto): BaseResponse<HabitResponseDto>

    suspend fun patchHabit(
        habitId: Long,
        request: HabitPatchRequestDto,
    ): BaseResponse<Unit>

    suspend fun deleteHabit(habitId: Long): BaseResponse<Unit>

    suspend fun getHabitList(): BaseResponse<HabitResponseListDto>
}
