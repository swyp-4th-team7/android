package com.swyp.firsttodo.presentation.reward.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.presentation.common.component.HaebomHeaderTab
import com.swyp.firsttodo.presentation.common.component.TopBarArea
import com.swyp.firsttodo.presentation.reward.component.ChildRewardList
import com.swyp.firsttodo.presentation.reward.component.ChildRewardUiModel
import com.swyp.firsttodo.presentation.reward.component.CompletedStickerHeader
import com.swyp.firsttodo.presentation.reward.component.ParentRewardManageList
import com.swyp.firsttodo.presentation.reward.component.ParentRewardUiModel
import com.swyp.firsttodo.presentation.reward.component.ParentStickerList
import com.swyp.firsttodo.presentation.reward.component.ParentStickerUiModel
import com.swyp.firsttodo.presentation.reward.component.RewardFilter
import com.swyp.firsttodo.presentation.reward.component.RewardFilterType
import com.swyp.firsttodo.presentation.reward.component.RewardHeader
import com.swyp.firsttodo.presentation.reward.component.StickerBoard
import com.swyp.firsttodo.presentation.reward.model.RewardState

@Composable
fun RewardListRoute(
    modifier: Modifier = Modifier,
    viewModel: RewardListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RewardListScreen(
        uiState = uiState,
        onTabClick = viewModel::onTabClick,
        onFilterTypeClick = viewModel::onFilterTypeClick,
        onHabitCreateBtnClick = viewModel::onCreateHabitBtnClick,
        onRewardLabelClick = viewModel::onRewardLabelClick,
        modifier = modifier,
    )
}

@Composable
fun RewardListScreen(
    uiState: RewardListUiState,
    onTabClick: (RewardHeaderTabType) -> Unit,
    onFilterTypeClick: (RewardFilterType) -> Unit,
    onHabitCreateBtnClick: () -> Unit,
    onRewardLabelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .background(HaebomTheme.colors.white),
    ) {
        TopBarArea()

        HaebomHeaderTab(
            currentTab = uiState.currentTab,
            tabs = uiState.tabs,
            onTabClick = onTabClick,
            modifier = Modifier,
        )

        RewardHeader(
            title = uiState.title,
            description = uiState.description,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp),
        )

        when (uiState.currentTab) {
            RewardHeaderTabType.STICKER -> {
                when (uiState.role) {
                    Role.PARENT -> ParentStickerList(
                        stickers = uiState.parentStickers,
                        onLabelClick = onRewardLabelClick,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 40.dp),
                    )

                    Role.CHILD -> Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        CompletedStickerHeader(
                            completedSticker = uiState.childCompletedSticker,
                        )

                        StickerBoard(
                            completedSticker = uiState.childCompletedSticker,
                        )
                    }

                    null -> Unit
                }
            }

            RewardHeaderTabType.REWARD -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    RewardFilter(
                        allFilters = uiState.filters,
                        selectedFilterType = uiState.selectedFilter,
                        onFilterClick = onFilterTypeClick,
                        modifier = Modifier.align(Alignment.End),
                    )

                    when (uiState.role) {
                        Role.PARENT -> ParentRewardManageList(
                            rewards = uiState.parentRewards,
                            onLabelClick = onRewardLabelClick,
                        )

                        Role.CHILD -> ChildRewardList(
                            rewards = uiState.childRewards,
                            onCreateHabitBtnClick = onHabitCreateBtnClick,
                        )

                        null -> Unit
                    }
                }
            }
        }

        Spacer(Modifier.height(40.dp))
    }
}

// region Preview

private val sampleParentStickers = listOf(
    ParentStickerUiModel(id = 1L, title = "수학 공부하기", boardCount = 2, startDate = "2026.03.17 (화)", stickerCount = 15),
    ParentStickerUiModel(id = 2L, title = "영어 단어 외우기", boardCount = 1, startDate = "2026.03.17 (화)", stickerCount = 3),
)

private val sampleParentRewards = listOf(
    ParentRewardUiModel(
        id = 1L,
        title = "수학 공부하기",
        habit = "매일 수학 문제 풀기",
        reward = "치킨 사주기",
        rewardState = RewardState.ING,
        habitIconRes = R.drawable.ic_habit_day_7,
    ),
    ParentRewardUiModel(
        id = 2L,
        title = "영어 단어 외우기",
        habit = "매일 영어 단어 20개",
        reward = "게임 1시간",
        rewardState = RewardState.CONFIRMING,
        habitIconRes = R.drawable.ic_habit_day_3,
    ),
    ParentRewardUiModel(
        id = 3L,
        title = "독서하기",
        habit = "하루 30분 독서",
        reward = "문화상품권",
        rewardState = RewardState.WAITING,
        habitIconRes = R.drawable.ic_habit_day_7,
    ),
    ParentRewardUiModel(
        id = 4L,
        title = "운동하기",
        habit = "줄넘기 100개",
        reward = "아이스크림",
        rewardState = RewardState.DONE,
        habitIconRes = R.drawable.ic_habit_day_7,
    ),
)

private val sampleChildRewards = listOf(
    ChildRewardUiModel(
        rewardId = 1L,
        title = "하루 10분 명상하기",
        reward = "아이스크림 사주기",
        defaultIconRes = R.drawable.ic_habit_day_7,
        completedIconRes = R.drawable.ic_habit_day_7_completed,
        rewardState = RewardState.ING,
    ),
    ChildRewardUiModel(
        rewardId = 2L,
        title = "매일 아침 스트레칭",
        reward = "영화 보러 가기",
        defaultIconRes = R.drawable.ic_habit_day_3,
        completedIconRes = R.drawable.ic_habit_day_3_completed,
        rewardState = RewardState.WAITING,
    ),
    ChildRewardUiModel(
        rewardId = 3L,
        title = "취침 전 일기 쓰기",
        reward = "좋아하는 카페 가기",
        defaultIconRes = R.drawable.ic_habit_day_7,
        completedIconRes = R.drawable.ic_habit_day_7_completed,
        rewardState = RewardState.DONE,
    ),
)

private class RewardListScreenPreviewProvider : PreviewParameterProvider<RewardListUiState> {
    override val values: Sequence<RewardListUiState> = sequenceOf(
        // Child - 스티커 탭 (Success)
        RewardListUiState(
            role = Role.CHILD,
            currentTab = RewardHeaderTabType.STICKER,
            childCompletedSticker = Async.Success(10),
        ),
        // Child - 스티커 탭 (Empty)
        RewardListUiState(
            role = Role.CHILD,
            currentTab = RewardHeaderTabType.STICKER,
            childCompletedSticker = Async.Empty,
        ),
        // Child - 보상 탭 (Success)
        RewardListUiState(
            role = Role.CHILD,
            currentTab = RewardHeaderTabType.REWARD,
            childRewards = Async.Success(sampleChildRewards),
        ),
        // Child - 보상 탭 (Empty)
        RewardListUiState(
            role = Role.CHILD,
            currentTab = RewardHeaderTabType.REWARD,
            childRewards = Async.Empty,
        ),
        // Parent - 스티커 탭 (Success)
        RewardListUiState(
            role = Role.PARENT,
            currentTab = RewardHeaderTabType.STICKER,
            parentStickers = Async.Success(sampleParentStickers),
        ),
        // Parent - 스티커 탭 (Empty)
        RewardListUiState(
            role = Role.PARENT,
            currentTab = RewardHeaderTabType.STICKER,
            parentStickers = Async.Empty,
        ),
        // Parent - 보상 탭 (Success)
        RewardListUiState(
            role = Role.PARENT,
            currentTab = RewardHeaderTabType.REWARD,
            parentRewards = Async.Success(sampleParentRewards),
        ),
        // Parent - 보상 탭 (Empty)
        RewardListUiState(
            role = Role.PARENT,
            currentTab = RewardHeaderTabType.REWARD,
            parentRewards = Async.Empty,
        ),
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun RewardListScreenPreview(
    @PreviewParameter(RewardListScreenPreviewProvider::class) uiState: RewardListUiState,
) {
    HaebomTheme {
        RewardListScreen(
            uiState = uiState,
            onTabClick = {},
            onFilterTypeClick = {},
            onHabitCreateBtnClick = {},
            onRewardLabelClick = {},
        )
    }
}

// endregion
