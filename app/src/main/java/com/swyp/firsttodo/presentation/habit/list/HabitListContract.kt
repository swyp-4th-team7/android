package com.swyp.firsttodo.presentation.habit.list

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.domain.model.habit.Habit

@Immutable
data class HabitListUiState(
    val habits: Async<List<Habit>> = Async.Init,
    val delRequestedId: Long? = null,
) : UiState {
    val showDeleteDialog = delRequestedId != null

    val habitsData = habits.getDataOrNull()
}

sealed interface HabitListSideEffect : UiEffect {
    data class ShowToast(val message: String) : HabitListSideEffect

    data class NavigateToHabitDetail(val habit: Habit? = null) : HabitListSideEffect
}
