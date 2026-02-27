package com.swyp.firsttodo.presentation.auth

import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.presentation.auth.launcher.GoogleLoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor() :
    BaseViewModel<LoginUiState, LoginSideEffect>(LoginUiState()) {
        fun onGoogleLoginClick() {
            if (uiState.value.idToken is Async.Loading) return

            sendEffect(LoginSideEffect.LaunchGoogleLogin)
        }

        fun onGoogleLoginResult(result: GoogleLoginResult) {
            when (result) {
                is GoogleLoginResult.Success -> {
                    updateState { copy(idToken = Async.Success(result.idToken)) }
                    Timber.i(result.idToken)
                    oAuthLogin(type = "GOOGLE")
                }

                is GoogleLoginResult.Cancelled -> {
                    updateState { copy(idToken = Async.Init) }
                }

                is GoogleLoginResult.NoCredential -> {
                    updateState { copy(idToken = Async.Empty) }
                    sendEffect(LoginSideEffect.ShowToast("구글 계정을 찾을 수 없어요. 다시 시도해주세요."))
                    Timber.e("No Credential")
                }

                is GoogleLoginResult.Error -> {
                    updateState { copy(idToken = Async.Failure(result.message)) }
                    sendEffect(LoginSideEffect.ShowToast("일시적인 오류가 발생했어요. 다시 시도해주세요."))
                    Timber.e(result.message)
                }
            }
        }

        private fun oAuthLogin(type: String) {
            if (uiState.value.idToken !is Async.Success) return

            // TODO: 로그인 API

            sendEffect(LoginSideEffect.NavigateToHome)
        }
    }
