package com.swyp.firsttodo.presentation.todo

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.swyp.firsttodo.core.auth.manager.SessionManager
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.core.designsystem.theme.LabelColor
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.model.ScheduleCategory
import com.swyp.firsttodo.domain.model.ScheduleChildCategory
import com.swyp.firsttodo.domain.model.TodoCategory
import com.swyp.firsttodo.domain.model.TodoChildCategory
import com.swyp.firsttodo.presentation.common.component.DeleteDialogType
import com.swyp.firsttodo.presentation.todo.component.ScheduleBottomSheetType
import com.swyp.firsttodo.presentation.todo.component.ScheduleUiModel
import com.swyp.firsttodo.presentation.todo.component.TodayTodoUiModel
import com.swyp.firsttodo.presentation.todo.component.TodoBottomSheetType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel
    @Inject
    constructor(
        sessionManager: SessionManager,
    ) : BaseViewModel<TodoUiState, TodoSideEffect>(TodoUiState()) {
        val todoFieldState = TextFieldState()
        val scheduleTitleFieldState = TextFieldState()
        val scheduleDateFieldState = TextFieldState()

        private val role: Role = when (sessionManager.sessionState.value.userType) {
            Role.PARENT.request -> Role.PARENT
            else -> Role.CHILD
        }

        init {
            getTodos()
            getSchedules()

            viewModelScope.launch {
                snapshotFlow { todoFieldState.text.toString() }
                    .collect { updateState { copy(editingTodo = editingTodo.copy(title = it)) } }
            }
            viewModelScope.launch {
                snapshotFlow { scheduleTitleFieldState.text.toString() }
                    .collect { updateState { copy(editingSchedule = editingSchedule.copy(title = it)) } }
            }
            viewModelScope.launch {
                snapshotFlow { scheduleDateFieldState.text.toString() }
                    .collect { updateState { copy(editingSchedule = editingSchedule.copy(date = it)) } }
            }
        }

        fun onCalenderPrevClick() {}

        fun onCalenderNextClick() {}

        fun getTodos() {
            updateState { copy(todos = Async.Loading(this.todos.getDataOrNull())) }

            viewModelScope.launch {
                delay(500)

                val newTodos = listOf(
                    TodayTodoUiModel(
                        todoId = 1L,
                        title = "미완료 할 일",
                        completed = false,
                        category = TodoChildCategory.CREATIVE_ACTIVITY,
                        labelColor = LabelColor.BLUE,
                    ),
                    TodayTodoUiModel(
                        todoId = 2L,
                        title = "완료 할 일",
                        completed = true,
                        category = TodoChildCategory.CREATIVE_ACTIVITY,
                        labelColor = LabelColor.PINK,
                    ),
                )

                updateState { copy(todos = if (newTodos.isEmpty()) Async.Empty else Async.Success(newTodos)) }
            }
        }

        fun getSchedules() {
            updateState { copy(schedules = Async.Loading(this.schedules.getDataOrNull())) }

            viewModelScope.launch {
                delay(500)

                val newSchedules = listOf(
                    ScheduleUiModel(
                        scheduleId = 1L,
                        dDay = 14,
                        title = "영어 자기소개 외워서 말하기",
                        date = "2026.03.12.일요일",
                        rawDate = "20260312",
                        category = ScheduleChildCategory.CONTEST,
                        isUrgent = false,
                    ),
                    ScheduleUiModel(
                        scheduleId = 2L,
                        dDay = 1,
                        title = "영어 자기소개 외워서 말하기",
                        date = "2026.03.12.일요일",
                        rawDate = "20260312",
                        category = ScheduleChildCategory.FINAL_EXAM,
                        isUrgent = true,
                    ),
                )

                updateState {
                    copy(
                        schedules = if (newSchedules.isEmpty()) Async.Empty else Async.Success(newSchedules),
                    )
                }
            }
        }

        fun completeTodo(todoUiModel: TodayTodoUiModel) {
            if (todoUiModel.completed) return

            // TODO: 수정 API 호출
        }

        fun openTodoCreateBottomSheet() {
            val sheetType = when (role) {
                Role.PARENT -> TodoBottomSheetType.PARENT_CREATE
                Role.CHILD -> TodoBottomSheetType.CHILD_CREATE
            }

            updateState { copy(showTodoBottomSheet = true, todoBottomSheetType = sheetType) }
        }

        fun openTodoEditBottomSheet(todoUiModel: TodayTodoUiModel) {
            val sheetType = when (role) {
                Role.PARENT -> TodoBottomSheetType.PARENT_EDIT
                Role.CHILD -> TodoBottomSheetType.CHILD_EDIT
            }

            todoFieldState.edit { replace(0, length, todoUiModel.title) }
            updateState {
                copy(
                    showTodoBottomSheet = true,
                    todoBottomSheetType = sheetType,
                    editingTodo = editingTodo.copy(
                        category = todoUiModel.category,
                        labelColor = todoUiModel.labelColor,
                    ),
                )
            }
        }

        fun openScheduleCreateBottomSheet() {
            val sheetType = when (role) {
                Role.PARENT -> ScheduleBottomSheetType.PARENT_CREATE
                Role.CHILD -> ScheduleBottomSheetType.CHILD_CREATE
            }

            updateState { copy(showScheduleBottomSheet = true, scheduleBottomSheetType = sheetType) }
        }

        fun openScheduleEditBottomSheet(scheduleUiModel: ScheduleUiModel) {
            val sheetType = when (role) {
                Role.PARENT -> ScheduleBottomSheetType.PARENT_EDIT
                Role.CHILD -> ScheduleBottomSheetType.CHILD_EDIT
            }

            scheduleTitleFieldState.edit { replace(0, length, scheduleUiModel.title) }
            scheduleDateFieldState.edit { replace(0, length, scheduleUiModel.rawDate) }
            updateState {
                copy(
                    showScheduleBottomSheet = true,
                    scheduleBottomSheetType = sheetType,
                    editingSchedule = editingSchedule.copy(category = scheduleUiModel.category),
                )
            }
        }

        fun closeTodoBottomSheet() {
            updateState { copy(showTodoBottomSheet = false) }
            clearEditingTodo()
        }

        fun closeScheduleBottomSheet() {
            updateState { copy(showScheduleBottomSheet = false) }
            clearEditingSchedule()
        }

        private fun clearEditingTodo() {
            todoFieldState.clearText()
            updateState { copy(editingTodo = EditingTodo()) }
        }

        private fun clearEditingSchedule() {
            scheduleTitleFieldState.clearText()
            scheduleDateFieldState.clearText()
            updateState { copy(editingSchedule = EditingSchedule()) }
        }

        fun openTodoDialog(todoUiModel: TodayTodoUiModel) {
            updateState { copy(delRequestedId = todoUiModel.todoId, delRequestedType = DeleteDialogType.TODO) }
        }

        fun openScheduleDialog(scheduleUiModel: ScheduleUiModel) {
            updateState {
                copy(
                    delRequestedId = scheduleUiModel.scheduleId,
                    delRequestedType = DeleteDialogType.SCHEDULE,
                )
            }
        }

        fun onDeleteCancel() {
            updateState { copy(delRequestedId = null) }
        }

        fun onDeleteConfirm() {
            when (uiState.value.delRequestedType) {
                DeleteDialogType.TODO -> deleteTodo()
                else -> deleteSchedule()
            }
            updateState { copy(delRequestedId = null) }
            sendEffect(TodoSideEffect.ShowToast("삭제되었어요."))
        }

        private fun deleteTodo() {
            // TODO : 삭제 API
        }

        private fun deleteSchedule() {
            // TODO : 삭제 API
        }

        fun onTodoCategoryClick(category: TodoCategory) {
            updateState { copy(editingTodo = this.editingTodo.copy(category = category)) }
        }

        fun onTodoColorClick(colorType: LabelColor) {
            updateState { copy(editingTodo = this.editingTodo.copy(labelColor = colorType)) }
        }

        fun onTodoBottomBtnClick() {
            when (uiState.value.todoBottomSheetType) {
                TodoBottomSheetType.CHILD_CREATE -> createTodo()
                TodoBottomSheetType.CHILD_EDIT -> editTodo()
                TodoBottomSheetType.PARENT_CREATE -> createTodo()
                TodoBottomSheetType.PARENT_EDIT -> editTodo()
            }
        }

        private fun createTodo() {
            // TODO: 생성 API
        }

        private fun editTodo() {
            // TODO: 수정 API
        }

        fun onScheduleCategoryClick(category: ScheduleCategory) {
            updateState { copy(editingSchedule = this.editingSchedule.copy(category = category)) }
        }

        fun onScheduleBottomBtnClick() {
            when (uiState.value.scheduleBottomSheetType) {
                ScheduleBottomSheetType.CHILD_CREATE -> createSchedule()
                ScheduleBottomSheetType.CHILD_EDIT -> editSchedule()
                ScheduleBottomSheetType.PARENT_CREATE -> createSchedule()
                ScheduleBottomSheetType.PARENT_EDIT -> editSchedule()
            }
        }

        private fun createSchedule() {
            // TODO: 생성 API
        }

        private fun editSchedule() {
            // TODO: 수정 API
        }
    }
