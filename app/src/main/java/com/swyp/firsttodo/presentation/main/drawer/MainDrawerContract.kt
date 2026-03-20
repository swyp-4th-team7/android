package com.swyp.firsttodo.presentation.main.drawer

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState

enum class DrawerType(
    val displayName: String,
) {
    FAMILY("가족보기"),
    SAHRE("공유관리"),
    LOGOUT("로그아웃"),
    WITHDRAWAL("계정탈퇴"),
}

@Immutable
data class MainDrawerUiState(
    val currentDrawer: DrawerType? = null,
    val nickname: Async<String> = Async.Init,
    val showDialog: Boolean = false,
    val dialogType: DrawerDialogType = DrawerDialogType.LOGOUT,
) : UiState

sealed interface MainDrawerSideEffect : UiEffect {
    data object Dismiss : MainDrawerSideEffect

    data object NavigateToLogin : MainDrawerSideEffect

    data object NavigateToFamily : MainDrawerSideEffect

    data object NavigateToShare : MainDrawerSideEffect

    data class ShowSnackbar(val message: String) : MainDrawerSideEffect
}
