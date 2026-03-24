package com.swyp.firsttodo.data.remote.datasourceimpl

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.datasource.HabitDataSource
import com.swyp.firsttodo.data.remote.dto.request.habit.HabitPatchRequestDto
import com.swyp.firsttodo.data.remote.dto.request.habit.HabitPostRequestDto
import com.swyp.firsttodo.data.remote.dto.response.habit.HabitResponseDto
import com.swyp.firsttodo.data.remote.dto.response.habit.HabitResponseListDto
import com.swyp.firsttodo.data.remote.service.HabitService
import javax.inject.Inject

class HabitDataSourceImpl
    @Inject
    constructor(
        private val habitService: HabitService,
    ) : HabitDataSource {
        override suspend fun postHabit(request: HabitPostRequestDto): BaseResponse<HabitResponseDto> =
            habitService.postHabit(request)

        override suspend fun patchHabit(
            habitId: Long,
            request: HabitPatchRequestDto,
        ): BaseResponse<Unit> =
            habitService.patchHabit(
                habitId,
                request,
            )

        override suspend fun deleteHabit(habitId: Long): BaseResponse<Unit> = habitService.deleteHabit(habitId)

        override suspend fun getHabitList(): BaseResponse<HabitResponseListDto> = habitService.getHabitList()
    }
