package com.swyp.firsttodo.data.remote.dto.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileRequestDto(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("userType")
    val userType: String,
)
