package com.swyp.firsttodo.data.repository

import com.swyp.firsttodo.data.model.type.SocialType

interface AuthRepository {
    suspend fun socialLogin(
        socialType: SocialType,
        token: String,
    ): Result<Boolean>
}
