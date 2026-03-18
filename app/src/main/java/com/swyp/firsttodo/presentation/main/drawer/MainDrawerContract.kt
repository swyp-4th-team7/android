package com.swyp.firsttodo.presentation.main.drawer

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState

@Immutable
data class MainDrawerUiState(
    val nickname: Async<String> = Async.Init,
    val showLogoutDialog: Boolean = false,
    val showWithdrawalDialog: Boolean = false,
) : UiState

sealed interface MainDrawerSideEffect : UiEffect {
    data object Dismiss : MainDrawerSideEffect

    data object NavigateToLogin : MainDrawerSideEffect

    data object NavigateToFamily : MainDrawerSideEffect

    data object NavigateToShare : MainDrawerSideEffect

    data class ShowSnackbar(val message: String) : MainDrawerSideEffect
}
