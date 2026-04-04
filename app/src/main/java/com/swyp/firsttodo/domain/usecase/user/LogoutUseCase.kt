package com.swyp.firsttodo.domain.usecase.user

import com.swyp.firsttodo.core.notification.NotificationTokenProvider
import com.swyp.firsttodo.domain.repository.NotificationRepository
import com.swyp.firsttodo.domain.repository.UserRepository
import timber.log.Timber
import javax.inject.Inject

class LogoutUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val notificationRepository: NotificationRepository,
        private val notificationTokenProvider: NotificationTokenProvider,
    ) {
        suspend operator fun invoke(): Result<Unit> {
            notificationTokenProvider.getToken()?.let { token ->
                notificationRepository.deleteNotificationToken(token)
                    .onFailure { t ->
                        Timber.e(t, "Notification token delete failed.")
                    }
            }
            return userRepository.logout()
        }
    }
