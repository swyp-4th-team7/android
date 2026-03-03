package com.swyp.firsttodo.data.remote.datasource

import com.swyp.firsttodo.core.network.model.BaseResponse

interface UserDataSource {
    suspend fun deleteLogout(): BaseResponse<Unit>
}
