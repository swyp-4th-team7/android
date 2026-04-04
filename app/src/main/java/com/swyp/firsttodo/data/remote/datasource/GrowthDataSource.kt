package com.swyp.firsttodo.data.remote.datasource

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.response.growth.ChildrenGrowthResponseDto
import com.swyp.firsttodo.data.remote.dto.response.growth.GrowthResponseDto

interface GrowthDataSource {
    suspend fun getGrowthTodo(): BaseResponse<GrowthResponseDto>

    suspend fun getGrowthHabit(): BaseResponse<GrowthResponseDto>

    suspend fun getGrowthChildren(): BaseResponse<ChildrenGrowthResponseDto>
}
