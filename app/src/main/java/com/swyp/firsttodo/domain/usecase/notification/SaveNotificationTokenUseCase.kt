package com.swyp.firsttodo.domain.usecase.notification

import com.swyp.firsttodo.core.auth.manager.AuthManager
import com.swyp.firsttodo.core.notification.NotificationTokenProvider
import com.swyp.firsttodo.data.repository.NotificationRepository
import timber.log.Timber
import javax.inject.Inject

class SaveNotificationTokenUseCase
    @Inject
    constructor(
        private val notificationRepository: NotificationRepository,
        private val notificationTokenProvider: NotificationTokenProvider,
        private val authManager: AuthManager,
    ) {
        suspend operator fun invoke() {
            if (!authManager.isLoggedIn.value) return

            notificationTokenProvider.getToken()?.let { token ->
                notificationRepository.saveNotificationToken(token)
                    .onSuccess { Timber.d("Notification token saved.") }
                    .onFailure { Timber.e(it, "Failed to save notification token.") }
            }
        }
    }
