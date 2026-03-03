package com.swyp.firsttodo.data.remote.service

import com.swyp.firsttodo.core.network.model.BaseResponse
import retrofit2.http.DELETE

interface UserService {
    @DELETE("/api/v1/auth/logout")
    suspend fun deleteLogout(): BaseResponse<Unit>
}
