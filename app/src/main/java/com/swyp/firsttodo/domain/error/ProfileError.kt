package com.swyp.firsttodo.domain.error

sealed class ProfileError(
    override val message: String?,
) : Throwable(message) {
    class NicknameEmpty(message: String) : ProfileError(message)

    class NicknameLength(message: String) : ProfileError(message)

    class NicknameSymbols(message: String) : ProfileError(message)

    class RoleEmpty(message: String) : ProfileError(message)
}
