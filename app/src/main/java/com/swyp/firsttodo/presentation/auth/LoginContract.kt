package com.swyp.firsttodo.presentation.auth

import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState

data class LoginUiState(
    val idToken: Async<String> = Async.Init,
) : UiState

sealed interface LoginSideEffect : UiEffect {
    data object NavigateToHome : LoginSideEffect

    data object LaunchGoogleLogin : LoginSideEffect

    data class ShowToast(val message: String) : LoginSideEffect
}
