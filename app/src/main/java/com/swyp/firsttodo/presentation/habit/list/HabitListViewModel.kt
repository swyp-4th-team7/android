package com.swyp.firsttodo.presentation.habit.list

import androidx.lifecycle.viewModelScope
import com.swyp.firsttodo.core.auth.manager.SessionManager
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.model.habit.HabitModel
import com.swyp.firsttodo.domain.repository.HabitRepository
import com.swyp.firsttodo.domain.throwable.HabitError
import com.swyp.firsttodo.presentation.common.extension.snackbarMsg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitListViewModel
    @Inject
    constructor(
        sessionManager: SessionManager,
        private val habitRepository: HabitRepository,
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
            updateState { copy(habits = Async.Loading((this.habits as? Async.Success)?.data)) }

            viewModelScope.launch {
                habitRepository.getHabits()
                    .onSuccess {
                        updateState { copy(habits = if (it.isEmpty()) Async.Empty else Async.Success(it)) }
                    }
                    .onFailure {
                        val prevData = uiState.value.habits.getDataOrNull()
                        if (prevData != null) {
                            updateState { copy(habits = Async.Success(prevData)) }
                        }

                        if (it is ApiError) sendEffect(HabitListSideEffect.ShowSnackbar(it.snackbarMsg()))
                    }
            }
        }

        fun getFailedHabits() {
            viewModelScope.launch {
                habitRepository.getFailedHabits()
                    .onSuccess {
                        updateState { copy(failedHabits = if (it.isEmpty()) Async.Empty else Async.Success(it)) }
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
            val habitId = uiState.value.delRequestedId ?: return

            updateState { copy(deleteState = Async.Loading()) }

            viewModelScope.launch {
                habitRepository.deleteHabit(habitId)
                    .onSuccess {
                        val message = if (uiState.value.isFailedHabitDelete) "실패한 습관이 삭제되었습니다." else "습관이 삭제되었습니다."
                        updateState {
                            copy(
                                delRequestedId = null,
                                deleteState = Async.Success(Unit),
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
