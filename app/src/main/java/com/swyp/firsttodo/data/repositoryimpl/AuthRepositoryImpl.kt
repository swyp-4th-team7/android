package com.swyp.firsttodo.data.repositoryimpl

import com.swyp.firsttodo.core.auth.manager.SessionManager
import com.swyp.firsttodo.core.common.util.suspendRunCatching
import com.swyp.firsttodo.core.network.util.ApiResponseHandler
import com.swyp.firsttodo.data.remote.datasource.AuthDataSource
import com.swyp.firsttodo.data.remote.dto.request.auth.SocialLoginRequestDto
import com.swyp.firsttodo.domain.model.LoginModel
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.model.SocialType
import com.swyp.firsttodo.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val sessionManager: SessionManager,
        private val apiResponseHandler: ApiResponseHandler,
        private val authDataSource: AuthDataSource,
    ) : AuthRepository {
        override suspend fun socialLogin(
            socialType: SocialType,
            token: String,
        ): Result<LoginModel> {
            val request = SocialLoginRequestDto(
                socialType = socialType.name,
                token = token,
            )

            val response = apiResponseHandler.safeApiCall {
                authDataSource.postSocialLogin(request)
            }.getOrElse { return Result.failure(it) }

            return suspendRunCatching {
                sessionManager.onLoginSuccess(
                    accessToken = response.accessToken,
                    refreshToken = response.refreshToken,
                    userType = response.userType,
                    profileCompleted = response.profileCompleted,
                )

                LoginModel(
                    userType = response.userType?.let { runCatching { Role.valueOf(it) }.getOrNull() },
                    profileCompleted = response.profileCompleted,
                )
            }
        }
    }
