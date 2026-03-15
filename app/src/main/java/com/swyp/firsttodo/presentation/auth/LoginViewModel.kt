package com.swyp.firsttodo.presentation.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.domain.model.SocialType
import com.swyp.firsttodo.domain.usecase.auth.SocialLoginUseCase
import com.swyp.firsttodo.domain.usecase.user.LogoutUseCase
import com.swyp.firsttodo.presentation.auth.launcher.GoogleLoginResult
import com.swyp.firsttodo.presentation.auth.navigation.AuthRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val loginUseCase: SocialLoginUseCase,
        private val logoutUseCase: LogoutUseCase,
    ) :
    BaseViewModel<LoginUiState, LoginSideEffect>(LoginUiState()) {
        val isSessionExpired = savedStateHandle.toRoute<AuthRoute.Login>().isSessionExpired

        fun onBack() {
            if (isSessionExpired) return

            sendEffect(LoginSideEffect.PopBackStack)
        }

        fun onGoogleLoginClick() {
            if (uiState.value.loginState is Async.Loading) return
            if (uiState.value.token is Async.Loading) return

            updateState { copy(token = Async.Loading()) }
            sendEffect(LoginSideEffect.LaunchGoogleLogin)
        }

        fun onGoogleLoginResult(result: GoogleLoginResult) {
            when (result) {
                is GoogleLoginResult.Success -> {
                    updateState { copy(token = Async.Success(result.idToken)) }
                    socialLogin(type = SocialType.GOOGLE)
                }

                is GoogleLoginResult.Cancelled -> {
                    updateState { copy(token = Async.Init) }
                }

                is GoogleLoginResult.NoCredential -> {
                    updateState { copy(token = Async.Init) }
                    sendEffect(LoginSideEffect.ShowToast("구글 계정을 찾을 수 없어요. 다시 시도해주세요."))
                    Timber.w("No Credential")
                }

                is GoogleLoginResult.Error -> {
                    updateState { copy(token = Async.Init) }
                    sendEffect(LoginSideEffect.ShowToast("일시적인 오류가 발생했어요. 다시 시도해주세요."))
                    Timber.e(result.message)
                }
            }
        }

        private fun socialLogin(type: SocialType) {
            val token = (uiState.value.token as? Async.Success)?.data ?: return

            updateState { copy(loginState = Async.Loading()) }

            viewModelScope.launch {
                loginUseCase(
                    socialType = type,
                    token = token,
                ).onSuccess { isProfileComplete ->
                    updateState { copy(loginState = Async.Success(Unit)) }

                    when {
                        isSessionExpired -> sendEffect(LoginSideEffect.PopBackStack)
                        isProfileComplete -> sendEffect(LoginSideEffect.NavigateToHome)
                        else -> sendEffect(LoginSideEffect.NavigateToOnboarding)
                    }
                }.onFailure {
                    updateState { copy(loginState = Async.Init) }
                    sendEffect(LoginSideEffect.ShowToast("로그인에 실패했어요. 다시 시도해주세요."))
                    Timber.e(it)
                }
            }
        }

        fun onLogoutClick() {
            viewModelScope.launch {
                logoutUseCase()
                    .onSuccess {
                        sendEffect(LoginSideEffect.ShowToast("로그아웃 성공!"))
                    }.onFailure {
                        sendEffect(LoginSideEffect.ShowToast("로그아웃 실패.."))
                    }
            }
        }
    }
