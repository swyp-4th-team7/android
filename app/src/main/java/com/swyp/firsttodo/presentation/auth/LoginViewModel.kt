package com.swyp.firsttodo.presentation.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.domain.model.SocialType
import com.swyp.firsttodo.domain.usecase.auth.SocialLoginUseCase
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
        private val socialLoginUseCase: SocialLoginUseCase,
    ) :
    BaseViewModel<LoginUiState, LoginSideEffect>(LoginUiState()) {
        val isSessionExpired = savedStateHandle.toRoute<AuthRoute.Login>().isSessionExpired

        init {
            if (isSessionExpired) {
                sendEffect(LoginSideEffect.ShowSnackbar("로그인이 만료되었어요. 다시 로그인 해주세요."))
            }
        }

        fun onBack() {
            if (isSessionExpired) return

            sendEffect(LoginSideEffect.PopBackStack)
        }

        fun onTosClick() {
            sendEffect(
                LoginSideEffect.NavigateToWebView(
                    title = "이용약관",
                    url = "https://www.naver.com/",
                ),
            )
        }

        fun onPrivacyClick() {
            sendEffect(
                LoginSideEffect.NavigateToWebView(
                    title = "개인정보 처리방침",
                    url = "https://www.google.com/",
                ),
            )
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
                    sendEffect(LoginSideEffect.ShowSnackbar("구글 계정을 찾을 수 없어요. 다시 시도해주세요."))
                    Timber.w("No Credential")
                }

                is GoogleLoginResult.Error -> {
                    updateState { copy(token = Async.Init) }
                    sendEffect(LoginSideEffect.ShowSnackbar("일시적인 오류가 발생했어요. 다시 시도해주세요."))
                    Timber.e(result.message)
                }
            }
        }

        private fun socialLogin(type: SocialType) {
            val token = (uiState.value.token as? Async.Success)?.data ?: return

            updateState { copy(loginState = Async.Loading()) }

            viewModelScope.launch {
                socialLoginUseCase(
                    socialType = type,
                    token = token,
                ).onSuccess { isProfileComplete ->
                    updateState { copy(loginState = Async.Success(Unit)) }

                    when {
                        isProfileComplete -> sendThrottledEffect(LoginSideEffect.NavigateToHome)
                        else -> sendThrottledEffect(LoginSideEffect.NavigateToOnboarding)
                    }
                }.onFailure {
                    updateState { copy(loginState = Async.Init) }
                    sendEffect(LoginSideEffect.ShowSnackbar("로그인에 실패했어요. 다시 시도해주세요."))
                    Timber.e(it)
                }
            }
        }
    }
