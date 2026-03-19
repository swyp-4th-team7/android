package com.swyp.firsttodo.presentation.reward.detail

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import kotlinx.serialization.Serializable

@Serializable
enum class RewardDetailScreenType {
    ACCEPT,
    DELIVER,
}

@Immutable
data class RewardDetailUiState(
    val screenType: RewardDetailScreenType? = null,
    val habit: String = "",
    val duration: HabitDuration? = null,
    val reward: String = "",
    val btnState: Async<Unit> = Async.Init,
) : UiState {
    val title = when (screenType) {
        RewardDetailScreenType.ACCEPT -> "보상 확인하기"
        RewardDetailScreenType.DELIVER -> "보상 전달하기"
        null -> ""
    }

    val description = when (screenType) {
        RewardDetailScreenType.ACCEPT -> "자녀 습관 보상을 확인하고 조율해보세요."
        RewardDetailScreenType.DELIVER -> "습관을 완료한 자녀에게 약속된 보상을 전달하셨나요?"
        null -> ""
    }

    val btnText = when (screenType) {
        RewardDetailScreenType.ACCEPT -> "보상 수락하기"
        RewardDetailScreenType.DELIVER -> "전달 완료"
        null -> ""
    }
}

sealed interface RewardDetailSideEffect : UiEffect {
    data object PopBackStack : RewardDetailSideEffect

    data class ShowSnackbar(val message: String) : RewardDetailSideEffect
}
