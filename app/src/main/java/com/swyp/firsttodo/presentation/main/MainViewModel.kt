package com.swyp.firsttodo.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp.firsttodo.core.auth.manager.AuthSideEffect
import com.swyp.firsttodo.core.auth.manager.SessionManager
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.domain.usecase.notification.SaveNotificationTokenUseCase
import com.swyp.firsttodo.presentation.auth.navigation.AuthRoute
import com.swyp.firsttodo.presentation.onboarding.navigation.OnboardingRoute
import com.swyp.firsttodo.presentation.todo.navigation.TodoRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val sessionManager: SessionManager,
        private val saveNotificationTokenUseCase: SaveNotificationTokenUseCase,
    ) : ViewModel() {
        val sideEffect: Flow<AuthSideEffect> = sessionManager.sideEffect

        val startDestination: StateFlow<Route?> = sessionManager.sessionState
            .map { state ->
                if (!state.isInitialized) return@map null

                val dest = when {
                    !state.isLoggedIn -> AuthRoute.Login
                    state.isLoggedIn && state.userType != null && state.isProfileCompleted -> TodoRoute.Todo
                    else -> OnboardingRoute.Onboarding
                }

                Timber.d("💗 Start Destination : $dest")

                dest
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = null,
            )

        init {
            viewModelScope.launch {
                sessionManager.initSession()
                Timber.d("Session init complete → ${sessionManager.sessionState.value}")
                saveNotificationTokenUseCase()
            }
        }
    }
