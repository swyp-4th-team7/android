package com.swyp.firsttodo.presentation.habit.list

import androidx.lifecycle.viewModelScope
import com.swyp.firsttodo.core.analytics.AnalyticsEvent
import com.swyp.firsttodo.core.analytics.AnalyticsManager
import com.swyp.firsttodo.core.auth.manager.SessionManager
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.core.common.extension.snackbarMsg
import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.domain.error.HabitError
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.model.habit.HabitModel
import com.swyp.firsttodo.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitListViewModel
    @Inject
    constructor(
        sessionManager: SessionManager,
        private val habitRepository: HabitRepository,
        private val analyticsManager: AnalyticsManager,
    ) : BaseViewModel<HabitListUiState, HabitListSideEffect>(HabitListUiState()) {
        init {
            val role: Role = when (sessionManager.sessionState.value.userType) {
                Role.PARENT.request -> Role.PARENT
                else -> Role.CHILD
            }

            updateState { copy(role = role) }

            getHabits()
            getFailedHabits()
        }

        fun getHabits() {
            viewModelScope.launch {
                habitRepository.getHabits()
                    .onSuccess {
                        updateState {
                            copy(
                                habits = if (it.isEmpty()) Async.Empty else Async.Success(it.toImmutableList()),
                            )
                        }
                    }
                    .onFailure {
                        if (it is ApiError) sendEffect(HabitListSideEffect.ShowSnackbar(it.snackbarMsg()))
                    }
            }
        }

        fun getFailedHabits() {
            viewModelScope.launch {
                habitRepository.getFailedHabits()
                    .onSuccess {
                        updateState {
                            copy(
                                failedHabits = if (it.isEmpty()) Async.Empty else Async.Success(it.toImmutableList()),
                            )
                        }
                    }
                    .onFailure {
                        if (it is ApiError) sendEffect(HabitListSideEffect.ShowSnackbar(it.snackbarMsg()))
                    }
            }
        }

        fun onDetailResult(message: String?) {
            message ?: return
            sendEffect(HabitListSideEffect.ShowSnackbar(message))
            getHabits()
            getFailedHabits()
        }

        fun onCreateClick() {
            sendThrottledEffect(HabitListSideEffect.NavigateToHabitDetail(null))
        }

        fun onCheckClick(habit: HabitModel) {
            viewModelScope.launch {
                habitRepository.editHabit(
                    habitId = habit.habitId,
                    title = habit.title,
                    duration = habit.duration,
                    reward = habit.reward,
                    isCompleted = !habit.isCompleted,
                ).onSuccess {
                    getHabits()
                }.onFailure {
                    if (it is HabitError.HabitNotFound) {
                        sendEffect(HabitListSideEffect.ShowSnackbar("이미 삭제된 습관입니다."))
                        getHabits()
                    }

                    if (it is ApiError) sendEffect(HabitListSideEffect.ShowSnackbar(it.snackbarMsg()))
                }
            }
        }

        fun onEditClick(habit: HabitModel) {
            sendThrottledEffect(HabitListSideEffect.NavigateToHabitDetail(habit))
        }

        fun onDeleteClick(habit: HabitModel) {
            updateState { copy(delRequestedId = habit.habitId, deleteState = Async.Init, isFailedHabitDelete = false) }
        }

        fun onFailedHabitDeleteClick(habit: HabitModel) {
            updateState { copy(delRequestedId = habit.habitId, deleteState = Async.Init, isFailedHabitDelete = true) }
        }

        fun onDeleteConfirm() {
            val habitId = currentState.delRequestedId ?: return

            updateState { copy(deleteState = Async.Loading()) }

            viewModelScope.launch {
                habitRepository.deleteHabit(habitId)
                    .onSuccess {
                        if (currentState.isFailedHabitDelete) {
                            analyticsManager.track(AnalyticsEvent.DeleteFailedHabit(habitId))
                        } else {
                            analyticsManager.track(AnalyticsEvent.DeleteHabit(habitId))
                        }

                        val message = if (currentState.isFailedHabitDelete) "실패한 습관이 삭제되었습니다." else "습관이 삭제되었습니다."
                        updateState {
                            copy(
                                delRequestedId = null,
                                deleteState = Async.Init,
                                isFailedHabitDelete = false,
                            )
                        }
                        sendEffect(HabitListSideEffect.ShowSnackbar(message))
                        getHabits()
                        getFailedHabits()
                    }.onFailure { throwable ->
                        when (throwable) {
                            is HabitError.HabitNotFound -> {
                                updateState { copy(delRequestedId = null, deleteState = Async.Init) }
                                sendEffect(HabitListSideEffect.ShowSnackbar("이미 삭제된 습관입니다."))
                                getHabits()
                            }

                            is ApiError -> sendEffect(HabitListSideEffect.ShowSnackbar(throwable.snackbarMsg()))
                        }
                    }
            }
        }

        fun onDeleteCancel() {
            updateState { copy(delRequestedId = null) }
        }

        fun onRetryClick(habit: HabitModel) {
            sendThrottledEffect(HabitListSideEffect.NavigateToHabitRetry(habit))
        }
    }
