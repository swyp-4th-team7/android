package com.swyp.firsttodo.data.remote.dto.response.growth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChildrenGrowthResponseDto(
    @SerialName("children")
    val children: List<ChildGrowthResponseDto>,
)

@Serializable
data class ChildGrowthResponseDto(
    @SerialName("childId")
    val childId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("todoStarCount")
    val todoStarCount: Int,
    @SerialName("habitStarCount")
    val habitStarCount: Int,
    @SerialName("weekRange")
    val weekRange: String,
)
