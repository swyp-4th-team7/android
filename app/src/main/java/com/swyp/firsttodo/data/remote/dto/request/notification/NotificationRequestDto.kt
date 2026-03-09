package com.swyp.firsttodo.data.remote.dto.request.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationRequestDto(
    @SerialName("token")
    val token: String,
    @SerialName("platform")
    val platform: String = ANDROID,
) {
    companion object {
        private const val ANDROID = "ANDROID"
    }
}
