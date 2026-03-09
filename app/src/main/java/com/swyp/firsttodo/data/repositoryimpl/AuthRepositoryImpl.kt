package com.swyp.firsttodo.data.repositoryimpl

import com.swyp.firsttodo.core.auth.manager.AuthManager
import com.swyp.firsttodo.core.common.util.suspendRunCatching
import com.swyp.firsttodo.core.network.util.ApiResponseHandler
import com.swyp.firsttodo.data.remote.datasource.AuthDataSource
import com.swyp.firsttodo.data.remote.dto.request.auth.SocialLoginRequestDto
import com.swyp.firsttodo.data.repository.AuthRepository
import com.swyp.firsttodo.domain.model.SocialType
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val authManager: AuthManager,
        private val apiResponseHandler: ApiResponseHandler,
        private val authDataSource: AuthDataSource,
    ) : AuthRepository {
        override suspend fun socialLogin(
            socialType: SocialType,
            token: String,
        ): Result<Boolean> {
            val request = SocialLoginRequestDto(
                socialType = socialType.name,
                token = token,
            )

            val response = apiResponseHandler.safeApiCall {
                authDataSource.postSocialLogin(request)
            }.getOrElse { return Result.failure(it) }

            return suspendRunCatching {
                authManager.onLoginSuccess(response.accessToken, response.refreshToken)
                response.isNewUser
            }
        }
    }
