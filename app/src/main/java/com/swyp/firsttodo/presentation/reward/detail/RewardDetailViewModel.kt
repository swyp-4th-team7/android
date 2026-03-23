package com.swyp.firsttodo.presentation.reward.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.presentation.reward.navigation.RewardRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RewardDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
    ) : BaseViewModel<RewardDetailUiState, RewardDetailSideEffect>(RewardDetailUiState()) {
        init {
            val screenType = savedStateHandle.toRoute<RewardRoute.RewardDetail>().screenType
            updateState { copy(screenType = screenType) }
        }

        fun onPopBackStack() {
            sendEffect(RewardDetailSideEffect.PopBackStack)
        }

        fun onBtnClick() {
            if (uiState.value.btnState is Async.Loading) return

            when (uiState.value.screenType) {
                RewardDetailScreenType.ACCEPT -> acceptReward()
                RewardDetailScreenType.DELIVER -> deliverReward()
                null -> Unit
            }

            sendEffect(RewardDetailSideEffect.PopBackStack)
        }

        private fun acceptReward() {}

        private fun deliverReward() {}
    }
