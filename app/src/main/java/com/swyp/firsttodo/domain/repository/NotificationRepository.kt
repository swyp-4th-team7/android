package com.swyp.firsttodo.domain.repository

interface NotificationRepository {
    suspend fun saveNotificationToken(token: String): Result<Unit>

    suspend fun deleteNotificationToken(token: String): Result<Unit>
}
