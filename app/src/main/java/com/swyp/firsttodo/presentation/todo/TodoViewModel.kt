package com.swyp.firsttodo.presentation.todo

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.swyp.firsttodo.core.auth.manager.SessionManager
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.core.common.extension.snackbarMsg
import com.swyp.firsttodo.core.designsystem.theme.LabelColor
import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.domain.error.ScheduleError
import com.swyp.firsttodo.domain.error.StickerError
import com.swyp.firsttodo.domain.error.TodoError
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.model.ScheduleCategory
import com.swyp.firsttodo.domain.model.todo.TodoCategoryModel
import com.swyp.firsttodo.domain.repository.ScheduleRepository
import com.swyp.firsttodo.domain.repository.StickerRepository
import com.swyp.firsttodo.domain.repository.TodoRepository
import com.swyp.firsttodo.presentation.common.component.DeleteDialogType
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
        private val stickerRepository: StickerRepository,
    ) : BaseViewModel<TodoUiState, TodoSideEffect>(TodoUiState()) {
        val todoFieldState = TextFieldState()
        val scheduleTitleFieldState = TextFieldState()
        val scheduleDateFieldState = TextFieldState()

        private var lastBackPressedTime = 0L

        init {
            val role: Role = when (sessionManager.sessionState.value.userType) {
                Role.PARENT.request -> Role.PARENT
                else -> Role.CHILD
            }

            updateState { copy(role = role) }

            viewModelScope.launch {
                getTodoCategories()
                getTodos()
            }
            getWeeklyStickers()
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

        fun onBack() {
            val now = System.currentTimeMillis()
            if (now - lastBackPressedTime < 2000L) {
                sendEffect(TodoSideEffect.FinishApp)
            } else {
                lastBackPressedTime = now
                sendEffect(TodoSideEffect.ShowSnackbar("한 번 더 '뒤로가기'하면 앱이 종료됩니다."))
            }
        }

        private fun getWeeklyStickers() {
            val weekOffset = currentState.weekOffset
            if (weekOffset !in -52..52) return

            updateState { copy(weeklyStickers = Async.Loading(weeklyStickers.getDataOrNull())) }

            viewModelScope.launch {
                stickerRepository.getWeeklyStickers(weekOffset)
                    .onSuccess {
                        updateState {
                            copy(
                                weekOffset = it.weekOffset,
                                weeklyStickers = Async.Success(it),
                            )
                        }
                    }
                    .onFailure { throwable ->
                        if (throwable is StickerError.WeekOffsetInvalid) {
                            sendEffect(TodoSideEffect.ShowSnackbar("주간 스티커 목록을 불러올 수 없어, 초기화합니다."))
                            if (weekOffset != 0) {
                                updateState { copy(weekOffset = 0) }
                                getWeeklyStickers()
                                return@launch
                            }
                        }

                        if (throwable is ApiError) {
                            sendEffect(TodoSideEffect.ShowSnackbar(throwable.snackbarMsg()))
                        }

                        val prevData = currentState.weeklyStickers.getDataOrNull()
                        if (prevData != null) {
                            updateState { copy(weeklyStickers = Async.Success(prevData), weekOffset = weekOffset) }
                        } else {
                            updateState { copy(weeklyStickers = Async.Init, weekOffset = weekOffset) }
                        }
                    }
            }
        }

        fun onCalenderPrevClick() {
            if (currentState.weekOffset <= -52) {
                sendEffect(TodoSideEffect.ShowSnackbar("이전 주차는 확인할 수 없습니다."))
                return
            }

            updateState { copy(weekOffset = this.weekOffset - 1) }
            getWeeklyStickers()
        }

        fun onCalenderNextClick() {
            if (currentState.weekOffset >= 52) {
                sendEffect(TodoSideEffect.ShowSnackbar("이후 주차는 확인할 수 없습니다."))
                return
            }

            updateState { copy(weekOffset = this.weekOffset + 1) }
            getWeeklyStickers()
        }

        suspend fun getTodoCategories() =
            todoRepository.getTodoCategories()
                .onSuccess { categories ->
                    updateState { copy(categories = categories) }
                }
                .onFailure {
                    if (it is ApiError) sendEffect(TodoSideEffect.ShowSnackbar(it.snackbarMsg()))
                }

        suspend fun getTodos() {
            todoRepository.getTodos()
                .onSuccess { data ->
                    val categories = currentState.categories

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
                            progressPercent = Async.Success(data.progressPercent),
                        )
                    }
                }
                .onFailure { throwable ->
                    val prevData = currentState.todos.getDataOrNull()
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

        fun getSchedules() {
            viewModelScope.launch {
                scheduleRepository.getSchedules()
                    .onSuccess { list ->
                        val newSchedules = list.map { model ->
                            ScheduleUiModel(
                                scheduleId = model.scheduleId,
                                dDay = model.dDay,
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
                        val prevData = currentState.schedules.getDataOrNull()
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
                    getWeeklyStickers()
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
            val sheetType = when (currentState.role) {
                Role.PARENT -> TodoBottomSheetType.PARENT_CREATE
                else -> TodoBottomSheetType.CHILD_CREATE
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
            val sheetType = when (currentState.role) {
                Role.PARENT -> TodoBottomSheetType.PARENT_EDIT
                else -> TodoBottomSheetType.CHILD_EDIT
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
                        title = todoUiModel.title,
                        category = todoUiModel.category,
                        labelColor = todoUiModel.labelColor,
                        originalTitle = todoUiModel.title,
                        originalCategory = todoUiModel.category,
                        originalLabelColor = todoUiModel.labelColor,
                    ),
                )
            }
        }

        fun openScheduleCreateBottomSheet() {
            val sheetType = when (currentState.role) {
                Role.PARENT -> ScheduleBottomSheetType.PARENT_CREATE
                else -> ScheduleBottomSheetType.CHILD_CREATE
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
            val sheetType = when (currentState.role) {
                Role.PARENT -> ScheduleBottomSheetType.PARENT_EDIT
                else -> ScheduleBottomSheetType.CHILD_EDIT
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
                        title = scheduleUiModel.title,
                        date = scheduleUiModel.rawDate,
                        category = scheduleUiModel.category,
                        originalTitle = scheduleUiModel.title,
                        originalDate = scheduleUiModel.rawDate,
                        originalCategory = scheduleUiModel.category,
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
            updateState {
                copy(
                    delRequestedId = todoUiModel.todoId,
                    delRequestedType = DeleteDialogType.Todo,
                    deleteState = Async.Init,
                )
            }
        }

        fun openScheduleDialog(scheduleUiModel: ScheduleUiModel) {
            updateState {
                copy(
                    delRequestedId = scheduleUiModel.scheduleId,
                    delRequestedType = DeleteDialogType.Schedule,
                    deleteState = Async.Init,
                )
            }
        }

        fun onDeleteCancel() {
            updateState { copy(delRequestedId = null) }
        }

        fun onDeleteConfirm() {
            if (currentState.deleteState is Async.Loading) return
            when (currentState.delRequestedType) {
                DeleteDialogType.Todo -> deleteTodo()
                DeleteDialogType.Schedule -> deleteSchedule()
                else -> Unit
            }
        }

        private fun deleteTodo() {
            val todoId = currentState.delRequestedId ?: return

            updateState { copy(deleteState = Async.Loading()) }

            viewModelScope.launch {
                todoRepository.deleteTodo(todoId)
                    .onSuccess {
                        updateState { copy(delRequestedId = null, deleteState = Async.Success(Unit)) }
                        sendEffect(TodoSideEffect.ShowSnackbar("할 일이 삭제되었습니다."))
                        getTodos()
                    }
                    .onFailure { throwable ->
                        val message = when (throwable) {
                            is TodoError.IdNotFound -> {
                                getTodos()
                                updateState {
                                    copy(
                                        delRequestedId = null,
                                        deleteState = Async.Success(Unit),
                                    )
                                }
                                "이미 삭제된 할 일 입니다."
                            }

                            is ApiError -> {
                                updateState { copy(deleteState = Async.Init) }
                                throwable.snackbarMsg()
                            }

                            else -> {
                                updateState { copy(deleteState = Async.Init) }
                                return@onFailure
                            }
                        }

                        sendEffect(TodoSideEffect.ShowSnackbar(message))
                    }
            }
        }

        private fun deleteSchedule() {
            val scheduleId = currentState.delRequestedId ?: return

            updateState { copy(deleteState = Async.Loading()) }

            viewModelScope.launch {
                scheduleRepository.deleteSchedule(scheduleId)
                    .onSuccess {
                        updateState { copy(delRequestedId = null, deleteState = Async.Success(Unit)) }
                        sendEffect(TodoSideEffect.ShowSnackbar("다가오는 일정이 삭제되었습니다."))
                        getSchedules()
                    }
                    .onFailure { throwable ->
                        val message = when (throwable) {
                            is ScheduleError.ScheduleNotFound -> {
                                updateState {
                                    copy(
                                        delRequestedId = null,
                                        deleteState = Async.Success(Unit),
                                    )
                                }
                                getSchedules()
                                "이미 삭제된 일정입니다."
                            }

                            is ApiError -> {
                                updateState { copy(deleteState = Async.Init) }
                                throwable.snackbarMsg()
                            }

                            else -> {
                                updateState { copy(deleteState = Async.Init) }
                                return@launch
                            }
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
            when (currentState.todoBottomSheetType) {
                TodoBottomSheetType.CHILD_CREATE -> createTodo()
                TodoBottomSheetType.CHILD_EDIT -> editTodo()
                TodoBottomSheetType.PARENT_CREATE -> createTodo()
                TodoBottomSheetType.PARENT_EDIT -> editTodo()
            }
        }

        private fun createTodo() {
            if (currentState.todoBottomSheetState is Async.Loading) return
            if (!currentState.editingTodo.isBtnEnabled) return

            updateState { copy(todoBottomSheetState = Async.Loading()) }
            viewModelScope.launch {
                val inputs = currentState.editingTodo
                val category = inputs.category?.name ?: return@launch
                val color = inputs.labelColor?.toTodoColor() ?: return@launch

                todoRepository.createTodo(
                    title = inputs.title,
                    category = category,
                    color = color,
                ).onSuccess {
                    updateState {
                        copy(
                            todoBottomSheetState = Async.Success(Unit),
                            showTodoBottomSheet = false,
                        )
                    }
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
            if (currentState.todoBottomSheetState is Async.Loading) return
            if (!currentState.editingTodo.isBtnEnabled) return

            updateState { copy(todoBottomSheetState = Async.Loading()) }
            viewModelScope.launch {
                val inputs = currentState.editingTodo
                val todoId = inputs.todoId ?: return@launch
                val category = inputs.category?.name ?: return@launch
                val color = inputs.labelColor?.toTodoColor() ?: return@launch

                todoRepository.editTodo(
                    todoId = todoId,
                    title = inputs.title,
                    category = category,
                    color = color,
                ).onSuccess {
                    updateState {
                        copy(
                            todoBottomSheetState = Async.Success(Unit),
                            showTodoBottomSheet = false,
                        )
                    }
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
            when (currentState.scheduleBottomSheetType) {
                ScheduleBottomSheetType.CHILD_CREATE -> createSchedule()
                ScheduleBottomSheetType.CHILD_EDIT -> editSchedule()
                ScheduleBottomSheetType.PARENT_CREATE -> createSchedule()
                ScheduleBottomSheetType.PARENT_EDIT -> editSchedule()
            }
        }

        private fun createSchedule() {
            if (currentState.scheduleBottomSheetState is Async.Loading) return

            val inputs = currentState.editingSchedule
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
            if (currentState.scheduleBottomSheetState is Async.Loading) return

            val inputs = currentState.editingSchedule
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

                            else -> {
                                updateState { copy(scheduleBottomSheetState = Async.Init) }
                                return@launch
                            }
                        }
                        sendEffect(TodoSideEffect.ShowSnackbar(message))
                    }
            }
        }
    }
