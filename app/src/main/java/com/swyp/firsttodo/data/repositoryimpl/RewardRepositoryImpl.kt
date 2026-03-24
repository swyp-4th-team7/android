package com.swyp.firsttodo.data.repositoryimpl

import com.swyp.firsttodo.core.network.util.ApiResponseHandler
import com.swyp.firsttodo.data.mapper.toModel
import com.swyp.firsttodo.data.remote.datasource.RewardDataSource
import com.swyp.firsttodo.data.remote.dto.request.reward.RewardRequestBody
import com.swyp.firsttodo.domain.model.reward.RewardModel
import com.swyp.firsttodo.domain.repository.RewardRepository
import javax.inject.Inject

class RewardRepositoryImpl
    @Inject
    constructor(
        private val apiResponseHandler: ApiResponseHandler,
        private val rewardDataSource: RewardDataSource,
    ) : RewardRepository {
        override suspend fun startReward(
            habitId: Long,
            reward: String,
        ): Result<Unit> =
            apiResponseHandler.safeApiCall {
                rewardDataSource.patchInProgressHabit(habitId, RewardRequestBody(reward))
            }

        override suspend fun completeReward(habitId: Long): Result<Unit> =
            apiResponseHandler.safeApiCall {
                rewardDataSource.patchCompleteHabit(habitId)
            }

        override suspend fun getRewards(status: String): Result<List<RewardModel>> =
            apiResponseHandler.safeApiCall {
                rewardDataSource.getRewards(status)
            }.map { it.toModel() }
    }
