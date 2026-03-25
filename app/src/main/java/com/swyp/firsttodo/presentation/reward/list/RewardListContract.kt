package com.swyp.firsttodo.presentation.reward.list

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.model.sticker.ChildStickerModel
import com.swyp.firsttodo.presentation.common.component.HaebomHeaderTabType
import com.swyp.firsttodo.presentation.reward.component.ChildRewardUiModel
import com.swyp.firsttodo.presentation.reward.component.ParentRewardUiModel
import com.swyp.firsttodo.presentation.reward.component.RewardFilterType
import com.swyp.firsttodo.presentation.reward.detail.RewardDetailScreenType

enum class RewardHeaderTabType(
    override val index: Int,
    override val label: String,
) : HaebomHeaderTabType {
    STICKER(0, "할 일 스티커"),
    REWARD(1, "보상 관리"),
}

enum class ChildRewardFilterType(
    override val displayName: String,
    override val request: String,
) : RewardFilterType {
    ALL(
        displayName = "전체",
        request = "ALL",
    ),
    REWARD_WAITING(
        displayName = "보상 대기중",
        request = "REWARD_WAITING",
    ),
    IN_PROGRESS(
        displayName = "진행중",
        request = "IN_PROGRESS",
    ),
    COMPLETE(
        displayName = "완료",
        request = "COMPLETE",
    ),
}

enum class ParentRewardFilterType(
    override val displayName: String,
    override val request: String,
) : RewardFilterType {
    ALL(
        displayName = "전체",
        request = "ALL",
    ),
    REWARD_CHECKING(
        displayName = "보상 확인중",
        request = "REWARD_CHECKING",
    ),
    REWARD_WAITING(
        displayName = "보상 대기중",
        request = "REWARD_WAITING",
    ),
    IN_PROGRESS(
        displayName = "진행중",
        request = "IN_PROGRESS",
    ),
    COMPLETE(
        displayName = "완료",
        request = "COMPLETE",
    ),
}

@Immutable
data class RewardListUiState(
    // common
    val role: Role? = null,
    val currentTab: RewardHeaderTabType = RewardHeaderTabType.STICKER,
    // parent
    val parentSelectedFilterType: ParentRewardFilterType = ParentRewardFilterType.ALL,
    val parentStickers: Async<List<ChildStickerModel>> = Async.Init,
    val parentRewards: Async<List<ParentRewardUiModel>> = Async.Init,
    // child
    val childCompletedSticker: Async<Int> = Async.Init,
    val showStickerCompleteDialog: Boolean = false,
    val childSelectedFilterType: ChildRewardFilterType = ChildRewardFilterType.ALL,
    val childRewards: Async<List<ChildRewardUiModel>> = Async.Init,
) : UiState {
    val tabs = RewardHeaderTabType.entries

    val title = when (currentTab) {
        RewardHeaderTabType.STICKER -> when (role) {
            Role.PARENT -> "자녀 할 일 스티커 현황"
            Role.CHILD -> "나의 할 일 스티커"
            null -> ""
        }

        RewardHeaderTabType.REWARD -> "보상 모음집"
    }

    val description = when (currentTab) {
        RewardHeaderTabType.STICKER -> when (role) {
            Role.PARENT -> "자녀 할 일 완료율을 한 눈에 확인해 보세요."
            Role.CHILD -> "할 일 스티커로 한 눈에 확인해 보세요."
            null -> ""
        }

        RewardHeaderTabType.REWARD -> "가족과 함께 정한 보상들을 확인해보세요."
    }

    val filters: List<RewardFilterType> = when (role) {
        Role.PARENT -> ParentRewardFilterType.entries
        else -> ChildRewardFilterType.entries
    }

    val selectedFilter: RewardFilterType = when (role) {
        Role.PARENT -> parentSelectedFilterType
        else -> childSelectedFilterType
    }
}

sealed interface RewardListSideEffect : UiEffect {
    data object NavigateToHabit : RewardListSideEffect

    data class NavigateToRewardDetail(
        val screenType: RewardDetailScreenType,
        val habitId: Long,
        val habit: String,
        val duration: String,
        val reward: String,
    ) : RewardListSideEffect

    data class ShowSnackbar(val message: String) : RewardListSideEffect
}
