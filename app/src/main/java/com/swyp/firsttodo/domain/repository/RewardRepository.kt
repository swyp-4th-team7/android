package com.swyp.firsttodo.domain.repository

import com.swyp.firsttodo.domain.model.reward.RewardModel

interface RewardRepository {
    suspend fun startReward(
        habitId: Long,
        reward: String,
    ): Result<Unit>

    suspend fun completeReward(habitId: Long): Result<Unit>

    suspend fun getRewards(status: String): Result<List<RewardModel>>
}
