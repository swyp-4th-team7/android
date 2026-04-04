package com.swyp.firsttodo.data.remote.service

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.request.auth.SocialLoginRequestDto
import com.swyp.firsttodo.data.remote.dto.response.auth.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("/api/v1/auth/login")
    suspend fun postSocialLogin(
        @Body request: SocialLoginRequestDto,
    ): BaseResponse<LoginResponseDto>

    @POST("/api/v1/auth/reissue")
    suspend fun postReissue(
        @Header("Authorization") refreshTokenWithBearer: String,
    ): BaseResponse<LoginResponseDto>
}
