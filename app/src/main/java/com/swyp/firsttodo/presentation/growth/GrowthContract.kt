package com.swyp.firsttodo.presentation.growth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.model.growth.ChildGrowthModel
import com.swyp.firsttodo.domain.model.growth.GrowthModel
import com.swyp.firsttodo.presentation.common.component.HaebomHeaderTabType

enum class GrowthHeaderTabType(
    override val index: Int,
    override val label: String,
) : HaebomHeaderTabType {
    TODO(0, "할 일"),
    HABIT(1, "습관"),
}

enum class GrowthTextType {
    EMPTY,
    NAGGING,
    CHEER,
    PERFECT,
}

@Composable
internal fun GrowthTextType.color(): Color {
    return when (this) {
        GrowthTextType.EMPTY -> HaebomTheme.colors.gray200
        GrowthTextType.NAGGING -> HaebomTheme.colors.orange500
        GrowthTextType.CHEER -> HaebomTheme.colors.semanticGreen
        GrowthTextType.PERFECT -> HaebomTheme.colors.semanticBlue
    }
}

@Immutable
data class GrowthUiState(
    // common
    val role: Role? = null,
    val currentTab: GrowthHeaderTabType = GrowthHeaderTabType.TODO,
    // parent
    val childrenGrowth: Async<List<ChildGrowthModel>> = Async.Init,
    val selectedChildIdx: Int = 0,
    // child
    val todoGrowth: Async<GrowthModel> = Async.Init,
    val habitGrowth: Async<GrowthModel> = Async.Init,
) : UiState {
    val currentStarCount = when (currentTab) {
        GrowthHeaderTabType.TODO -> when (role) {
            Role.PARENT -> if (childrenGrowth is Async.Empty) {
                0
            } else {
                childrenGrowth.getDataOrNull()?.let { data ->
                    runCatching { data[selectedChildIdx].todoStarCount }.getOrNull()
                }
            }

            else -> todoGrowth.getDataOrNull()?.starCount
        }

        GrowthHeaderTabType.HABIT -> when (role) {
            Role.PARENT -> if (childrenGrowth is Async.Empty) {
                0
            } else {
                childrenGrowth.getDataOrNull()?.let { data ->
                    runCatching { data[selectedChildIdx].habitStarCount }.getOrNull()
                }
            }

            else -> habitGrowth.getDataOrNull()?.starCount
        }
    }

    val growthTextType = when (currentStarCount) {
        1 -> GrowthTextType.NAGGING
        2 -> GrowthTextType.CHEER
        3 -> GrowthTextType.PERFECT
        else -> GrowthTextType.EMPTY
    }

    val bubbleImageRes = when (currentStarCount) {
        0 -> R.drawable.img_growth_speech_bubble_monochrome
        1, 2, 3 -> R.drawable.img_growth_speech_bubble
        else -> null
    }

    val characterImageRes = when (currentStarCount) {
        0 -> R.drawable.img_growth_empty
        1 -> R.drawable.img_todo_nagging_176
        2 -> R.drawable.img_todo_cheer_176
        3 -> R.drawable.img_todo_perfect_176
        else -> null
    }

    val bubbleText = when (currentStarCount) {
        0 -> "아직 기록이 없어요."
        1 -> if (role == Role.PARENT) "아직 시작 단계예요!" else "조금만 더 힘내볼까요?"
        2 -> if (role == Role.PARENT) "꽤 잘하고 있어요!" else "지금 잘하고 있어요!"
        3 -> if (role == Role.PARENT) "정말 열심히 했어요!" else "너무 대단해요!"
        else -> ""
    }

    val selectedChild = childrenGrowth.getDataOrNull()?.let { data ->
        runCatching { data[selectedChildIdx] }.getOrNull()
    }

    val description = when (currentStarCount) {
        0 -> if (role == Role.PARENT) {
            when (currentTab) {
                GrowthHeaderTabType.TODO -> "이번 주에 자녀가 할 일을 실천하면\n매주 월요일에 성장을 확인할 수 있어요!"
                GrowthHeaderTabType.HABIT -> "이번 주에 자녀가 습관을 실천하면\n매주 월요일에 성장을 확인할 수 있어요!"
            }
        } else {
            when (currentTab) {
                GrowthHeaderTabType.TODO -> "이번 주에 할 일을 실천하면\n다음 주 월요일에 별을 받을 수 있어요."
                GrowthHeaderTabType.HABIT -> "이번 주에 습관을 실천하면\n다음 주 월요일에 별을 받을 수 있어요."
            }
        }

        1 -> if (role == Role.PARENT) {
            "지난 주 ${selectedChild?.nickname}(은)는 별 1개!\n오늘 한 가지만 같이 해보자고 격려해 주세요."
        } else {
            "이번 주는 별 1개!\n다음 주엔 더 해낼 수 있어요."
        }

        2 -> if (role == Role.PARENT) {
            "지난 주 ${selectedChild?.nickname}(은)는 별 2개!\n조금만 더 해볼 수 있도록 응원해 주세요."
        } else {
            "이번 주는 별 2개!\n우리 조금만 더 힘내봐요!"
        }

        3 -> if (role == Role.PARENT) {
            "지난 주 ${selectedChild?.nickname}(은)는 별 3개!\n노력한 우리 아이에게 꼭 칭찬해 주세요. \uD83C\uDF89"
        } else {
            "이번 주는 별 3개!\n다음 주도 기대할게요. \uD83C\uDF89"
        }

        else -> ""
    }

    val weekRange = when (role) {
        Role.PARENT -> selectedChild?.weekRange

        else -> when (currentTab) {
            GrowthHeaderTabType.TODO -> todoGrowth.getDataOrNull()?.weekRange
            GrowthHeaderTabType.HABIT -> habitGrowth.getDataOrNull()?.weekRange
        }
    }
}

sealed interface GrowthSideEffect : UiEffect {
    data class ShowSnackbar(val message: String) : GrowthSideEffect
}
