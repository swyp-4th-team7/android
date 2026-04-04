package com.swyp.firsttodo.presentation.reward.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.domain.repository.RewardRepository
import com.swyp.firsttodo.domain.throwable.RewardError
import com.swyp.firsttodo.presentation.common.extension.snackbarMsg
import com.swyp.firsttodo.presentation.reward.navigation.RewardRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val rewardRepository: RewardRepository,
    ) : BaseViewModel<RewardDetailUiState, RewardDetailSideEffect>(RewardDetailUiState()) {
        val rewardFieldState = TextFieldState()

        init {
            val route = savedStateHandle.toRoute<RewardRoute.RewardDetail>()
            rewardFieldState.edit { append(route.reward) }
            updateState {
                copy(
                    screenType = route.screenType,
                    habitId = route.habitId,
                    habit = route.habit,
                    duration = runCatching { HabitDuration.valueOf(route.duration) }.getOrNull(),
                    initialReward = route.reward,
                    rewardText = route.reward,
                )
            }

            snapshotFlow { rewardFieldState.text.toString() }
                .onEach { updateState { copy(rewardText = it) } }
                .launchIn(viewModelScope)
        }

        fun onPopBackStack() {
            sendEffect(RewardDetailSideEffect.PopBackStack())
        }

        fun onBtnClick() {
            if (uiState.value.btnState is Async.Loading) return

            when (uiState.value.screenType) {
                RewardDetailScreenType.ACCEPT -> acceptReward()
                RewardDetailScreenType.DELIVER -> deliverReward()
                null -> Unit
            }
        }

        private fun acceptReward() {
            val habitId = uiState.value.habitId ?: return
            val inputReward = uiState.value.rewardText

            updateState { copy(btnState = Async.Loading()) }

            viewModelScope.launch {
                rewardRepository.startReward(habitId, inputReward)
                    .onSuccess {
                        sendEffect(RewardDetailSideEffect.PopBackStack("보상 수락이 완료되었습니다."))
                    }.onFailure {
                        updateState { copy(btnState = Async.Init) }
                        if (it is RewardError.RewardValueEmpty) {
                            sendEffect(RewardDetailSideEffect.ShowSnackbar("보상을 필수로 입력해주세요."))
                            return@launch
                        }

                        if (it is ApiError) {
                            sendEffect(RewardDetailSideEffect.ShowSnackbar(it.snackbarMsg()))
                            return@launch
                        }

                        val popBackMessage = when (it) {
                            is RewardError.InvalidStatus -> "이미 완료된 요청입니다."
                            is RewardError.RewardNotFound -> "존재하지 않는 보상입니다."
                            is RewardError.AccessDenied -> "접근할 수 없는 페이지입니다."
                            else -> return@launch
                        }
                        sendEffect(RewardDetailSideEffect.PopBackStack(popBackMessage))
                    }
            }
        }

        private fun deliverReward() {
            val habitId = uiState.value.habitId ?: return

            updateState { copy(btnState = Async.Loading()) }

            viewModelScope.launch {
                rewardRepository.completeReward(habitId)
                    .onSuccess {
                        sendEffect(RewardDetailSideEffect.PopBackStack("보상 전달이 완료되었습니다."))
                    }
                    .onFailure {
                        updateState { copy(btnState = Async.Init) }
                        if (it is ApiError) {
                            sendEffect(RewardDetailSideEffect.ShowSnackbar(it.snackbarMsg()))
                            return@launch
                        }

                        val popBackMessage = when (it) {
                            is RewardError.InvalidStatus -> "이미 완료된 요청입니다."
                            is RewardError.RewardNotFound -> "존재하지 않는 보상입니다."
                            is RewardError.AccessDenied -> "접근할 수 없는 페이지입니다."
                            else -> return@launch
                        }
                        sendEffect(RewardDetailSideEffect.PopBackStack(popBackMessage))
                    }
            }
        }
    }
