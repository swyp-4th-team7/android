package com.swyp.firsttodo.data.remote.dto.response.growth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GrowthResponseDto(
    @SerialName("starCount")
    val starCount: Int,
    @SerialName("weekRange")
    val weekRange: String,
)
