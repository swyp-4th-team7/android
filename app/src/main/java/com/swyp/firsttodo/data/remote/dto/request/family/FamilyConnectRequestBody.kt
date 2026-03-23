package com.swyp.firsttodo.data.remote.dto.request.family

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FamilyConnectRequestBody(
    @SerialName("inviteCode")
    val inviteCode: String,
)
