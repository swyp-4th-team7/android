package com.swyp.firsttodo.presentation.todo

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState
import com.swyp.firsttodo.core.common.type.DeleteDialogType
import com.swyp.firsttodo.core.designsystem.theme.LabelColor
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.model.ScheduleCategory
import com.swyp.firsttodo.domain.model.sticker.WeeklyStickersModel
import com.swyp.firsttodo.domain.model.todo.TodoCategoryModel
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
    val originalTitle: String? = null,
    val originalCategory: TodoCategoryModel? = null,
    val originalLabelColor: LabelColor? = null,
) {
    private val isEditMode = todoId != null
    private val hasChanges = title != originalTitle || category != originalCategory || labelColor != originalLabelColor
    val isBtnEnabled: Boolean = title.isNotBlank() && category != null && labelColor != null &&
        (!isEditMode || hasChanges)
}

data class EditingSchedule(
    val scheduleId: Long? = null,
    val title: String = "",
    val date: String = "",
    val category: ScheduleCategory? = null,
    val originalTitle: String? = null,
    val originalDate: String? = null,
    val originalCategory: ScheduleCategory? = null,
) {
    val dateErrorText: String? = run {
        if (date.isEmpty()) return@run null
        val parsed = date.toDateOrNull() ?: return@run "올바르지 않은 날짜예요. 다시 확인해 주세요."
        if (!parsed.isTodayOrAfter()) return@run "과거의 날짜예요. 다시 확인해 주세요."
        null
    }

    private val isEditMode = scheduleId != null
    private val hasChanges = title != originalTitle || date != originalDate || category != originalCategory
    val isBtnEnabled: Boolean = title.isNotBlank() && date.isNotEmpty() && dateErrorText == null && category != null &&
        (!isEditMode || hasChanges)
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
    val schedules: Async<List<ScheduleUiModel>> = Async.Init,
    val editingTodo: EditingTodo = EditingTodo(),
    val editingSchedule: EditingSchedule = EditingSchedule(),
    val showTodoBottomSheet: Boolean = false,
    val showScheduleBottomSheet: Boolean = false,
    val scheduleBottomSheetType: ScheduleBottomSheetType = ScheduleBottomSheetType.CHILD_CREATE,
    val todoBottomSheetType: TodoBottomSheetType = TodoBottomSheetType.CHILD_CREATE,
    val scheduleBottomSheetState: Async<Unit> = Async.Init,
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
                "오늘도 최고야!"
            } else {
                "오늘도 수고하셨습니다!"
            }

            progressPercent.data in 50..99 -> if (role == Role.CHILD) {
                "잘하고 있어~ 조금만 더!"
            } else {
                "잘하고 있어요~ 조금만 더!"
            }

            else -> if (role == Role.CHILD) {
                "오늘 안에 할 수 있지?"
            } else {
                "아직 남은 할 일이 있어요!"
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
