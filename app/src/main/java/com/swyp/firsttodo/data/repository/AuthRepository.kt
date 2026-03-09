package com.swyp.firsttodo.data.repository

import com.swyp.firsttodo.domain.model.SocialType

interface AuthRepository {
    suspend fun socialLogin(
        socialType: SocialType,
        token: String,
    ): Result<Boolean>
}
