package com.swyp.firsttodo.data.remote.datasource

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.request.auth.SocialLoginRequestDto
import com.swyp.firsttodo.data.remote.dto.response.auth.LoginResponseDto

interface AuthDataSource {
    suspend fun postSocialLogin(request: SocialLoginRequestDto): BaseResponse<LoginResponseDto>

    suspend fun postReissue(refreshToken: String): BaseResponse<LoginResponseDto>
}
