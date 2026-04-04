package com.swyp.firsttodo.domain.repository

import com.swyp.firsttodo.domain.model.user.MyInfoModel

interface UserRepository {
    suspend fun getMyInfo(): Result<MyInfoModel>

    suspend fun logout(): Result<Unit>

    suspend fun deleteAccount(): Result<Unit>

    suspend fun updateProfile(
        nickname: String,
        userType: String,
    ): Result<Unit>

    suspend fun updateTerms(): Result<Unit>
}
