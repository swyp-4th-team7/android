package com.swyp.firsttodo.data.remote.dto.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyInfoResponseDto(
    @SerialName("id")
    val userId: Long,
    @SerialName("email")
    val email: String,
    @SerialName("nickname")
    val nickname: String?,
    @SerialName("userType")
    val userType: String?,
    @SerialName("profileCompleted")
    val profileCompleted: Boolean,
    @SerialName("termsAgreed")
    val termsAgreed: Boolean,
)
