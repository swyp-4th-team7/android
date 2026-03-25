package com.swyp.firsttodo.data.remote.dto.request.reward

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RewardRequestBody(
    @SerialName("reward")
    val reward: String,
)
