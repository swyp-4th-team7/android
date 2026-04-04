package com.swyp.firsttodo.data.remote.datasourceimpl

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.datasource.NotificationDataSource
import com.swyp.firsttodo.data.remote.dto.request.notification.NotificationRequestDto
import com.swyp.firsttodo.data.remote.service.NotificationService
import javax.inject.Inject

class NotificationDataSourceImpl
    @Inject
    constructor(
        private val notificationService: NotificationService,
    ) : NotificationDataSource {
        override suspend fun postNotificationToken(request: NotificationRequestDto): BaseResponse<Unit> =
            notificationService.postNotificationToken(request)

        override suspend fun deleteNotificationToken(token: String): BaseResponse<Unit> =
            notificationService.deleteNotificationToken(token)
    }
