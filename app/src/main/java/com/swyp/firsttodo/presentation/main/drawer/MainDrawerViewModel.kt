package com.swyp.firsttodo.presentation.main.drawer

import android.os.Build
import androidx.lifecycle.viewModelScope
import com.swyp.firsttodo.BuildConfig
import com.swyp.firsttodo.core.analytics.AnalyticsEvent
import com.swyp.firsttodo.core.analytics.AnalyticsManager
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.core.common.extension.snackbarMsg
import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.domain.repository.UserRepository
import com.swyp.firsttodo.domain.usecase.user.DeleteAccountUseCase
import com.swyp.firsttodo.domain.usecase.user.LogoutUseCase
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
        private val analyticsManager: AnalyticsManager,
    ) : BaseViewModel<MainDrawerUiState, MainDrawerSideEffect>(MainDrawerUiState()) {
        fun getMyInfo() {
            if (currentState.nickname is Async.Success) return

            viewModelScope.launch {
                userRepository.getMyInfo()
                    .onSuccess { info ->
                        updateState {
                            copy(
                                nickname = if (info.nickname != null) Async.Success(info.nickname) else nickname,
                                userType = if (info.userType != null) Async.Success(info.userType) else userType,
                            )
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

        fun onContactClick() {
            val nickname = (currentState.nickname as? Async.Success)?.data ?: ""
            val accountType = when ((currentState.userType as? Async.Success)?.data) {
                "PARENT" -> "부모"
                "CHILD" -> "자녀"
                else -> ""
            }
            val subject = "[해봄] $accountType · $nickname 님의 메일입니다."
            val body = """
안녕하세요! 해봄 팀입니다. 🌱
불편한 점, 바라는 점, 버그, 칭찬까지 뭐든 편하게 적어 주세요.
더 좋은 앱으로 발전하겠습니다. :)

------

내용을 여기에 적어 주세요.
(버그라면 어떤 화면에서 발생했는지 함께 적어 주시면 도움이 됩니다.)

------

(아래의 내용을 지우시면 확인이 지연됩니다.)

App Version: ${BuildConfig.VERSION_NAME}
Build Number: ${BuildConfig.VERSION_CODE}
Model: ${Build.MODEL}
System Version: ${Build.VERSION.RELEASE}
Account Type: $accountType
            """.trimIndent()
            sendThrottledEffect(MainDrawerSideEffect.OpenMail(subject, body))
        }

        fun onReviewClick() {
            sendThrottledEffect(MainDrawerSideEffect.OpenPlayStore)
        }

        fun closeDialog() {
            updateState { copy(showDialog = false, currentDrawer = null, dialogLoadingState = Async.Init) }
        }

        fun onDialogConfirmBtnClick() {
            when (currentState.dialogType) {
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
            if (currentState.dialogLoadingState is Async.Loading) return

            updateState { copy(dialogLoadingState = Async.Loading()) }

            viewModelScope.launch {
                logoutUseCase()
                    .onSuccess {
                        analyticsManager.track(AnalyticsEvent.Logout)
                        updateState { copy(dialogLoadingState = Async.Success(Unit), nickname = Async.Init) }
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
            if (currentState.dialogLoadingState is Async.Loading) return

            updateState { copy(dialogLoadingState = Async.Loading()) }

            viewModelScope.launch {
                deleteAccountUseCase()
                    .onSuccess {
                        analyticsManager.track(AnalyticsEvent.Withdraw)
                        updateState { copy(dialogLoadingState = Async.Success(Unit), nickname = Async.Init) }
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
