package com.swyp.firsttodo.data.remote.datasource

import com.swyp.firsttodo.core.network.model.BaseResponse

interface UserDataSource {
    suspend fun deleteLogout(): BaseResponse<Unit>

    suspend fun deleteAccount(): BaseResponse<Unit>

    suspend fun patchProfile(): BaseResponse<Unit>

    suspend fun patchTerms(): BaseResponse<Unit>
}
