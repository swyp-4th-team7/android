package com.swyp.firsttodo.data.remote.service

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.request.reward.RewardRequestBody
import com.swyp.firsttodo.data.remote.dto.response.reward.RewardListResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface RewardService {
    @PATCH("/api/v1/habits/{habitId}/status/in-progress")
    suspend fun patchInProgressHabit(
        @Path("habitId") habitId: Long,
        @Body request: RewardRequestBody,
    ): BaseResponse<Unit>

    @PATCH("/api/v1/habits/{habitId}/status/complete")
    suspend fun patchCompleteHabit(
        @Path("habitId") habitId: Long,
    ): BaseResponse<Unit>

    @GET("/api/v1/habits/rewards")
    suspend fun getRewards(
        @Query("status") status: String,
    ): BaseResponse<RewardListResponseBody>
}
