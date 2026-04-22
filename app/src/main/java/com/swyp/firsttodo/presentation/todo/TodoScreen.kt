package com.swyp.firsttodo.presentation.todo

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.component.HaebomDeleteDialog
import com.swyp.firsttodo.core.common.component.TopBarArea
import com.swyp.firsttodo.core.common.util.HandleSideEffects
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.LabelColor
import com.swyp.firsttodo.domain.model.ScheduleCategory
import com.swyp.firsttodo.domain.model.sticker.WeeklyStickerModel
import com.swyp.firsttodo.domain.model.sticker.WeeklyStickersModel
import com.swyp.firsttodo.domain.model.todo.TodoCategoryModel
import com.swyp.firsttodo.presentation.main.snackbar.LocalSnackbarHostState
import com.swyp.firsttodo.presentation.main.snackbar.showHaebomSnackbar
import com.swyp.firsttodo.presentation.todo.component.ScheduleBottomSheet
import com.swyp.firsttodo.presentation.todo.component.ScheduleList
import com.swyp.firsttodo.presentation.todo.component.ScheduleUiModel
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
    val activity = LocalContext.current as? Activity

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            is TodoSideEffect.ShowSnackbar -> snackbarHost.showHaebomSnackbar(effect.message)
            TodoSideEffect.FinishApp -> activity?.finish()
        }
    }

    BackHandler {
        viewModel.onBack()
    }

    if (uiState.showDeleteDialog) {
        HaebomDeleteDialog(
            dialogType = uiState.delRequestedType,
            onConfirm = viewModel::onDeleteConfirm,
            onCancel = viewModel::onDeleteCancel,
            onDismiss = viewModel::onDeleteCancel,
            loadingState = uiState.deleteState,
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

    if (uiState.showScheduleBottomSheet) {
        ScheduleBottomSheet(
            sheetType = uiState.scheduleBottomSheetType,
            btnEnabled = uiState.editingSchedule.isBtnEnabled,
            loadingStatus = uiState.scheduleBottomSheetState,
            selectedCategory = uiState.editingSchedule.category,
            titleFieldState = viewModel.scheduleTitleFieldState,
            dateFieldState = viewModel.scheduleDateFieldState,
            dateErrorText = uiState.editingSchedule.dateErrorText,
            onBtnClick = viewModel::onScheduleBottomBtnClick,
            onCategoryClick = viewModel::onScheduleCategoryClick,
            onDismiss = viewModel::closeScheduleBottomSheet,
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
        onSchedulePlusClick = viewModel::openScheduleCreateBottomSheet,
        onScheduleEditClick = viewModel::openScheduleEditBottomSheet,
        onScheduleDeleteClick = viewModel::openScheduleDialog,
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
    onSchedulePlusClick: () -> Unit,
    onScheduleEditClick: (ScheduleUiModel) -> Unit,
    onScheduleDeleteClick: (ScheduleUiModel) -> Unit,
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
            imageRes = uiState.characterImageRes,
            bubbleText = uiState.bubbleText,
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

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                thickness = 0.8.dp,
                color = HaebomTheme.colors.gray50,
            )

            ScheduleList(
                schedules = uiState.schedules,
                onPlusClick = onSchedulePlusClick,
                onEditClick = onScheduleEditClick,
                onDeleteClick = onScheduleDeleteClick,
                scrollState = scrollState,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 27.dp),
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

private val previewSchedules = listOf(
    ScheduleUiModel(
        scheduleId = 1L,
        dDay = 3,
        title = "기말고사",
        date = "2026.03.20.금요일",
        rawDate = "20260320",
        category = ScheduleCategory.SCHOOL_EXAM,
        isUrgent = true,
    ),
    ScheduleUiModel(
        scheduleId = 2L,
        dDay = 14,
        title = "영어 말하기 대회",
        date = "2026.03.31.화요일",
        rawDate = "20260331",
        category = ScheduleCategory.SCHOOL_EXAM,
        isUrgent = false,
    ),
)

private class TodoScreenPreviewProvider : PreviewParameterProvider<TodoUiState> {
    override val values = sequenceOf(
        TodoUiState(
            remainTodoCount = Async.Success(2),
            weeklyStickers = Async.Success(previewWeeklyStickers),
            todos = Async.Success(previewTodos),
            schedules = Async.Success(previewSchedules),
        ),
        TodoUiState(
            remainTodoCount = Async.Success(0),
            weeklyStickers = Async.Success(previewWeeklyStickers),
            todos = Async.Empty,
            schedules = Async.Empty,
        ),
        TodoUiState(
            remainTodoCount = Async.Loading(),
            weeklyStickers = Async.Loading(),
            todos = Async.Loading(),
            schedules = Async.Loading(),
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
            onSchedulePlusClick = {},
            onScheduleEditClick = {},
            onScheduleDeleteClick = {},
        )
    }
}
