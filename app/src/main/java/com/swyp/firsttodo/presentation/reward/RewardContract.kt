package com.swyp.firsttodo.presentation.reward

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState

@Immutable
data class RewardUiState(
    val data: String = "",
) : UiState

sealed interface RewardSideEffect : UiEffect {
    data class ShowToast(val message: String) : RewardSideEffect
}
