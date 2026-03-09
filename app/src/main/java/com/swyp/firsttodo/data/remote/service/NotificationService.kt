package com.swyp.firsttodo.data.remote.service

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.request.notification.NotificationRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Query

interface NotificationService {
    @POST("/api/v1/users/me/fcm-tokens")
    suspend fun postNotificationToken(
        @Body request: NotificationRequestDto,
    ): BaseResponse<Unit>

    @DELETE("/api/v1/users/me/fcm-tokens")
    suspend fun deleteNotificationToken(
        @Query("token") token: String,
    ): BaseResponse<Unit>
}
