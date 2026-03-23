package com.swyp.firsttodo.presentation.hamburger.share

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState

@Immutable
data class ShareUiState(
    val data: String = "",
) : UiState

sealed interface ShareSideEffect : UiEffect {
    data class ShowSnackbar(val message: String) : ShareSideEffect
}
