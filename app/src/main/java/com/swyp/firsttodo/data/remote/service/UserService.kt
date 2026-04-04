package com.swyp.firsttodo.data.remote.service

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.request.user.ProfileRequestDto
import com.swyp.firsttodo.data.remote.dto.response.user.MyInfoResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserService {
    @GET("/api/v1/users/me")
    suspend fun getMyInfo(): BaseResponse<MyInfoResponseDto>

    @DELETE("/api/v1/auth/logout")
    suspend fun deleteLogout(): BaseResponse<Unit>

    @DELETE("/api/v1/users/me")
    suspend fun deleteAccount(): BaseResponse<Unit>

    @PATCH("/api/v1/users/me")
    suspend fun patchProfile(
        @Body request: ProfileRequestDto,
    ): BaseResponse<Unit>

    @PATCH("/api/v1/users/me/terms")
    suspend fun patchTerms(): BaseResponse<Unit>
}
