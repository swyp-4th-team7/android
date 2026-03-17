package com.swyp.firsttodo.presentation.habit.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.swyp.firsttodo.core.auth.manager.SessionManager
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.presentation.habit.navigation.HabitNavArgs
import com.swyp.firsttodo.presentation.habit.navigation.HabitNavArgsNavType
import com.swyp.firsttodo.presentation.habit.navigation.HabitRoute
import com.swyp.firsttodo.presentation.habit.navigation.toHabit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class HabitDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        sessionManager: SessionManager,
    ) : BaseViewModel<HabitDetailUiState, HabitDetailSideEffect>(HabitDetailUiState()) {
        private val role: Role = when (sessionManager.sessionState.value.userType) {
            Role.PARENT.request -> Role.PARENT
            else -> Role.CHILD
        }

        val screenType = when (role) {
            Role.PARENT -> HabitDetailScreenType.PARENT
            Role.CHILD -> HabitDetailScreenType.CHILD
        }

        private val initialHabit = savedStateHandle
            .toRoute<HabitRoute.HabitDetail>(typeMap = mapOf(typeOf<HabitNavArgs?>() to HabitNavArgsNavType))
            .habitNavArgs?.toHabit()

        val titleState = TextFieldState(initialText = initialHabit?.title ?: "")
        val rewardState = TextFieldState(initialText = initialHabit?.reward ?: "")

        private val validInputRegex = Regex("^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9\\s]+$")

        init {
            val state = when (initialHabit) {
                null -> HabitDetailScreenState.CREATE
                else -> HabitDetailScreenState.EDIT
            }

            updateState {
                copy(
                    duration = initialHabit?.duration,
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
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

        fun onBackClick() {
            sendEffect(HabitDetailSideEffect.PopBackStack)
        }

        fun onDurationClick(duration: HabitDuration) {
            updateState { copy(duration = duration) }
        }

        private fun validateTitle(): Boolean {
            val title = titleState.text.toString()
            return title.isNotBlank() && title.length <= 12 && validInputRegex.matches(title)
        }

        private fun validateReward(): Boolean {
            val reward = rewardState.text.toString()
            return reward.isNotBlank() && validInputRegex.matches(reward)
        }

        private fun validateDuration(): Boolean = uiState.value.duration != null

        private fun validateAllField(): Boolean = validateTitle() && validateReward() && validateDuration()

        private fun isChanged(): Boolean =
            titleState.text.toString() != initialHabit?.title ||
                rewardState.text.toString() != initialHabit.reward ||
                uiState.value.duration != initialHabit.duration

        fun onBtnClick() {
            when (uiState.value.screenState) {
                HabitDetailScreenState.IDLE -> Unit
                HabitDetailScreenState.CREATE -> {
                    // TODO: CREATE API
                    sendEffect(HabitDetailSideEffect.ShowToast("습관 생성이 완료되었어요."))
                    sendEffect(HabitDetailSideEffect.PopBackStack)
                }

                HabitDetailScreenState.EDIT -> {
                    // TODO: EDIT API
                    sendEffect(HabitDetailSideEffect.ShowToast("습관 수정이 완료되었어요."))
                    sendEffect(HabitDetailSideEffect.PopBackStack)
                }
            }
        }
    }
