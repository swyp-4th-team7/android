package com.swyp.firsttodo.data.repository

interface NotificationRepository {
    suspend fun saveNotificationToken(token: String): Result<Unit>

    suspend fun deleteNotificationToken(token: String): Result<Unit>
}
