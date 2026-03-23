package com.swyp.firsttodo.presentation.todo

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState
import com.swyp.firsttodo.core.designsystem.theme.LabelColor
import com.swyp.firsttodo.domain.model.ScheduleCategory
import com.swyp.firsttodo.domain.model.todo.TodoCategoryModel
import com.swyp.firsttodo.presentation.common.component.DeleteDialogType
import com.swyp.firsttodo.presentation.todo.component.DayInfo
import com.swyp.firsttodo.presentation.todo.component.ScheduleBottomSheetType
import com.swyp.firsttodo.presentation.todo.component.ScheduleUiModel
import com.swyp.firsttodo.presentation.todo.component.TodayTodoUiModel
import com.swyp.firsttodo.presentation.todo.component.TodoBottomSheetType
import com.swyp.firsttodo.presentation.todo.util.isTodayOrAfter
import com.swyp.firsttodo.presentation.todo.util.toDateOrNull

data class EditingTodo(
    val todoId: Long? = null,
    val title: String = "",
    val category: TodoCategoryModel? = null,
    val labelColor: LabelColor? = null,
) {
    val isBtnEnabled: Boolean = title.isNotBlank() && category != null && labelColor != null
}

data class EditingSchedule(
    val title: String = "",
    val date: String = "",
    val category: ScheduleCategory? = null,
) {
    val dateErrorText: String? = run {
        if (date.isEmpty()) return@run null
        val parsed = date.toDateOrNull() ?: return@run "올바르지 않은 날짜예요. 다시 확인해 주세요."
        if (!parsed.isTodayOrAfter()) return@run "과거의 날짜예요. 다시 확인해 주세요."
        null
    }

    val isBtnEnabled: Boolean = title.isNotBlank() && date.isNotEmpty() && dateErrorText == null && category != null
}

@Immutable
data class TodoUiState(
    val categories: List<TodoCategoryModel> = emptyList(),
    val remainTodoCount: Async<Int> = Async.Init,
    val dayInfos: Async<List<DayInfo>> = Async.Init,
    val todos: Async<List<TodayTodoUiModel>> = Async.Init,
    val schedules: Async<List<ScheduleUiModel>> = Async.Init,
    val editingTodo: EditingTodo = EditingTodo(),
    val editingSchedule: EditingSchedule = EditingSchedule(),
    val showTodoBottomSheet: Boolean = false,
    val todoBottomSheetType: TodoBottomSheetType = TodoBottomSheetType.CHILD_CREATE,
    val showScheduleBottomSheet: Boolean = false,
    val scheduleBottomSheetType: ScheduleBottomSheetType = ScheduleBottomSheetType.CHILD_CREATE,
    val todoBottomSheetState: Async<Unit> = Async.Init,
    val scheduleBottomSheetState: Async<Unit> = Async.Init,
    val delRequestedId: Long? = null,
    val delRequestedType: DeleteDialogType = DeleteDialogType.TODO,
) : UiState {
    val month = 1

    val week = 1

    val todoCategories: List<TodoCategoryModel> = categories

    val showDeleteDialog = delRequestedId != null
}

sealed interface TodoSideEffect : UiEffect {
    data class ShowSnackbar(val message: String) : TodoSideEffect
}
