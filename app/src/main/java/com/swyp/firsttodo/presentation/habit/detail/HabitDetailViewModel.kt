package com.swyp.firsttodo.presentation.habit.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.swyp.firsttodo.core.auth.manager.SessionManager
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.domain.repository.HabitRepository
import com.swyp.firsttodo.domain.throwable.HabitError
import com.swyp.firsttodo.presentation.common.extension.snackbarMsg
import com.swyp.firsttodo.presentation.habit.navigation.HabitNavArgs
import com.swyp.firsttodo.presentation.habit.navigation.HabitNavArgsNavType
import com.swyp.firsttodo.presentation.habit.navigation.HabitRoute
import com.swyp.firsttodo.presentation.habit.navigation.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class HabitDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        sessionManager: SessionManager,
        private val habitRepository: HabitRepository,
    ) : BaseViewModel<HabitDetailUiState, HabitDetailSideEffect>(HabitDetailUiState()) {
        private val role: Role = when (sessionManager.sessionState.value.userType) {
            Role.PARENT.request -> Role.PARENT
            else -> Role.CHILD
        }

        val screenType = when (role) {
            Role.PARENT -> HabitDetailScreenType.PARENT
            Role.CHILD -> HabitDetailScreenType.CHILD
        }

        private val navArgs = savedStateHandle
            .toRoute<HabitRoute.HabitDetail>(typeMap = mapOf(typeOf<HabitNavArgs?>() to HabitNavArgsNavType))

        private val initialHabit = navArgs.habitNavArgs?.toModel()
        private val isRetry = navArgs.isRetry

        val titleState = TextFieldState(initialText = initialHabit?.title ?: "")
        val rewardState = TextFieldState(initialText = initialHabit?.reward ?: "")

        init {
            val state = when {
                isRetry -> HabitDetailScreenState.RETRY
                initialHabit == null -> HabitDetailScreenState.CREATE
                else -> HabitDetailScreenState.EDIT
            }

            updateState {
                copy(
                    duration = initialHabit?.duration,
                    habitId = initialHabit?.habitId,
                    isCompleted = initialHabit?.isCompleted,
                    screenState = state,
                )
            }
        }

        val isBtnEnabled: StateFlow<Boolean> = combine(
            snapshotFlow { titleState.text.toString() },
            snapshotFlow { rewardState.text.toString() },
            uiState,
        ) { _, _, state ->
            when (state.screenState) {
                HabitDetailScreenState.IDLE -> false
                HabitDetailScreenState.CREATE -> validateAllField()
                HabitDetailScreenState.EDIT -> isChanged() && validateAllField()
                HabitDetailScreenState.RETRY -> true
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

        fun onBackClick() {
            sendEffect(HabitDetailSideEffect.PopBackStack())
        }

        fun onDurationClick(duration: HabitDuration) {
            updateState { copy(duration = duration) }
        }

        private fun validateTitle(): Boolean {
            val title = titleState.text.toString()
            return title.isNotBlank()
        }

        private fun validateReward(): Boolean {
            val reward = rewardState.text.toString()
            return reward.isNotBlank()
        }

        private fun validateDuration(): Boolean = uiState.value.duration != null

        private fun validateAllField(): Boolean {
            return when (screenType) {
                HabitDetailScreenType.CHILD -> validateTitle() && validateReward() && validateDuration()
                HabitDetailScreenType.PARENT -> validateTitle() && validateDuration()
                HabitDetailScreenType.IDLE -> false
            }
        }

        private fun isChanged(): Boolean {
            val titleChanged = titleState.text.toString() != initialHabit?.title
            val durationChanged = uiState.value.duration != initialHabit?.duration
            val rewardChanged = when (screenType) {
                HabitDetailScreenType.CHILD -> rewardState.text.toString() != initialHabit?.reward
                HabitDetailScreenType.PARENT -> false
                HabitDetailScreenType.IDLE -> false
            }
            return titleChanged || durationChanged || rewardChanged
        }

        fun onBtnClick() {
            when (uiState.value.screenState) {
                HabitDetailScreenState.IDLE -> Unit

                HabitDetailScreenState.CREATE -> createHabit()

                HabitDetailScreenState.EDIT -> editHabit()
                HabitDetailScreenState.RETRY -> retryHabit()
            }
        }

        private fun createHabit() {
            if (uiState.value.loadingState is Async.Loading) return
            if (!validateAllField()) return

            val inputTitle = titleState.text.toString()
            val inputDuration = uiState.value.duration ?: return
            val inputReward = when (screenType) {
                HabitDetailScreenType.CHILD -> rewardState.text.toString()
                HabitDetailScreenType.PARENT -> null
                HabitDetailScreenType.IDLE -> null
            }

            updateState { copy(loadingState = Async.Loading()) }

            viewModelScope.launch {
                habitRepository.createHabit(
                    title = inputTitle,
                    duration = inputDuration,
                    reward = inputReward,
                ).onSuccess {
                    sendEffect(HabitDetailSideEffect.PopBackStack("습관이 추가되었습니다."))
                    updateState { copy(loadingState = Async.Success(Unit)) }
                }.onFailure { throwable ->
                    val message = when (throwable) {
                        is HabitError.TitleEmpty -> "습관 제목을 입력해주세요"
                        is HabitError.DurationEmpty -> "기간을 선택해주세요"
                        is ApiError -> throwable.snackbarMsg()
                        else -> return@launch
                    }
                    sendEffect(HabitDetailSideEffect.ShowSnackbar(message))
                    updateState { copy(loadingState = Async.Init) }
                }
            }
        }

        private fun editHabit() {
            if (uiState.value.loadingState is Async.Loading) return
            if (!validateAllField()) return

            val habitId = uiState.value.habitId ?: return
            val inputTitle = titleState.text.toString()
            val inputDuration = uiState.value.duration ?: return
            val inputReward = when (screenType) {
                HabitDetailScreenType.CHILD -> rewardState.text.toString()
                HabitDetailScreenType.PARENT -> null
                HabitDetailScreenType.IDLE -> null
            }
            val completed = uiState.value.isCompleted ?: return

            updateState { copy(loadingState = Async.Loading()) }

            viewModelScope.launch {
                habitRepository.editHabit(
                    habitId = habitId,
                    title = inputTitle,
                    duration = inputDuration,
                    reward = inputReward,
                    isCompleted = completed,
                )
                    .onSuccess {
                        sendEffect(HabitDetailSideEffect.PopBackStack("습관이 수정되었습니다."))
                        updateState { copy(loadingState = Async.Success(Unit)) }
                    }
                    .onFailure { throwable ->
                        when (throwable) {
                            is HabitError.HabitNotFound -> {
                                sendEffect(HabitDetailSideEffect.PopBackStack("이미 삭제된 습관입니다."))
                            }

                            is ApiError -> sendEffect(HabitDetailSideEffect.ShowSnackbar(throwable.snackbarMsg()))
                        }
                        updateState { copy(loadingState = Async.Init) }
                    }
            }
        }

        private fun retryHabit() {
            val habitId = uiState.value.habitId ?: return
            val inputDuration = uiState.value.duration ?: return
            val inputReward = rewardState.text.toString()

            viewModelScope.launch {
                habitRepository.retryFailedHabit(
                    habitId = habitId,
                    duration = inputDuration,
                    reward = inputReward,
                )
                    .onSuccess {
                        sendEffect(HabitDetailSideEffect.PopBackStack("멋져요! 습관 재도전을 시작했습니다."))
                        updateState { copy(loadingState = Async.Success(Unit)) }
                    }
                    .onFailure { throwable ->
                        when (throwable) {
                            is HabitError.HabitNotFound -> {
                                sendEffect(HabitDetailSideEffect.PopBackStack("이미 삭제된 습관입니다."))
                            }

                            is HabitError.RewardEmpty -> {
                                sendEffect(HabitDetailSideEffect.ShowSnackbar("보상을 입력해주세요."))
                            }

                            is ApiError -> sendEffect(HabitDetailSideEffect.ShowSnackbar(throwable.snackbarMsg()))
                        }
                        updateState { copy(loadingState = Async.Init) }
                    }
            }
        }
    }
