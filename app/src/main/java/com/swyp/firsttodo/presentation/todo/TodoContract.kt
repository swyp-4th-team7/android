package com.swyp.firsttodo.presentation.todo

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState
import com.swyp.firsttodo.core.designsystem.theme.LabelColor
import com.swyp.firsttodo.domain.model.sticker.WeeklyStickersModel
import com.swyp.firsttodo.domain.model.todo.TodoCategoryModel
import com.swyp.firsttodo.presentation.common.component.DeleteDialogType
import com.swyp.firsttodo.presentation.todo.component.TodayTodoUiModel
import com.swyp.firsttodo.presentation.todo.component.TodoBottomSheetType

data class EditingTodo(
    val todoId: Long? = null,
    val title: String = "",
    val category: TodoCategoryModel? = null,
    val labelColor: LabelColor? = null,
) {
    val isBtnEnabled: Boolean = title.isNotBlank() && category != null && labelColor != null
}

@Immutable
data class TodoUiState(
    val categories: List<TodoCategoryModel> = emptyList(),
    val remainTodoCount: Async<Int> = Async.Init,
    val weeklyStickers: Async<WeeklyStickersModel> = Async.Init,
    val weekOffset: Int = 0,
    val todos: Async<List<TodayTodoUiModel>> = Async.Init,
    val editingTodo: EditingTodo = EditingTodo(),
    val showTodoBottomSheet: Boolean = false,
    val todoBottomSheetType: TodoBottomSheetType = TodoBottomSheetType.CHILD_CREATE,
    val todoBottomSheetState: Async<Unit> = Async.Init,
    val delRequestedId: Long? = null,
    val delRequestedType: DeleteDialogType = DeleteDialogType.Todo,
) : UiState {
    val todoCategories: List<TodoCategoryModel> = categories

    val showDeleteDialog = delRequestedId != null
}

sealed interface TodoSideEffect : UiEffect {
    data class ShowSnackbar(val message: String) : TodoSideEffect
}
