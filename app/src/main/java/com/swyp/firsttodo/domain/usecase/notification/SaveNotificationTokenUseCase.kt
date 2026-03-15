package com.swyp.firsttodo.domain.usecase.notification

import com.swyp.firsttodo.core.auth.manager.SessionManager
import com.swyp.firsttodo.core.notification.NotificationTokenProvider
import com.swyp.firsttodo.domain.repository.NotificationRepository
import timber.log.Timber
import javax.inject.Inject

class SaveNotificationTokenUseCase
    @Inject
    constructor(
        private val notificationRepository: NotificationRepository,
        private val notificationTokenProvider: NotificationTokenProvider,
        private val sessionManager: SessionManager,
    ) {
        suspend operator fun invoke() {
            if (!sessionManager.isLoggedIn) return

            notificationTokenProvider.getToken()?.let { token ->
                notificationRepository.saveNotificationToken(token)
                    .onSuccess { Timber.d("Notification token saved.") }
                    .onFailure { Timber.e(it, "Failed to save notification token.") }
            }
        }
    }
