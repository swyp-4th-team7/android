package com.swyp.firsttodo.presentation.todo

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState
import com.swyp.firsttodo.core.designsystem.theme.LabelColor
import com.swyp.firsttodo.domain.model.Role
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
    val role: Role? = null,
    val categories: List<TodoCategoryModel> = emptyList(),
    val progressPercent: Async<Int> = Async.Init,
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
    val deleteState: Async<Unit> = Async.Init,
) : UiState {
    val todoCategories: List<TodoCategoryModel> = categories

    val showDeleteDialog = delRequestedId != null

    val characterImageRes = if (todos is Async.Empty) {
        R.drawable.img_todo_empty_176
    } else if (progressPercent is Async.Success) {
        when {
            progressPercent.data == 100 -> R.drawable.img_todo_perfect_176
            progressPercent.data in 50..99 -> R.drawable.img_todo_cheer_176
            else -> R.drawable.img_todo_nagging_176
        }
    } else {
        null
    }

    val bubbleText = if (todos is Async.Empty) {
        if (role == Role.CHILD) {
            "할 일을 추가해 볼까?"
        } else {
            "할 일을 추가해 볼까요?"
        }
    } else if (progressPercent is Async.Success) {
        when {
            progressPercent.data == 100 -> if (role == Role.CHILD) {
                "완전 대단해!!"
            } else {
                "오늘도 수고하셨습니다!"
            }

            progressPercent.data in 50..99 -> if (role == Role.CHILD) {
                "잘하고 있어! 힘내!!"
            } else {
                "잘하고 있습니다!"
            }

            else -> if (role == Role.CHILD) {
                "뭐하고 있어! 빨리 해야해!!"
            } else {
                "빨리 해야해요!!"
            }
        }
    } else {
        null
    }
}

sealed interface TodoSideEffect : UiEffect {
    data class ShowSnackbar(val message: String) : TodoSideEffect

    data object FinishApp : TodoSideEffect
}
