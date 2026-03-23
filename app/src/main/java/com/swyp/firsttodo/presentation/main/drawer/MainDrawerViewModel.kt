package com.swyp.firsttodo.presentation.main.drawer

import androidx.lifecycle.viewModelScope
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.domain.usecase.user.DeleteAccountUseCase
import com.swyp.firsttodo.domain.usecase.user.LogoutUseCase
import com.swyp.firsttodo.presentation.common.extension.snackbarMsg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainDrawerViewModel
    @Inject
    constructor(
        private val logoutUseCase: LogoutUseCase,
        private val deleteAccountUseCase: DeleteAccountUseCase,
    ) : BaseViewModel<MainDrawerUiState, MainDrawerSideEffect>(MainDrawerUiState()) {
        fun onDismiss() {
            sendEffect(MainDrawerSideEffect.Dismiss)
        }

        fun onFamilyClick() {
            sendEffect(MainDrawerSideEffect.NavigateToFamily)
        }

        fun onShareClick() {
            sendEffect(MainDrawerSideEffect.NavigateToShare)
        }

        fun closeDialog() {
            updateState { copy(showDialog = false, currentDrawer = null) }
        }

        fun onDialogConfirmBtnClick() {
            when (uiState.value.dialogType) {
                DrawerDialogType.LOGOUT -> onLogoutConfirm()
                DrawerDialogType.WITHDRAWAL -> onWithdrawalConfirm()
            }
        }

        fun onLogoutClick() {
            updateState {
                copy(
                    currentDrawer = DrawerType.LOGOUT,
                    showDialog = true,
                    dialogType = DrawerDialogType.LOGOUT,
                )
            }
        }

        private fun onLogoutConfirm() {
            viewModelScope.launch {
                logoutUseCase()
                    .onSuccess {
                        closeDialog()
                        sendEffect(MainDrawerSideEffect.ShowSnackbar("로그아웃 되었습니다."))
                    }
                    .onFailure { throwable ->
                        val message = if (throwable is ApiError) throwable.snackbarMsg() else ""
                        sendEffect(MainDrawerSideEffect.ShowSnackbar(message))
                    }
            }
        }

        fun onWithdrawalClick() {
            updateState {
                copy(
                    currentDrawer = DrawerType.WITHDRAWAL,
                    showDialog = true,
                    dialogType = DrawerDialogType.WITHDRAWAL,
                )
            }
        }

        private fun onWithdrawalConfirm() {
            viewModelScope.launch {
                deleteAccountUseCase()
                    .onSuccess {
                        closeDialog()
                        sendEffect(MainDrawerSideEffect.ShowSnackbar("계정이 탈퇴되었습니다."))
                    }
                    .onFailure { throwable ->
                        val message = if (throwable is ApiError) throwable.snackbarMsg() else ""
                        sendEffect(MainDrawerSideEffect.ShowSnackbar(message))
                    }
            }
        }
    }
