package com.swyp.firsttodo.data.remote.datasource

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.request.reward.RewardListResponseBody
import com.swyp.firsttodo.data.remote.dto.request.reward.RewardRequestBody

interface RewardDataSource {
    suspend fun patchInProgressHabit(
        habitId: Long,
        request: RewardRequestBody,
    ): BaseResponse<Unit>

    suspend fun patchCompleteHabit(habitId: Long): BaseResponse<Unit>

    suspend fun getRewards(status: String): BaseResponse<RewardListResponseBody>
}
