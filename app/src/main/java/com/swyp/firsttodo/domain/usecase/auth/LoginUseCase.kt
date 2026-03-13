package com.swyp.firsttodo.domain.usecase.auth

import com.swyp.firsttodo.core.notification.NotificationTokenProvider
import com.swyp.firsttodo.domain.model.SocialType
import com.swyp.firsttodo.domain.repository.AuthRepository
import com.swyp.firsttodo.domain.repository.NotificationRepository
import timber.log.Timber
import javax.inject.Inject

class LoginUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val notificationRepository: NotificationRepository,
        private val notificationTokenProvider: NotificationTokenProvider,
    ) {
        suspend operator fun invoke(
            socialType: SocialType,
            token: String,
        ): Result<Boolean> {
            val loginResult = authRepository.socialLogin(socialType, token)

            if (loginResult.isSuccess) {
                notificationTokenProvider.getToken()?.let { notificationToken ->
                    notificationRepository.saveNotificationToken(notificationToken)
                        .onFailure { t ->
                            Timber.e(t, "Login success but notification token save failed")
                        }
                }
            }

            return loginResult
        }
    }
