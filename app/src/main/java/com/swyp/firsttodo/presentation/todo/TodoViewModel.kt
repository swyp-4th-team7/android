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
import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.model.ScheduleCategory
import com.swyp.firsttodo.domain.model.todo.TodoCategoryModel
import com.swyp.firsttodo.domain.repository.ScheduleRepository
import com.swyp.firsttodo.domain.repository.TodoRepository
import com.swyp.firsttodo.domain.throwable.ScheduleError
import com.swyp.firsttodo.domain.throwable.TodoError
import com.swyp.firsttodo.presentation.common.component.DeleteDialogType
import com.swyp.firsttodo.presentation.common.extension.snackbarMsg
import com.swyp.firsttodo.presentation.todo.component.ScheduleBottomSheetType
import com.swyp.firsttodo.presentation.todo.component.ScheduleUiModel
import com.swyp.firsttodo.presentation.todo.component.TodayTodoUiModel
import com.swyp.firsttodo.presentation.todo.component.TodoBottomSheetType
import com.swyp.firsttodo.presentation.todo.extension.toLabelColor
import com.swyp.firsttodo.presentation.todo.extension.toTodoColor
import com.swyp.firsttodo.presentation.todo.util.removeDashes
import com.swyp.firsttodo.presentation.todo.util.toDashedDate
import com.swyp.firsttodo.presentation.todo.util.toDisplayDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel
    @Inject
    constructor(
        sessionManager: SessionManager,
        private val todoRepository: TodoRepository,
        private val scheduleRepository: ScheduleRepository,
    ) : BaseViewModel<TodoUiState, TodoSideEffect>(TodoUiState()) {
        val todoFieldState = TextFieldState()
        val scheduleTitleFieldState = TextFieldState()
        val scheduleDateFieldState = TextFieldState()

        private val role: Role = when (sessionManager.sessionState.value.userType) {
            Role.PARENT.request -> Role.PARENT
            else -> Role.CHILD
        }

        init {
            getTodoCategories()
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

        fun getTodoCategories() {
            viewModelScope.launch {
                todoRepository.getTodoCategories()
                    .onSuccess { categories ->
                        updateState { copy(categories = categories) }
                    }
                    .onFailure {
                    }
            }
        }

        fun getTodos() {
            updateState { copy(todos = Async.Loading(this.todos.getDataOrNull())) }

            viewModelScope.launch {
                todoRepository.getTodos()
                    .onSuccess { data ->
                        val categories = uiState.value.categories

                        val newTodos = data.todos.map { todo ->
                            TodayTodoUiModel(
                                todoId = todo.todoId,
                                title = todo.title,
                                completed = todo.isCompleted,
                                category = categories.find { it.name == todo.category }
                                    ?: TodoCategoryModel(name = todo.category, label = todo.category),
                                labelColor = todo.color.toLabelColor(),
                            )
                        }

                        updateState {
                            copy(
                                remainTodoCount = Async.Success(data.remainingCount),
                                todos = if (newTodos.isEmpty()) Async.Empty else Async.Success(newTodos),
                            )
                        }
                    }
                    .onFailure { throwable ->
                        val prevData = uiState.value.todos.getDataOrNull()
                        updateState {
                            copy(
                                todos = if (prevData == null) Async.Init else Async.Success(prevData),
                            )
                        }

                        if (throwable is ApiError) {
                            sendEffect(TodoSideEffect.ShowSnackbar(throwable.snackbarMsg()))
                        }
                    }
            }
        }

        fun getSchedules() {
            updateState { copy(schedules = Async.Loading(this.schedules.getDataOrNull())) }

            viewModelScope.launch {
                scheduleRepository.getSchedules()
                    .onSuccess { list ->
                        val newSchedules = list.map { model ->
                            ScheduleUiModel(
                                scheduleId = model.scheduleId,
                                dDay = model.dDay.toInt(),
                                title = model.title,
                                date = model.scheduleDate.toDisplayDate(),
                                rawDate = model.scheduleDate.removeDashes(),
                                category = model.category,
                                isUrgent = model.dDay < 15,
                            )
                        }

                        updateState {
                            copy(
                                schedules = if (list.isEmpty()) Async.Empty else Async.Success(newSchedules),
                            )
                        }
                    }
                    .onFailure {
                        val prevData = uiState.value.schedules.getDataOrNull()
                        updateState {
                            copy(schedules = if (prevData == null) Async.Init else Async.Success(prevData))
                        }

                        if (it is ApiError) sendEffect(TodoSideEffect.ShowSnackbar(it.snackbarMsg()))
                    }
            }
        }

        fun toggleCompleteTodo(todoUiModel: TodayTodoUiModel) {
            viewModelScope.launch {
                todoRepository.editTodo(
                    todoId = todoUiModel.todoId,
                    completed = !todoUiModel.completed,
                ).onSuccess {
                    getTodos()
                }.onFailure { throwable ->
                    val message = when (throwable) {
                        is TodoError.IdNotFound -> {
                            getTodos()
                            "이미 삭제된 할 일이에요."
                        }

                        is ApiError -> throwable.snackbarMsg()
                        else -> ""
                    }
                    sendEffect(TodoSideEffect.ShowSnackbar(message))
                }
            }
        }

        fun openTodoCreateBottomSheet() {
            val sheetType = when (role) {
                Role.PARENT -> TodoBottomSheetType.PARENT_CREATE
                Role.CHILD -> TodoBottomSheetType.CHILD_CREATE
            }

            clearEditingTodo()
            updateState {
                copy(
                    showTodoBottomSheet = true,
                    todoBottomSheetType = sheetType,
                    todoBottomSheetState = Async.Init,
                )
            }
        }

        fun openTodoEditBottomSheet(todoUiModel: TodayTodoUiModel) {
            val sheetType = when (role) {
                Role.PARENT -> TodoBottomSheetType.PARENT_EDIT
                Role.CHILD -> TodoBottomSheetType.CHILD_EDIT
            }

            clearEditingTodo()
            todoFieldState.edit { replace(0, length, todoUiModel.title) }
            updateState {
                copy(
                    showTodoBottomSheet = true,
                    todoBottomSheetType = sheetType,
                    todoBottomSheetState = Async.Init,
                    editingTodo = editingTodo.copy(
                        todoId = todoUiModel.todoId,
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

            clearEditingSchedule()
            updateState {
                copy(
                    showScheduleBottomSheet = true,
                    scheduleBottomSheetType = sheetType,
                    scheduleBottomSheetState = Async.Init,
                )
            }
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
                    scheduleBottomSheetState = Async.Init,
                    editingSchedule = editingSchedule.copy(
                        scheduleId = scheduleUiModel.scheduleId,
                        category = scheduleUiModel.category,
                    ),
                )
            }
        }

        fun closeTodoBottomSheet() {
            updateState { copy(showTodoBottomSheet = false) }
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
            updateState { copy(delRequestedId = todoUiModel.todoId, delRequestedType = DeleteDialogType.Todo) }
        }

        fun openScheduleDialog(scheduleUiModel: ScheduleUiModel) {
            updateState {
                copy(
                    delRequestedId = scheduleUiModel.scheduleId,
                    delRequestedType = DeleteDialogType.Schedule,
                )
            }
        }

        fun onDeleteCancel() {
            updateState { copy(delRequestedId = null) }
        }

        fun onDeleteConfirm() {
            when (uiState.value.delRequestedType) {
                DeleteDialogType.Todo -> deleteTodo()
                DeleteDialogType.Schedule -> deleteSchedule()
                else -> Unit
            }
        }

        private fun deleteTodo() {
            val todoId = uiState.value.delRequestedId ?: return

            viewModelScope.launch {
                todoRepository.deleteTodo(todoId)
                    .onSuccess {
                        getTodos()
                        updateState { copy(delRequestedId = null) }
                        sendEffect(TodoSideEffect.ShowSnackbar("할 일이 삭제되었습니다."))
                    }
                    .onFailure { throwable ->
                        val message = when (throwable) {
                            is TodoError.IdNotFound -> {
                                getTodos()
                                updateState { copy(delRequestedId = null) }
                                "이미 삭제된 할 일 입니다."
                            }

                            is ApiError -> throwable.snackbarMsg()
                            else -> ""
                        }

                        sendEffect(TodoSideEffect.ShowSnackbar(message))
                    }
            }
        }

        private fun deleteSchedule() {
            val scheduleId = uiState.value.delRequestedId ?: return

            viewModelScope.launch {
                scheduleRepository.deleteSchedule(scheduleId)
                    .onSuccess {
                        getSchedules()
                        updateState { copy(delRequestedId = null) }
                        sendEffect(TodoSideEffect.ShowSnackbar("다가오는 일정이 삭제되었습니다."))
                    }
                    .onFailure { throwable ->
                        val message = when (throwable) {
                            is ScheduleError.ScheduleNotFound -> {
                                updateState { copy(delRequestedId = null) }
                                getSchedules()
                                "이미 삭제된 일정입니다."
                            }

                            is ApiError -> throwable.snackbarMsg()

                            else -> return@launch
                        }
                        sendEffect(TodoSideEffect.ShowSnackbar(message))
                    }
            }
        }

        fun onTodoCategoryClick(category: TodoCategoryModel) {
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
            if (uiState.value.todoBottomSheetState is Async.Loading) return
            if (!uiState.value.editingTodo.isBtnEnabled) return

            updateState { copy(todoBottomSheetState = Async.Loading()) }
            viewModelScope.launch {
                val inputs = uiState.value.editingTodo
                val category = inputs.category?.name ?: return@launch
                val color = inputs.labelColor?.toTodoColor() ?: return@launch

                todoRepository.createTodo(
                    title = inputs.title,
                    category = category,
                    color = color,
                ).onSuccess {
                    updateState { copy(todoBottomSheetState = Async.Success(Unit), showTodoBottomSheet = false) }
                    getTodos()
                    sendEffect(TodoSideEffect.ShowSnackbar("할 일이 추가되었습니다."))
                }.onFailure { throwable ->
                    updateState { copy(todoBottomSheetState = Async.Init) }
                    val message = when (throwable) {
                        is TodoError.TitleEmpty -> "할 일을 입력해주세요."
                        is TodoError.CategoryEmpty -> "카테고리를 선택해주세요."
                        is TodoError.ColorEmpty -> "색상을 선택해주세요."
                        is TodoError.CategoryInvalid -> {
                            getTodoCategories()
                            "유효하지 않은 카테고리예요. 다시 선택해주세요."
                        }

                        is ApiError -> throwable.snackbarMsg()
                        else -> ""
                    }
                    sendEffect(TodoSideEffect.ShowSnackbar(message))
                }
            }
        }

        private fun editTodo() {
            if (uiState.value.todoBottomSheetState is Async.Loading) return
            if (!uiState.value.editingTodo.isBtnEnabled) return

            updateState { copy(todoBottomSheetState = Async.Loading()) }
            viewModelScope.launch {
                val inputs = uiState.value.editingTodo
                val todoId = inputs.todoId ?: return@launch
                val category = inputs.category?.name ?: return@launch
                val color = inputs.labelColor?.toTodoColor() ?: return@launch

                todoRepository.editTodo(
                    todoId = todoId,
                    title = inputs.title,
                    category = category,
                    color = color,
                ).onSuccess {
                    updateState { copy(todoBottomSheetState = Async.Success(Unit), showTodoBottomSheet = false) }
                    getTodos()
                    sendEffect(TodoSideEffect.ShowSnackbar("할 일이 수정되었습니다."))
                }.onFailure { throwable ->
                    updateState { copy(todoBottomSheetState = Async.Init) }
                    val message = when (throwable) {
                        is TodoError.IdNotFound -> {
                            getTodos()
                            "이미 삭제된 할 일이에요."
                        }

                        is TodoError.TitleEmpty -> "할 일을 입력해주세요."
                        is TodoError.CategoryEmpty -> "카테고리를 선택해주세요."
                        is TodoError.ColorEmpty -> "색상을 선택해주세요."
                        is TodoError.CategoryInvalid -> {
                            getTodoCategories()
                            "유효하지 않은 카테고리예요. 다시 선택해주세요."
                        }

                        is ApiError -> throwable.snackbarMsg()
                        else -> ""
                    }
                    sendEffect(TodoSideEffect.ShowSnackbar(message))
                }
            }
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
            if (uiState.value.scheduleBottomSheetState is Async.Loading) return

            val inputs = uiState.value.editingSchedule
            val category = inputs.category?.request

            if (!inputs.isBtnEnabled || category == null) return

            updateState { copy(scheduleBottomSheetState = Async.Loading()) }

            viewModelScope.launch {
                scheduleRepository.createSchedule(
                    title = inputs.title,
                    category = category,
                    scheduleDate = inputs.date.toDashedDate(),
                )
                    .onSuccess {
                        updateState {
                            copy(
                                scheduleBottomSheetState = Async.Success(Unit),
                                showScheduleBottomSheet = false,
                            )
                        }
                        sendEffect(TodoSideEffect.ShowSnackbar("다가오는 일정이 추가되었습니다."))
                        getSchedules()
                    }
                    .onFailure { throwable ->
                        updateState { copy(scheduleBottomSheetState = Async.Init) }
                        val message = when (throwable) {
                            is ScheduleError.TitleEmpty -> "일정 제목을 입력해주세요."
                            is ScheduleError.CategoryEmpty -> "카테고리를 선택해주세요."
                            is ScheduleError.DateEmpty -> "날짜를 입력해주세요."
                            is ApiError -> throwable.snackbarMsg()
                            else -> return@launch
                        }
                        sendEffect(TodoSideEffect.ShowSnackbar(message))
                    }
            }
        }

        private fun editSchedule() {
            if (uiState.value.scheduleBottomSheetState is Async.Loading) return

            val inputs = uiState.value.editingSchedule
            val scheduleId = inputs.scheduleId
            val category = inputs.category?.request

            if (!inputs.isBtnEnabled || scheduleId == null || category == null) return

            updateState { copy(scheduleBottomSheetState = Async.Loading()) }

            viewModelScope.launch {
                scheduleRepository.updateSchedule(
                    scheduleId = scheduleId,
                    title = inputs.title,
                    category = category,
                    scheduleDate = inputs.date.toDashedDate(),
                )
                    .onSuccess {
                        updateState {
                            copy(
                                scheduleBottomSheetState = Async.Success(Unit),
                                showScheduleBottomSheet = false,
                            )
                        }
                        sendEffect(TodoSideEffect.ShowSnackbar("다가오는 일정이 수정되었습니다."))
                        getSchedules()
                    }
                    .onFailure { throwable ->
                        val message = when (throwable) {
                            is ScheduleError.ScheduleNotFound -> {
                                updateState { copy(scheduleBottomSheetState = Async.Success(Unit)) }
                                getSchedules()
                                "이미 삭제된 일정입니다."
                            }

                            is ApiError -> {
                                updateState { copy(scheduleBottomSheetState = Async.Init) }
                                throwable.snackbarMsg()
                            }

                            else -> return@launch
                        }
                        sendEffect(TodoSideEffect.ShowSnackbar(message))
                    }
            }
        }
    }
