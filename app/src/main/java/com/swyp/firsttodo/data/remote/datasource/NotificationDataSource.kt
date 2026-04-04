package com.swyp.firsttodo.data.remote.datasource

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.request.notification.NotificationRequestDto

interface NotificationDataSource {
    suspend fun postNotificationToken(request: NotificationRequestDto): BaseResponse<Unit>

    suspend fun deleteNotificationToken(token: String): BaseResponse<Unit>
}
