package com.swyp.firsttodo.domain.model.reward

import com.swyp.firsttodo.domain.model.habit.HabitDuration

data class RewardModel(
    val habitId: Long,
    val title: String,
    val nickname: String?,
    val duration: HabitDuration,
    val reward: String,
    val status: RewardStatus,
    val isCompleted: Boolean,
)

enum class RewardStatus {
    IN_PROGRESS,
    REWARD_CHECKING,
    REWARD_WAITING,
    COMPLETE,
}
