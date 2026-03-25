package com.swyp.firsttodo.data.mapper

import com.swyp.firsttodo.data.remote.dto.request.reward.RewardListResponseBody
import com.swyp.firsttodo.data.remote.dto.request.reward.RewardResponseBody
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.domain.model.reward.RewardModel
import com.swyp.firsttodo.domain.model.reward.RewardStatus

fun RewardListResponseBody.toModel(): List<RewardModel> = this.habitRewards.map { it.toModel() }

fun RewardResponseBody.toModel(): RewardModel =
    RewardModel(
        habitId = this.habitId,
        title = this.title,
        nickname = this.nickname,
        duration = runCatching { HabitDuration.valueOf(this.duration) }.getOrDefault(HabitDuration.SEVEN_DAYS),
        reward = this.reward,
        status = runCatching { RewardStatus.valueOf(this.status) }.getOrDefault(RewardStatus.IN_PROGRESS),
    )
