package com.swyp.firsttodo.presentation.habit.list

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.model.habit.HabitModel
import com.swyp.firsttodo.presentation.habit.component.HabitListType

@Immutable
data class HabitListUiState(
    val role: Role? = null,
    val habits: Async<List<HabitModel>> = Async.Init,
    val delRequestedId: Long? = null,
    val deleteState: Async<Unit> = Async.Init,
    val retryHabits: Async<List<HabitModel>> = Async.Init,
) : UiState {
    val showDeleteDialog = delRequestedId != null

    val habitsData = habits.getDataOrNull()

    val listType = when (role) {
        Role.PARENT -> HabitListType.PARENT
        else -> HabitListType.CHILD
    }

    val description = when (role) {
        Role.PARENT -> "실천하고 싶은 습관을 정하고,\n하나씩 실천해 보세요!"
        Role.CHILD -> "가족과 보상을 정하고\n재미있는 습관을 실천해요!"
        null -> ""
    }
}

sealed interface HabitListSideEffect : UiEffect {
    data class ShowSnackbar(val message: String) : HabitListSideEffect

    data class NavigateToHabitDetail(val habit: HabitModel? = null) : HabitListSideEffect

    data class NavigateToHabitRetry(val habit: HabitModel) : HabitListSideEffect
}
