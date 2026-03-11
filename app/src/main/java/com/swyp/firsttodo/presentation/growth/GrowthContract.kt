package com.swyp.firsttodo.presentation.growth

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState

@Immutable
data class GrowthUiState(
    val data: String = "",
) : UiState

sealed interface GrowthSideEffect : UiEffect {
    data class ShowToast(val message: String) : GrowthSideEffect
}
