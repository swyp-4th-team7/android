package com.swyp.firsttodo.presentation.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.util.HandleSideEffects
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.LabelColor
import com.swyp.firsttodo.domain.model.sticker.WeeklyStickerModel
import com.swyp.firsttodo.domain.model.sticker.WeeklyStickersModel
import com.swyp.firsttodo.domain.model.todo.TodoCategoryModel
import com.swyp.firsttodo.presentation.common.component.HaebomDeleteDialog
import com.swyp.firsttodo.presentation.common.component.TopBarArea
import com.swyp.firsttodo.presentation.main.snackbar.LocalSnackbarHostState
import com.swyp.firsttodo.presentation.main.snackbar.showHaebomSnackbar
import com.swyp.firsttodo.presentation.todo.component.TodayTodoUiModel
import com.swyp.firsttodo.presentation.todo.component.TodoBanner
import com.swyp.firsttodo.presentation.todo.component.TodoBottomSheet
import com.swyp.firsttodo.presentation.todo.component.TodoList
import com.swyp.firsttodo.presentation.todo.component.WeeklyCalendar

@Composable
fun TodoRoute(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHost = LocalSnackbarHostState.current

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            is TodoSideEffect.ShowSnackbar -> snackbarHost.showHaebomSnackbar(effect.message)
        }
    }

    if (uiState.showDeleteDialog) {
        HaebomDeleteDialog(
            dialogType = uiState.delRequestedType,
            onConfirm = viewModel::onDeleteConfirm,
            onCancel = viewModel::onDeleteCancel,
            onDismiss = viewModel::onDeleteCancel,
        )
    }

    if (uiState.showTodoBottomSheet) {
        TodoBottomSheet(
            sheetType = uiState.todoBottomSheetType,
            btnEnabled = uiState.editingTodo.isBtnEnabled,
            loadingStatus = uiState.todoBottomSheetState,
            categories = uiState.todoCategories,
            selectedCategory = uiState.editingTodo.category,
            selectedLabelColor = uiState.editingTodo.labelColor,
            titleFieldState = viewModel.todoFieldState,
            onLabelColorClick = viewModel::onTodoColorClick,
            onBtnClick = viewModel::onTodoBottomBtnClick,
            onCategoryClick = viewModel::onTodoCategoryClick,
            onDismiss = viewModel::closeTodoBottomSheet,
        )
    }

    TodoScreen(
        uiState = uiState,
        onCalenderPrevClick = viewModel::onCalenderPrevClick,
        onCalenderNextClick = viewModel::onCalenderNextClick,
        onTodoPlusClick = viewModel::openTodoCreateBottomSheet,
        onTodoCheckClick = viewModel::toggleCompleteTodo,
        onTodoEditClick = viewModel::openTodoEditBottomSheet,
        onTodoDeleteClick = viewModel::openTodoDialog,
        modifier = modifier,
    )
}

@Composable
fun TodoScreen(
    uiState: TodoUiState,
    onCalenderPrevClick: () -> Unit,
    onCalenderNextClick: () -> Unit,
    onTodoPlusClick: () -> Unit,
    onTodoCheckClick: (TodayTodoUiModel) -> Unit,
    onTodoEditClick: (TodayTodoUiModel) -> Unit,
    onTodoDeleteClick: (TodayTodoUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HaebomTheme.colors.white),
    ) {
        TopBarArea()

        TodoBanner(
            remainTodo = uiState.remainTodoCount,
        )

        Column(
            modifier = Modifier
                .verticalScroll(scrollState),
        ) {
            WeeklyCalendar(
                onPrevClick = onCalenderPrevClick,
                onNextClick = onCalenderNextClick,
                weeklyStickers = uiState.weeklyStickers,
                modifier = Modifier.padding(bottom = 28.dp),
            )

            TodoList(
                todos = uiState.todos,
                onPlusClick = onTodoPlusClick,
                onCheckClick = onTodoCheckClick,
                onEditClick = onTodoEditClick,
                onDeleteClick = onTodoDeleteClick,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

private val previewWeeklyStickers = WeeklyStickersModel(
    weekLabel = "3월 2주차",
    weekOffset = 0,
    startDate = "2026-03-10",
    endDate = "2026-03-16",
    stickers = listOf(
        WeeklyStickerModel("2026-03-10", "BASIC_STICKER"),
        WeeklyStickerModel("2026-03-11", null),
        WeeklyStickerModel("2026-03-12", "BASIC_STICKER"),
        WeeklyStickerModel("2026-03-13", null),
        WeeklyStickerModel("2026-03-14", null),
        WeeklyStickerModel("2026-03-15", null),
        WeeklyStickerModel("2026-03-16", null),
    ),
)

private val previewTodos = listOf(
    TodayTodoUiModel(
        todoId = 1L,
        title = "수학 숙제 완료하기",
        completed = false,
        category = TodoCategoryModel("HOMEWORK", "숙제"),
        labelColor = LabelColor.BLUE,
    ),
    TodayTodoUiModel(
        todoId = 2L,
        title = "영어 단어 20개 외우기",
        completed = true,
        category = TodoCategoryModel("STUDY", "공부"),
        labelColor = LabelColor.PINK,
    ),
    TodayTodoUiModel(
        todoId = 3L,
        title = "방 청소하기",
        completed = false,
        category = TodoCategoryModel("CLEANING", "정리"),
        labelColor = LabelColor.MINT,
    ),
    TodayTodoUiModel(
        todoId = 4L,
        title = "수학 숙제 완료하기",
        completed = false,
        category = TodoCategoryModel("HOMEWORK", "숙제"),
        labelColor = LabelColor.BLUE,
    ),
    TodayTodoUiModel(
        todoId = 5L,
        title = "영어 단어 20개 외우기",
        completed = true,
        category = TodoCategoryModel("STUDY", "공부"),
        labelColor = LabelColor.PINK,
    ),
    TodayTodoUiModel(
        todoId = 6L,
        title = "방 청소하기 방 청소하기 방 청소하기 방 청소하기 방 청소하기",
        completed = false,
        category = TodoCategoryModel("CLEANING", "정리"),
        labelColor = LabelColor.MINT,
    ),
)

private class TodoScreenPreviewProvider : PreviewParameterProvider<TodoUiState> {
    override val values = sequenceOf(
        TodoUiState(
            remainTodoCount = Async.Success(2),
            weeklyStickers = Async.Success(previewWeeklyStickers),
            todos = Async.Success(previewTodos),
        ),
        TodoUiState(
            remainTodoCount = Async.Success(0),
            weeklyStickers = Async.Success(previewWeeklyStickers),
            todos = Async.Empty,
        ),
        TodoUiState(
            remainTodoCount = Async.Loading(),
            weeklyStickers = Async.Loading(),
            todos = Async.Loading(),
        ),
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun TodoScreenPreview(
    @PreviewParameter(TodoScreenPreviewProvider::class) uiState: TodoUiState,
) {
    HaebomTheme {
        TodoScreen(
            uiState = uiState,
            onCalenderPrevClick = {},
            onCalenderNextClick = {},
            onTodoPlusClick = {},
            onTodoCheckClick = {},
            onTodoEditClick = {},
            onTodoDeleteClick = {},
        )
    }
}
