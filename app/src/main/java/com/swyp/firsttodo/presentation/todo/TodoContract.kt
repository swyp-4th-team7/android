package com.swyp.firsttodo.presentation.todo

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState

@Immutable
data class TodoUiState(
    val data: String = "",
) : UiState

sealed interface TodoSideEffect : UiEffect {
    data class ShowToast(val message: String) : TodoSideEffect
}
