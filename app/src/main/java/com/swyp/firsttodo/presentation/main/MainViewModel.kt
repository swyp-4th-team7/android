package com.swyp.firsttodo.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp.firsttodo.core.auth.manager.AuthManager
import com.swyp.firsttodo.domain.usecase.notification.SaveNotificationTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val authManager: AuthManager,
        private val saveNotificationTokenUseCase: SaveNotificationTokenUseCase,
    ) : ViewModel() {
        init {
            viewModelScope.launch {
                authManager.initAuthState()
                Timber.d("Initialized logged in state : ${authManager.isLoggedIn.value}")
                saveNotificationTokenUseCase()
            }
        }
    }
