package com.swyp.firsttodo.data.remote.service

import com.swyp.firsttodo.core.network.model.BaseResponse
import retrofit2.http.DELETE
import retrofit2.http.PATCH

interface UserService {
    @DELETE("/api/v1/auth/logout")
    suspend fun deleteLogout(): BaseResponse<Unit>

    @DELETE("/api/v1/users/me")
    suspend fun deleteAccount(): BaseResponse<Unit>

    @PATCH("/api/v1/users/me")
    suspend fun patchProfile(): BaseResponse<Unit>

    @PATCH("/api/v1/users/me/terms")
    suspend fun patchTerms(): BaseResponse<Unit>
}
