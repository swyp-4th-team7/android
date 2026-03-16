package com.swyp.firsttodo.domain.usecase.user

import com.swyp.firsttodo.core.auth.manager.SessionManager
import com.swyp.firsttodo.core.common.util.suspendRunCatching
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.repository.UserRepository
import javax.inject.Inject

class SaveOnboardingProfile
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val sessionManager: SessionManager,
    ) {
        suspend operator fun invoke(
            nickname: String,
            role: Role,
        ): Result<Unit> {
            val remoteResult = userRepository.updateProfile(nickname, role.request)

            return if (remoteResult.isSuccess) {
                suspendRunCatching { sessionManager.saveSession(role.name, true) }
            } else {
                remoteResult
            }
        }
    }
