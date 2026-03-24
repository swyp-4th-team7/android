package com.swyp.firsttodo.presentation.habit.list

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.domain.model.habit.HabitModel

@Immutable
data class HabitListUiState(
    val habits: Async<List<HabitModel>> = Async.Init,
    val delRequestedId: Long? = null,
) : UiState {
    val showDeleteDialog = delRequestedId != null

    val habitsData = habits.getDataOrNull()
}

sealed interface HabitListSideEffect : UiEffect {
    data class ShowSnackbar(val message: String) : HabitListSideEffect

    data class NavigateToHabitDetail(val habit: HabitModel? = null) : HabitListSideEffect
}
