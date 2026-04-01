package com.swyp.firsttodo.data.remote.datasourceimpl

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.datasource.GrowthDataSource
import com.swyp.firsttodo.data.remote.dto.response.growth.ChildrenGrowthResponseDto
import com.swyp.firsttodo.data.remote.dto.response.growth.GrowthResponseDto
import com.swyp.firsttodo.data.remote.service.GrowthService
import javax.inject.Inject

class GrowthDataSourceImpl
    @Inject
    constructor(
        private val growthService: GrowthService,
    ) : GrowthDataSource {
        override suspend fun getGrowthTodo(): BaseResponse<GrowthResponseDto> = growthService.getGrowthTodo()

        override suspend fun getGrowthHabit(): BaseResponse<GrowthResponseDto> = growthService.getGrowthHabit()

        override suspend fun getGrowthChildren(): BaseResponse<ChildrenGrowthResponseDto> =
            growthService.getGrowthChildren()
    }
