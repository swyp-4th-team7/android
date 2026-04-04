package com.swyp.firsttodo.presentation.growth

import androidx.lifecycle.viewModelScope
import com.swyp.firsttodo.core.auth.manager.SessionManager
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.repository.GrowthRepository
import com.swyp.firsttodo.presentation.common.extension.snackbarMsg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GrowthViewModel
    @Inject
    constructor(
        sessionManager: SessionManager,
        private val growthRepository: GrowthRepository,
    ) : BaseViewModel<GrowthUiState, GrowthSideEffect>(GrowthUiState()) {
        init {
            val role: Role = when (sessionManager.sessionState.value.userType) {
                Role.PARENT.request -> Role.PARENT
                else -> Role.CHILD
            }

            updateState { copy(role = role) }

            if (role == Role.CHILD) {
                loadTodoGrowth()
                loadHabitGrowth()
            }
        }

        fun refresh() {
            if (uiState.value.role != Role.PARENT) return
            updateState { copy(selectedChildIdx = 0) }
            loadChildrenGrowth()
        }

        private fun loadChildrenGrowth() {
            viewModelScope.launch {
                growthRepository.getGrowthChildren()
                    .onSuccess {
                        updateState { copy(childrenGrowth = if (it.isEmpty()) Async.Empty else Async.Success(it)) }
                    }
                    .onFailure {
                        if (it is ApiError) sendEffect(GrowthSideEffect.ShowSnackbar(it.snackbarMsg()))
                    }
            }
        }

        private fun loadTodoGrowth() {
            viewModelScope.launch {
                growthRepository.getGrowthTodo()
                    .onSuccess {
                        updateState { copy(todoGrowth = Async.Success(it)) }
                    }
                    .onFailure {
                        if (it is ApiError) sendEffect(GrowthSideEffect.ShowSnackbar(it.snackbarMsg()))
                    }
            }
        }

        private fun loadHabitGrowth() {
            viewModelScope.launch {
                growthRepository.getGrowthHabit()
                    .onSuccess {
                        updateState { copy(habitGrowth = Async.Success(it)) }
                    }
                    .onFailure {
                        if (it is ApiError) sendEffect(GrowthSideEffect.ShowSnackbar(it.snackbarMsg()))
                    }
            }
        }

        fun onTabClick(tab: GrowthHeaderTabType) {
            updateState { copy(currentTab = tab) }
        }

        fun onChildChipClick(index: Int) {
            updateState { copy(selectedChildIdx = index) }
        }
    }
