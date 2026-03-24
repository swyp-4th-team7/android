package com.swyp.firsttodo.domain.model.reward

data class RewardModel(
    val habitId: Long,
    val title: String,
    val nickname: String?,
    val duration: String,
    val reward: String,
    val status: RewardStatus,
)

enum class RewardStatus {
    IN_PROGRESS,
    CONFIRMING,
    WAITING,
    DONE,
}
