package com.swyp.firsttodo.presentation.habit.detail

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState
import com.swyp.firsttodo.domain.model.habit.HabitDuration

enum class HabitDetailScreenType {
    IDLE,
    CHILD,
    PARENT,
}

enum class HabitDetailScreenState {
    IDLE,
    CREATE,
    EDIT,
    RETRY,
}

@Immutable
data class HabitDetailUiState(
    val screenType: HabitDetailScreenType = HabitDetailScreenType.IDLE,
    val screenState: HabitDetailScreenState = HabitDetailScreenState.IDLE,
    val habitId: Long? = null,
    val duration: HabitDuration? = null,
    val isCompleted: Boolean? = null,
    val loadingState: Async<Unit> = Async.Init,
) : UiState {
    val title = when (screenState) {
        HabitDetailScreenState.IDLE -> ""
        HabitDetailScreenState.CREATE -> "습관 만들기"
        HabitDetailScreenState.EDIT -> "습관 수정하기"
        HabitDetailScreenState.RETRY -> "습관 재도전하기"
    }

    val description = when (screenState) {
        HabitDetailScreenState.IDLE -> ""
        HabitDetailScreenState.CREATE -> "습관을 만들고 매일 실천해 보세요!"
        HabitDetailScreenState.EDIT -> "습관 정보를 수정하고 업데이트하세요."
        HabitDetailScreenState.RETRY -> if (screenType == HabitDetailScreenType.PARENT) {
            "실패한 습관의 정보를 수정하고 다시 도전해 보세요."
        } else {
            "실패한 습관의 정보를 수정하고 다시 도전해 볼까요?"
        }
    }

    val habitDescription = if (screenState == HabitDetailScreenState.RETRY) {
        "재도전할 습관 제목은 수정할 수 없습니다."
    } else {
        null
    }

    val btnText = when (screenState) {
        HabitDetailScreenState.IDLE -> ""
        HabitDetailScreenState.CREATE -> "가족에게 습관 알리기"
        HabitDetailScreenState.EDIT -> "수정하기"
        HabitDetailScreenState.RETRY -> "재도전하기"
    }
}

sealed interface HabitDetailSideEffect : UiEffect {
    data class PopBackStack(val resultMessage: String? = null) : HabitDetailSideEffect

    data class ShowSnackbar(val message: String) : HabitDetailSideEffect
}
