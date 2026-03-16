package com.swyp.firsttodo.presentation.main.drawer

import com.swyp.firsttodo.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainDrawerViewModel
    @Inject
    constructor() : BaseViewModel<MainDrawerUiState, MainDrawerSideEffect>(MainDrawerUiState()) {
        fun onDismiss() {
            sendEffect(MainDrawerSideEffect.Dismiss)
        }

        fun onFamilyClick() {
            sendEffect(MainDrawerSideEffect.NavigateToFamily)
        }

        fun onShareClick() {
            sendEffect(MainDrawerSideEffect.NavigateToShare)
        }

        fun onLogoutClick() {
            updateState { copy(showLogoutDialog = true) }
        }

        fun onLogoutCancel() {
            updateState { copy(showLogoutDialog = false) }
        }

        fun onLogoutConfirm() {
            // TODO: 로그아웃 유스케이스 -> NavigateToLogin
        }

        fun onWithdrawalClick() {
            updateState { copy(showWithdrawalDialog = true) }
        }

        fun onWithdrawalCancel() {
            updateState { copy(showWithdrawalDialog = false) }
        }

        fun onWithdrawalConfirm() {
            // TODO: 회원탈퇴 유스케이스 -> NavigateToLogin
        }
    }
