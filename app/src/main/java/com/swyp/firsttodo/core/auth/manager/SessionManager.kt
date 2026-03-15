package com.swyp.firsttodo.core.auth.manager

import com.swyp.firsttodo.core.auth.datasource.SessionDataSource
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState
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

data class SessionState(
    val isInitialized: Boolean = false,
    val isLoggedIn: Boolean = false,
    val userType: String? = null,
    val isProfileCompleted: Boolean = false,
) : UiState

@Singleton
class SessionManager
    @Inject
    constructor(
        private val sessionDataSource: SessionDataSource,
    ) {
        private val _sessionState = MutableStateFlow(SessionState())
        val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

        private val _sideEffect = Channel<AuthSideEffect>(Channel.BUFFERED)
        val sideEffect: Flow<AuthSideEffect> = _sideEffect.receiveAsFlow()

        val isLoggedIn: Boolean
            get() = _sessionState.value.isLoggedIn

        suspend fun initSession() {
            val accessToken = sessionDataSource.getAccessToken()
            val refreshToken = sessionDataSource.getRefreshToken()
            val userType = sessionDataSource.getUserType()
            val profileCompleted = sessionDataSource.getProfileCompleted()

            val isLoggedIn = !accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()

            _sessionState.update {
                SessionState(
                    isInitialized = true,
                    isLoggedIn = isLoggedIn,
                    userType = userType,
                    isProfileCompleted = profileCompleted ?: false,
                )
            }

            Timber.d(
                "🔒 Session init → isLoggedIn=$isLoggedIn, userType=$userType, isProfileCompleted=$profileCompleted",
            )
        }

        suspend fun onLoginSuccess(
            accessToken: String,
            refreshToken: String,
            userType: String?,
            profileCompleted: Boolean,
        ) {
            sessionDataSource.saveTokens(accessToken, refreshToken)

            if (!userType.isNullOrBlank()) {
                sessionDataSource.saveSession(userType, profileCompleted)
            }

            _sessionState.update {
                it.copy(
                    isLoggedIn = true,
                    userType = userType,
                    isProfileCompleted = profileCompleted,
                )
            }

            Timber.d("🔒 Login (userType=$userType, isProfileCompleted=$profileCompleted)")
        }

        suspend fun clearSession() {
            sessionDataSource.clearTokens()
            _sessionState.update { SessionState(isInitialized = true) }
            _sideEffect.trySend(AuthSideEffect.NavigateToLogin)

            Timber.d("🔒 Session cleared")
        }

        suspend fun saveSession(
            userType: String,
            profileCompleted: Boolean,
        ) {
            sessionDataSource.saveSession(userType, profileCompleted)
            _sessionState.update {
                it.copy(
                    userType = userType,
                    isProfileCompleted = profileCompleted,
                )
            }

            Timber.d("💾 Session saved (userType=$userType, isProfileCompleted=$profileCompleted)")
        }
    }
