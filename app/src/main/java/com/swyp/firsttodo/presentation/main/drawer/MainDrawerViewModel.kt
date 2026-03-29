package com.swyp.firsttodo.presentation.main.drawer

import androidx.lifecycle.viewModelScope
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.domain.repository.UserRepository
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
        private val userRepository: UserRepository,
    ) : BaseViewModel<MainDrawerUiState, MainDrawerSideEffect>(MainDrawerUiState()) {
        fun getMyInfo() {
            if (uiState.value.nickname is Async.Success) return

            viewModelScope.launch {
                userRepository.getMyInfo()
                    .onSuccess { info ->
                        if (info.nickname != null) {
                            updateState { copy(nickname = Async.Success(info.nickname)) }
                        }
                    }
            }
        }

        fun onDismiss() {
            sendEffect(MainDrawerSideEffect.Dismiss)
        }

        fun onFamilyClick() {
            sendThrottledEffect(MainDrawerSideEffect.NavigateToFamily)
        }

        fun onShareClick() {
            sendThrottledEffect(MainDrawerSideEffect.NavigateToShare)
        }

        fun closeDialog() {
            updateState { copy(showDialog = false, currentDrawer = null, dialogLoadingState = Async.Init) }
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
            if (uiState.value.dialogLoadingState is Async.Loading) return

            updateState { copy(dialogLoadingState = Async.Loading()) }

            viewModelScope.launch {
                logoutUseCase()
                    .onSuccess {
                        updateState { copy(dialogLoadingState = Async.Success(Unit)) }
                        closeDialog()
                        sendEffect(MainDrawerSideEffect.ShowSnackbar("로그아웃 되었습니다."))
                    }
                    .onFailure {
                        updateState { copy(dialogLoadingState = Async.Init) }
                        if (it is ApiError) sendEffect(MainDrawerSideEffect.ShowSnackbar(it.snackbarMsg()))
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
            if (uiState.value.dialogLoadingState is Async.Loading) return

            updateState { copy(dialogLoadingState = Async.Loading()) }

            viewModelScope.launch {
                deleteAccountUseCase()
                    .onSuccess {
                        updateState { copy(dialogLoadingState = Async.Success(Unit)) }
                        closeDialog()
                        sendEffect(MainDrawerSideEffect.ShowSnackbar("계정이 탈퇴되었습니다."))
                    }
                    .onFailure {
                        updateState { copy(dialogLoadingState = Async.Init) }
                        if (it is ApiError) sendEffect(MainDrawerSideEffect.ShowSnackbar(it.snackbarMsg()))
                    }
            }
        }
    }
