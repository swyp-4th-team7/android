package com.swyp.firsttodo.data.remote.datasourceimpl

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.datasource.AuthDataSource
import com.swyp.firsttodo.data.remote.dto.request.auth.SocialLoginRequestDto
import com.swyp.firsttodo.data.remote.dto.response.auth.LoginResponseDto
import com.swyp.firsttodo.data.remote.service.AuthService
import javax.inject.Inject

class AuthDataSourceImpl
    @Inject
    constructor(
        private val authService: AuthService,
    ) : AuthDataSource {
        override suspend fun postSocialLogin(request: SocialLoginRequestDto): BaseResponse<LoginResponseDto> =
            authService.postSocialLogin(request)

        override suspend fun postReissue(refreshToken: String): BaseResponse<LoginResponseDto> =
            authService.postReissue("$BEARER $refreshToken")

        companion object {
            private const val BEARER = "Bearer"
        }
    }
