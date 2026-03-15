package com.swyp.firsttodo.domain.repository

import com.swyp.firsttodo.domain.model.LoginModel
import com.swyp.firsttodo.domain.model.SocialType

interface AuthRepository {
    suspend fun socialLogin(
        socialType: SocialType,
        token: String,
    ): Result<LoginModel>
}
