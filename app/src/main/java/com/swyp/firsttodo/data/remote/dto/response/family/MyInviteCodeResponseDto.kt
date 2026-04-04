package com.swyp.firsttodo.data.remote.dto.response.family

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyInviteCodeResponseDto(
    @SerialName("inviteCode")
    val inviteCode: String,
)
