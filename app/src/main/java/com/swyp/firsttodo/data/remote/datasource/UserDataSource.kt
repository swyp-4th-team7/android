package com.swyp.firsttodo.data.remote.datasource

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.request.user.ProfileRequestDto
import com.swyp.firsttodo.data.remote.dto.response.user.MyInfoResponseDto

interface UserDataSource {
    suspend fun getMyInfo(): BaseResponse<MyInfoResponseDto>

    suspend fun deleteLogout(): BaseResponse<Unit>

    suspend fun deleteAccount(): BaseResponse<Unit>

    suspend fun patchProfile(request: ProfileRequestDto): BaseResponse<Unit>

    suspend fun patchTerms(): BaseResponse<Unit>
}
