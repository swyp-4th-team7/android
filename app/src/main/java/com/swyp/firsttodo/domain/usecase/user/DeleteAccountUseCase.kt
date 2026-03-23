package com.swyp.firsttodo.domain.usecase.user

import com.swyp.firsttodo.core.notification.NotificationTokenProvider
import com.swyp.firsttodo.domain.repository.NotificationRepository
import com.swyp.firsttodo.domain.repository.UserRepository
import javax.inject.Inject

class DeleteAccountUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val notificationRepository: NotificationRepository,
        private val notificationTokenProvider: NotificationTokenProvider,
    ) {
        suspend operator fun invoke(): Result<Unit> {
            notificationTokenProvider.getToken()?.let { token ->
                notificationRepository.deleteNotificationToken(token)
            }
            return userRepository.deleteAccount()
        }
    }
