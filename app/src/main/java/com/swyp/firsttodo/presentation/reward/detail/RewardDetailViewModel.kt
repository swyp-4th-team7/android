package com.swyp.firsttodo.presentation.reward.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.swyp.firsttodo.core.analytics.AnalyticsEvent
import com.swyp.firsttodo.core.analytics.AnalyticsManager
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.core.common.extension.snackbarMsg
import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.domain.error.RewardError
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.domain.repository.RewardRepository
import com.swyp.firsttodo.presentation.reward.navigation.RewardRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val rewardRepository: RewardRepository,
        private val analyticsManager: AnalyticsManager,
    ) : BaseViewModel<RewardDetailUiState, RewardDetailSideEffect>(RewardDetailUiState()) {
        val rewardFieldState = TextFieldState()

        val isBtnEnabled: StateFlow<Boolean> = combine(
            snapshotFlow { rewardFieldState.text.toString() },
            uiState,
        ) { text, state ->
            when (state.screenType) {
                RewardDetailScreenType.ACCEPT -> text.isNotBlank()
                RewardDetailScreenType.DELIVER -> true
                null -> false
            } && state.loadingState !is Async.Loading
        }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

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
                )
            }
        }

        fun onPopBackStack() {
            sendEffect(RewardDetailSideEffect.PopBackStack())
        }

        fun onBtnClick() {
            if (currentState.loadingState is Async.Loading) return

            when (currentState.screenType) {
                RewardDetailScreenType.ACCEPT -> acceptReward()
                RewardDetailScreenType.DELIVER -> deliverReward()
                null -> Unit
            }
        }

        private fun acceptReward() {
            val habitId = currentState.habitId ?: return
            val inputReward = rewardFieldState.text.toString()

            updateState { copy(loadingState = Async.Loading()) }

            viewModelScope.launch {
                rewardRepository.startReward(habitId, inputReward)
                    .onSuccess {
                        analyticsManager.track(
                            AnalyticsEvent.AcceptReward(
                                habitId = habitId,
                                title = currentState.habit,
                                duration = currentState.duration?.name ?: HabitDuration.SEVEN_DAYS.name,
                                reward = inputReward,
                            ),
                        )
                        sendEffect(RewardDetailSideEffect.PopBackStack("보상 수락이 완료되었습니다."))
                    }.onFailure {
                        updateState { copy(loadingState = Async.Init) }
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
            val habitId = currentState.habitId ?: return

            updateState { copy(loadingState = Async.Loading()) }

            viewModelScope.launch {
                rewardRepository.completeReward(habitId)
                    .onSuccess {
                        analyticsManager.track(
                            AnalyticsEvent.GiveReward(
                                habitId = habitId,
                                title = currentState.habit,
                                duration = currentState.duration?.name ?: HabitDuration.SEVEN_DAYS.name,
                                reward = rewardFieldState.text.toString(),
                            ),
                        )
                        sendEffect(RewardDetailSideEffect.PopBackStack("보상 전달이 완료되었습니다."))
                    }
                    .onFailure {
                        updateState { copy(loadingState = Async.Init) }
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
