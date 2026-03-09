package com.swyp.firsttodo.core.auth.manager

import com.swyp.firsttodo.core.auth.datasource.TokenDataSource
import com.swyp.firsttodo.core.base.UiEffect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

sealed interface AuthSideEffect : UiEffect {
    data object NavigateToLogin : AuthSideEffect
}

@Singleton
class AuthManager
    @Inject
    constructor(
        private val tokenDataSource: TokenDataSource,
    ) {
        private val _isLoggedIn = MutableStateFlow(false)
        val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

        private val _sideEffect = Channel<AuthSideEffect>(Channel.BUFFERED)
        val sideEffect: Flow<AuthSideEffect> = _sideEffect.receiveAsFlow()

        suspend fun initAuthState() {
            val hasTokens =
                !tokenDataSource.getAccessToken().isNullOrBlank() &&
                    !tokenDataSource.getRefreshToken().isNullOrBlank()

            _isLoggedIn.update { hasTokens }
            Timber.d("🔓 Login Status = $hasTokens")
        }

        suspend fun logout() {
            tokenDataSource.clearTokens()
            _isLoggedIn.update { false }
            _sideEffect.trySend(AuthSideEffect.NavigateToLogin)
            Timber.d("🔓 Logout")
        }

        suspend fun onLoginSuccess(
            accessToken: String,
            refreshToken: String,
        ) {
            tokenDataSource.saveTokens(accessToken, refreshToken)
            _isLoggedIn.update { true }
            Timber.d("🔓 Login")
        }
    }
