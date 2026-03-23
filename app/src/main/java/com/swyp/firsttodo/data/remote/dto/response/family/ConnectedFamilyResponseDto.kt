package com.swyp.firsttodo.data.remote.dto.response.family

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConnectedFamilyResponseDto(
    @SerialName("members")
    val members: List<ConnectedFamilyMemberDto>,
)

@Serializable
data class ConnectedFamilyMemberDto(
    @SerialName("userId")
    val userId: Long,
    @SerialName("nickname")
    val nickname: String,
)
