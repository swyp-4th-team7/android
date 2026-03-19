package com.swyp.firsttodo.presentation.reward.list

import androidx.lifecycle.viewModelScope
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.auth.manager.SessionManager
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.presentation.reward.component.ChildRewardUiModel
import com.swyp.firsttodo.presentation.reward.component.ParentRewardUiModel
import com.swyp.firsttodo.presentation.reward.component.ParentStickerUiModel
import com.swyp.firsttodo.presentation.reward.component.RewardFilterType
import com.swyp.firsttodo.presentation.reward.model.RewardState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardListViewModel
    @Inject
    constructor(
        sessionManager: SessionManager,
    ) : BaseViewModel<RewardListUiState, RewardListSideEffect>(RewardListUiState()) {
        init {
            val role: Role = when (sessionManager.sessionState.value.userType) {
                Role.PARENT.request -> Role.PARENT
                else -> Role.CHILD
            }

            updateState { copy(role = role) }

            initStickerTab()
        }

        private fun initStickerTab() {
            when (uiState.value.role) {
                Role.PARENT -> initParentStickerTab()
                Role.CHILD -> initChildStickerTab()
                null -> Unit
            }
        }

        private fun initRewardTab() {
            when (uiState.value.role) {
                Role.PARENT -> initParentRewardTab()
                Role.CHILD -> initChildRewardTab()
                null -> Unit
            }
        }

        private fun initParentStickerTab() {
            updateState { copy(parentStickers = Async.Loading(this.parentStickers.getDataOrNull())) }
            viewModelScope.launch { delay(500) }
            val newStickers = listOf(
                ParentStickerUiModel(
                    id = 1L,
                    title = "수학 공부하기",
                    boardCount = 2,
                    startDate = "2026.03.17 (화)",
                    stickerCount = 15,
                ),
                ParentStickerUiModel(
                    id = 2L,
                    title = "영어 단어 외우기",
                    boardCount = 1,
                    startDate = "2026.03.17 (화)",
                    stickerCount = 3,
                ),
                ParentStickerUiModel(
                    id = 1L,
                    title = "수학 공부하기",
                    boardCount = 2,
                    startDate = "2026.03.17 (화)",
                    stickerCount = 15,
                ),
                ParentStickerUiModel(
                    id = 2L,
                    title = "영어 단어 외우기",
                    boardCount = 1,
                    startDate = "2026.03.17 (화)",
                    stickerCount = 3,
                ),
                ParentStickerUiModel(
                    id = 1L,
                    title = "수학 공부하기",
                    boardCount = 2,
                    startDate = "2026.03.17 (화)",
                    stickerCount = 15,
                ),
                ParentStickerUiModel(
                    id = 2L,
                    title = "영어 단어 외우기",
                    boardCount = 1,
                    startDate = "2026.03.17 (화)",
                    stickerCount = 3,
                ),
                ParentStickerUiModel(
                    id = 3L,
                    title = "수학 공부하기",
                    boardCount = 2,
                    startDate = "2026.03.17 (화)",
                    stickerCount = 15,
                ),
                ParentStickerUiModel(
                    id = 4L,
                    title = "영어 단어 외우기",
                    boardCount = 1,
                    startDate = "2026.03.17 (화)",
                    stickerCount = 3,
                ),
                ParentStickerUiModel(
                    id = 5L,
                    title = "수학 공부하기",
                    boardCount = 2,
                    startDate = "2026.03.17 (화)",
                    stickerCount = 15,
                ),
                ParentStickerUiModel(
                    id = 6L,
                    title = "영어 단어 외우기",
                    boardCount = 1,
                    startDate = "2026.03.17 (화)",
                    stickerCount = 3,
                ),
                ParentStickerUiModel(
                    id = 1L,
                    title = "수학 공부하기",
                    boardCount = 2,
                    startDate = "2026.03.17 (화)",
                    stickerCount = 15,
                ),
                ParentStickerUiModel(
                    id = 7L,
                    title = "영어 단어 외우기",
                    boardCount = 1,
                    startDate = "2026.03.17 (화)",
                    stickerCount = 3,
                ),
            )
            updateState { copy(parentStickers = Async.Success(newStickers)) }
        }

        private fun initChildStickerTab() {
            updateState { copy(childCompletedSticker = Async.Loading(this.childCompletedSticker.getDataOrNull())) }
            viewModelScope.launch { delay(500) }
            updateState { copy(childCompletedSticker = Async.Success(10), showStickerCompleteDialog = true) }
        }

        private fun initParentRewardTab() {
            updateState { copy(parentRewards = Async.Loading(this.parentRewards.getDataOrNull())) }
            viewModelScope.launch { delay(500) }
            val newRewards = listOf(
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
            updateState { copy(parentRewards = Async.Success(newRewards)) }
        }

        private fun initChildRewardTab() {
            updateState { copy(childRewards = Async.Loading(this.childRewards.getDataOrNull())) }
            viewModelScope.launch { delay(500) }
            val newRewards = listOf(
                ChildRewardUiModel(
                    rewardId = 1L,
                    title = "하루 10분 명상하기",
                    reward = "아이스크림 사주기",
                    defaultIconRes = R.drawable.ic_habit_day_7,
                    completedIconRes = R.drawable.ic_habit_day_7,
                    rewardState = RewardState.ING,
                ),
                ChildRewardUiModel(
                    rewardId = 2L,
                    title = "하루에 책 10장 읽기 하루에 책 10장 읽기 하루에 책 10장 읽기",
                    reward = "가족이랑 놀이공원 가기 가족이랑 놀이공원 가기 가족이랑 놀이공원",
                    defaultIconRes = R.drawable.ic_habit_day_7,
                    completedIconRes = R.drawable.ic_habit_day_7,
                    rewardState = RewardState.ING,
                ),
                ChildRewardUiModel(
                    rewardId = 3L,
                    title = "매일 아침 스트레칭",
                    reward = "영화 보러 가기",
                    defaultIconRes = R.drawable.ic_habit_day_7,
                    completedIconRes = R.drawable.ic_habit_day_7,
                    rewardState = RewardState.WAITING,
                ),
                ChildRewardUiModel(
                    rewardId = 4L,
                    title = "매일 아침 스트레칭 10분 매일 아침 스트레칭 10분 매일 아침",
                    reward = "가족과 외식하기 가족과 외식하기 가족과 외식하기 가족과 외식하기",
                    defaultIconRes = R.drawable.ic_habit_day_7,
                    completedIconRes = R.drawable.ic_habit_day_7,
                    rewardState = RewardState.WAITING,
                ),
                ChildRewardUiModel(
                    rewardId = 5L,
                    title = "취침 전 일기 쓰기",
                    reward = "좋아하는 카페 가기",
                    defaultIconRes = R.drawable.ic_habit_day_7,
                    completedIconRes = R.drawable.ic_habit_day_7,
                    rewardState = RewardState.DONE,
                ),
                ChildRewardUiModel(
                    rewardId = 6L,
                    title = "취침 전 일기 쓰기 취침 전 일기 쓰기 취침 전 일기 쓰기 취침 전",
                    reward = "좋아하는 카페 가기 좋아하는 카페 가기 좋아하는 카페 가기 좋아",
                    defaultIconRes = R.drawable.ic_habit_day_7,
                    completedIconRes = R.drawable.ic_habit_day_7,
                    rewardState = RewardState.DONE,
                ),
            )
            updateState { copy(childRewards = Async.Success(newRewards)) }
        }

        fun onTabClick(tab: RewardHeaderTabType) {
            val currentTab = uiState.value.currentTab
            updateState { copy(currentTab = tab) }

            if (currentTab != tab) {
                when (tab) {
                    RewardHeaderTabType.STICKER -> initStickerTab()
                    RewardHeaderTabType.REWARD -> initRewardTab()
                }
            }
        }

        fun onFilterTypeClick(filterType: RewardFilterType) {
            when (uiState.value.role) {
                Role.PARENT -> if (filterType is ParentRewardFilterType) {
                    updateState { copy(parentSelectedFilterType = filterType) }
                    initParentRewardTab()
                }

                Role.CHILD -> if (filterType is ChildRewardFilterType) {
                    updateState { copy(childSelectedFilterType = filterType) }
                    initChildRewardTab()
                }

                null -> Unit
            }
        }

        fun onBoardCompleteBtnClick() {
            initStickerTab()
            updateState { copy(showStickerCompleteDialog = false) }
        }

        fun onCreateHabitBtnClick() {
            sendEffect(RewardListSideEffect.NavigateToHabit)
        }

        fun onRewardLabelClick() {
        }
    }
