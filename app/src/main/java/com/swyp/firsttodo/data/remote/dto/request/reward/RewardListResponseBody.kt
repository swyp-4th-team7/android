package com.swyp.firsttodo.data.remote.dto.request.reward

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RewardListResponseBody(
    @SerialName("habitRewards")
    val habitRewards: List<RewardResponseBody>,
)

@Serializable
data class RewardResponseBody(
    @SerialName("habitId")
    val habitId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("nickname")
    val nickname: String?,
    @SerialName("duration")
    val duration: String,
    @SerialName("reward")
    val reward: String,
    @SerialName("status")
    val status: String,
)
