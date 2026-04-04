package com.swyp.firsttodo.data.repositoryimpl

import com.swyp.firsttodo.core.network.util.ApiResponseHandler
import com.swyp.firsttodo.data.remote.datasource.NotificationDataSource
import com.swyp.firsttodo.data.remote.dto.request.notification.NotificationRequestDto
import com.swyp.firsttodo.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl
    @Inject
    constructor(
        private val apiResponseHandler: ApiResponseHandler,
        private val notificationDataSource: NotificationDataSource,
    ) : NotificationRepository {
        override suspend fun saveNotificationToken(token: String): Result<Unit> =
            apiResponseHandler.safeApiCall {
                val request = NotificationRequestDto(
                    token = token,
                )
                notificationDataSource.postNotificationToken(request)
            }

        override suspend fun deleteNotificationToken(token: String): Result<Unit> =
            apiResponseHandler.safeApiCall {
                notificationDataSource.deleteNotificationToken(token)
            }
    }
