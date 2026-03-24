package com.swyp.firsttodo.data.remote.datasourceimpl

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.datasource.RewardDataSource
import com.swyp.firsttodo.data.remote.dto.request.reward.RewardListResponseBody
import com.swyp.firsttodo.data.remote.dto.request.reward.RewardRequestBody
import com.swyp.firsttodo.data.remote.service.RewardService
import javax.inject.Inject

class RewardDataSourceImpl
    @Inject
    constructor(
        private val rewardService: RewardService,
    ) : RewardDataSource {
        override suspend fun patchInProgressHabit(
            habitId: Long,
            request: RewardRequestBody,
        ): BaseResponse<Unit> = rewardService.patchInProgressHabit(habitId, request)

        override suspend fun patchCompleteHabit(habitId: Long): BaseResponse<Unit> =
            rewardService.patchCompleteHabit(habitId)

        override suspend fun getRewards(status: String): BaseResponse<RewardListResponseBody> =
            rewardService.getRewards(status)
    }
