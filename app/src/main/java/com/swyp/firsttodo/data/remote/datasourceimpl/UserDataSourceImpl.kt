package com.swyp.firsttodo.data.remote.datasourceimpl

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.datasource.UserDataSource
import com.swyp.firsttodo.data.remote.dto.request.user.ProfileRequestDto
import com.swyp.firsttodo.data.remote.dto.response.user.MyInfoResponseDto
import com.swyp.firsttodo.data.remote.service.UserService
import javax.inject.Inject

class UserDataSourceImpl
    @Inject
    constructor(
        private val userService: UserService,
    ) : UserDataSource {
        override suspend fun getMyInfo(): BaseResponse<MyInfoResponseDto> = userService.getMyInfo()

        override suspend fun deleteLogout(): BaseResponse<Unit> = userService.deleteLogout()

        override suspend fun deleteAccount(): BaseResponse<Unit> = userService.deleteAccount()

        override suspend fun patchProfile(request: ProfileRequestDto): BaseResponse<Unit> =
            userService.patchProfile(
                request,
            )

        override suspend fun patchTerms(): BaseResponse<Unit> = userService.patchTerms()
    }
