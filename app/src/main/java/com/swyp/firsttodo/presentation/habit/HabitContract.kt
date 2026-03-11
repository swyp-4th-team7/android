package com.swyp.firsttodo.presentation.habit

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState

@Immutable
data class HabitUiState(
    val data: String = "",
) : UiState

sealed interface HabitSideEffect : UiEffect {
    data class ShowToast(val message: String) : HabitSideEffect
}
