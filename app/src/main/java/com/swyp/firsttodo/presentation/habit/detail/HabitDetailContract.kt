package com.swyp.firsttodo.presentation.habit.detail

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState
import com.swyp.firsttodo.domain.model.habit.HabitDuration

enum class HabitDetailScreenState {
    IDLE,
    CREATE,
    EDIT,
}

@Immutable
data class HabitDetailUiState(
    val screenState: HabitDetailScreenState = HabitDetailScreenState.IDLE,
    val duration: HabitDuration? = null,
) : UiState {
    val title = when (screenState) {
        HabitDetailScreenState.IDLE -> ""
        HabitDetailScreenState.CREATE -> "습관 만들기"
        HabitDetailScreenState.EDIT -> "습관 수정하기"
    }

    val description = when (screenState) {
        HabitDetailScreenState.IDLE -> ""
        HabitDetailScreenState.CREATE -> "습관을 만들고 매일 실천해보세요!"
        HabitDetailScreenState.EDIT -> "습관 정보를 수정하고 업데이트하세요."
    }

    val btnText = when (screenState) {
        HabitDetailScreenState.IDLE -> ""
        HabitDetailScreenState.CREATE -> "가족에게 습관 알리기"
        HabitDetailScreenState.EDIT -> "수정하기"
    }
}

sealed interface HabitDetailSideEffect : UiEffect {
    data object PopBackStack : HabitDetailSideEffect

    data class ShowToast(val message: String) : HabitDetailSideEffect
}
