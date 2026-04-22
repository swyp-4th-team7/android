package com.swyp.firsttodo.presentation.reward.list

import androidx.lifecycle.viewModelScope
import com.swyp.firsttodo.core.auth.manager.SessionManager
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.model.reward.RewardStatus
import com.swyp.firsttodo.domain.repository.RewardRepository
import com.swyp.firsttodo.domain.repository.StickerRepository
import com.swyp.firsttodo.presentation.common.extension.snackbarMsg
import com.swyp.firsttodo.presentation.reward.component.ChildRewardUiModel
import com.swyp.firsttodo.presentation.reward.component.ParentRewardUiModel
import com.swyp.firsttodo.presentation.reward.component.RewardFilterType
import com.swyp.firsttodo.presentation.reward.detail.RewardDetailScreenType
import com.swyp.firsttodo.presentation.reward.extension.durationIconRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardListViewModel
    @Inject
    constructor(
        sessionManager: SessionManager,
        private val stickerRepository: StickerRepository,
        private val rewardRepository: RewardRepository,
    ) : BaseViewModel<RewardListUiState, RewardListSideEffect>(RewardListUiState()) {
        init {
            val role: Role = when (sessionManager.sessionState.value.userType) {
                Role.PARENT.request -> Role.PARENT
                else -> Role.CHILD
            }

            updateState { copy(role = role) }
        }

        private fun initStickerTab() {
            when (currentState.role) {
                Role.PARENT -> initParentStickerTab()
                Role.CHILD -> initChildStickerTab()
                null -> Unit
            }
        }

        private fun initRewardTab() {
            when (currentState.role) {
                Role.PARENT -> loadParentRewardTab(currentState.selectedFilter)
                Role.CHILD -> loadChildRewardTab(currentState.selectedFilter)
                null -> Unit
            }
        }

        private fun initParentStickerTab() {
            updateState { copy(parentStickers = Async.Loading(this.parentStickers.getDataOrNull())) }
            viewModelScope.launch {
                stickerRepository.getChildrenStickerList()
                    .onSuccess {
                        updateState { copy(parentStickers = if (it.isEmpty()) Async.Empty else Async.Success(it)) }
                    }.onFailure { throwable ->
                        currentState.parentStickers.getDataOrNull()?.let { prevData ->
                            updateState { copy(parentStickers = Async.Success(prevData)) }
                        } ?: updateState { copy(parentStickers = Async.Init) }

                        if (throwable is ApiError) {
                            sendEffect(RewardListSideEffect.ShowSnackbar(throwable.snackbarMsg()))
                        }
                    }
            }
        }

        private fun initChildStickerTab() {
            updateState { copy(childCompletedSticker = Async.Loading(this.childCompletedSticker.getDataOrNull())) }
            viewModelScope.launch {
                stickerRepository.getStickerBoard()
                    .onSuccess {
                        val stickerCount = it.filledSlotCount
                        updateState {
                            copy(
                                childCompletedSticker = if (stickerCount == 0) {
                                    Async.Empty
                                } else {
                                    Async.Success(
                                        stickerCount,
                                    )
                                },
                                showStickerCompleteDialog = it.showCompletionPopup,
                            )
                        }
                    }
                    .onFailure {
                        currentState.childCompletedSticker.getDataOrNull()?.let { prevData ->
                            updateState { copy(childCompletedSticker = Async.Success(prevData)) }
                        } ?: updateState { copy(childCompletedSticker = Async.Init) }

                        if (it is ApiError) sendEffect(RewardListSideEffect.ShowSnackbar(it.snackbarMsg()))
                    }
            }
        }

        private fun loadParentRewardTab(filter: RewardFilterType) {
            viewModelScope.launch {
                rewardRepository.getRewards(filter.request)
                    .onSuccess {
                        val newData = it.map { model ->
                            ParentRewardUiModel(
                                id = model.habitId,
                                title = model.nickname ?: "",
                                habit = model.title,
                                duration = model.duration,
                                reward = model.reward,
                                rewardState = model.status,
                                habitIconRes = model.durationIconRes,
                            )
                        }
                        updateState {
                            copy(
                                parentRewards = if (newData.isEmpty()) Async.Empty else Async.Success(newData),
                            )
                        }
                    }
                    .onFailure { throwable ->
                        if (throwable is ApiError) {
                            sendEffect(RewardListSideEffect.ShowSnackbar(throwable.snackbarMsg()))
                        }
                    }
            }
        }

        private fun loadChildRewardTab(filter: RewardFilterType) {
            viewModelScope.launch {
                rewardRepository.getRewards(filter.request)
                    .onSuccess {
                        val newData = it.map { model ->
                            ChildRewardUiModel(
                                rewardId = model.habitId,
                                title = model.title,
                                reward = model.reward,
                                iconRes = model.durationIconRes,
                                rewardState = model.status,
                            )
                        }
                        updateState {
                            copy(
                                childRewards = if (newData.isEmpty()) Async.Empty else Async.Success(newData),
                            )
                        }
                    }
                    .onFailure { throwable ->
                        if (throwable is ApiError) {
                            sendEffect(RewardListSideEffect.ShowSnackbar(throwable.snackbarMsg()))
                        }
                    }
            }
        }

        fun onTabClick(tab: RewardHeaderTabType) {
            val currentTab = currentState.currentTab
            updateState { copy(currentTab = tab) }

            if (currentTab != tab) {
                when (tab) {
                    RewardHeaderTabType.STICKER -> initStickerTab()
                    RewardHeaderTabType.REWARD -> initRewardTab()
                }
            }
        }

        fun onFilterTypeClick(filterType: RewardFilterType) {
            when (currentState.role) {
                Role.PARENT -> if (filterType is ParentRewardFilterType) {
                    updateState { copy(parentSelectedFilterType = filterType) }
                    loadParentRewardTab(filterType)
                }

                Role.CHILD -> if (filterType is ChildRewardFilterType) {
                    updateState { copy(childSelectedFilterType = filterType) }
                    loadChildRewardTab(filterType)
                }

                null -> Unit
            }
        }

        fun onBoardCompleteBtnClick() {
            viewModelScope.launch {
                stickerRepository.stickerPopupConfirm()
                    .onSuccess {
                        updateState { copy(showStickerCompleteDialog = false) }
                    }
                    .onFailure {
                        if (it is ApiError) sendEffect(RewardListSideEffect.ShowSnackbar(it.snackbarMsg()))
                    }
            }
        }

        fun onCreateHabitBtnClick() {
            sendThrottledEffect(RewardListSideEffect.NavigateToHabit)
        }

        fun refresh() {
            when (currentState.currentTab) {
                RewardHeaderTabType.STICKER -> initStickerTab()
                RewardHeaderTabType.REWARD -> initRewardTab()
            }
        }

        fun onDetailResult(message: String?) {
            message ?: return
            sendEffect(RewardListSideEffect.ShowSnackbar(message))
            initRewardTab()
        }

        fun onRewardLabelClick(reward: ParentRewardUiModel) {
            val screenType = when (reward.rewardState) {
                RewardStatus.REWARD_CHECKING -> RewardDetailScreenType.ACCEPT
                RewardStatus.REWARD_WAITING -> RewardDetailScreenType.DELIVER
                else -> return
            }
            sendEffect(
                RewardListSideEffect.NavigateToRewardDetail(
                    screenType = screenType,
                    habitId = reward.id,
                    habit = reward.habit,
                    duration = reward.duration.name,
                    reward = reward.reward,
                ),
            )
        }
    }
