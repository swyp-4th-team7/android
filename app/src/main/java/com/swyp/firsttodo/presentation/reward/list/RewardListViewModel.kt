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
            viewModelScope.launch {
                stickerRepository.getChildrenStickerList()
                    .onSuccess {
                        updateState { copy(parentStickers = Async.Success(it)) }
                    }.onFailure { throwable ->
                        uiState.value.parentStickers.getDataOrNull()?.let { prevData ->
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
                        uiState.value.childCompletedSticker.getDataOrNull()?.let { prevData ->
                            updateState { copy(childCompletedSticker = Async.Success(prevData)) }
                        } ?: updateState { copy(childCompletedSticker = Async.Init) }

                        if (it is ApiError) sendEffect(RewardListSideEffect.ShowSnackbar(it.snackbarMsg()))
                    }
            }
        }

        private fun initParentRewardTab() {
            viewModelScope.launch {
                rewardRepository.getRewards(uiState.value.selectedFilter.request)
                    .onSuccess {
                        val newData = it.map { model ->
                            ParentRewardUiModel(
                                id = model.habitId,
                                title = model.nickname ?: "",
                                habit = model.title,
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

        private fun initChildRewardTab() {
            viewModelScope.launch {
                rewardRepository.getRewards(uiState.value.childSelectedFilterType.request)
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
            sendEffect(RewardListSideEffect.NavigateToHabit)
        }

        fun onRewardLabelClick(reward: ParentRewardUiModel) {
            when (reward.rewardState) {
                RewardStatus.REWARD_CHECKING -> sendEffect(
                    RewardListSideEffect.NavigateToRewardDetail(RewardDetailScreenType.ACCEPT),
                )

                RewardStatus.REWARD_WAITING -> sendEffect(
                    RewardListSideEffect.NavigateToRewardDetail(RewardDetailScreenType.DELIVER),
                )

                else -> Unit
            }
        }
    }
