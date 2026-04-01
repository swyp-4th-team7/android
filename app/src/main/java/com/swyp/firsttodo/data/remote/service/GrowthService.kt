package com.swyp.firsttodo.data.remote.service

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.response.growth.ChildrenGrowthResponseDto
import com.swyp.firsttodo.data.remote.dto.response.growth.GrowthResponseDto
import retrofit2.http.GET

interface GrowthService {
    @GET("/api/v1/growth/todo")
    suspend fun getGrowthTodo(): BaseResponse<GrowthResponseDto>

    @GET("/api/v1/growth/habit")
    suspend fun getGrowthHabit(): BaseResponse<GrowthResponseDto>

    @GET("/api/v1/growth/children")
    suspend fun getGrowthChildren(): BaseResponse<ChildrenGrowthResponseDto>
}
