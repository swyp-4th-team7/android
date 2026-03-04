package com.swyp.firsttodo.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp.firsttodo.domain.usecase.notification.SaveNotificationTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val saveNotificationTokenUseCase: SaveNotificationTokenUseCase,
    ) : ViewModel() {
        fun saveNotificationToken() {
            viewModelScope.launch {
                saveNotificationTokenUseCase()
            }
        }
    }
