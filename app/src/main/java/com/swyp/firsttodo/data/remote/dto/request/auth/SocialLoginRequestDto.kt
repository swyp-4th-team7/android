package com.swyp.firsttodo.data.remote.dto.request.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocialLoginRequestDto(
    @SerialName("socialType")
    val socialType: String,
    @SerialName("token")
    val token: String,
)
