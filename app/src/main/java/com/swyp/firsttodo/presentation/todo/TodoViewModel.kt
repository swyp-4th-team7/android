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
import com.swyp.firsttodo.domain.model.todo.TodoCategoryModel
import com.swyp.firsttodo.domain.repository.StickerRepository
import com.swyp.firsttodo.domain.repository.TodoRepository
import com.swyp.firsttodo.domain.throwable.StickerError
import com.swyp.firsttodo.domain.throwable.TodoError
import com.swyp.firsttodo.presentation.common.component.DeleteDialogType
import com.swyp.firsttodo.presentation.common.extension.snackbarMsg
import com.swyp.firsttodo.presentation.todo.component.TodayTodoUiModel
import com.swyp.firsttodo.presentation.todo.component.TodoBottomSheetType
import com.swyp.firsttodo.presentation.todo.extension.toLabelColor
import com.swyp.firsttodo.presentation.todo.extension.toTodoColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel
    @Inject
    constructor(
        sessionManager: SessionManager,
        private val todoRepository: TodoRepository,
        private val stickerRepository: StickerRepository,
    ) : BaseViewModel<TodoUiState, TodoSideEffect>(TodoUiState()) {
        val todoFieldState = TextFieldState()
        private var lastBackPressedTime = 0L

        private val role: Role = when (sessionManager.sessionState.value.userType) {
            Role.PARENT.request -> Role.PARENT
            else -> Role.CHILD
        }

        init {
            viewModelScope.launch {
                getTodoCategories()
                getTodos()
            }
            getWeeklyStickers()

            viewModelScope.launch {
                snapshotFlow { todoFieldState.text.toString() }
                    .collect { updateState { copy(editingTodo = editingTodo.copy(title = it)) } }
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
            val weekOffset = uiState.value.weekOffset
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

                        val prevData = uiState.value.weeklyStickers.getDataOrNull()
                        if (prevData != null) {
                            updateState { copy(weeklyStickers = Async.Success(prevData), weekOffset = weekOffset) }
                        } else {
                            updateState { copy(weeklyStickers = Async.Init, weekOffset = weekOffset) }
                        }
                    }
            }
        }

        fun onCalenderPrevClick() {
            if (uiState.value.weekOffset <= -52) {
                sendEffect(TodoSideEffect.ShowSnackbar("이전 주차는 확인할 수 없습니다."))
                return
            }

            updateState { copy(weekOffset = this.weekOffset - 1) }
            getWeeklyStickers()
        }

        fun onCalenderNextClick() {
            if (uiState.value.weekOffset >= 52) {
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
            updateState { copy(todos = Async.Loading(this.todos.getDataOrNull())) }

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
                            progressPercent = Async.Success(data.progressPercent),
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
                        title = todoUiModel.title,
                        category = todoUiModel.category,
                        labelColor = todoUiModel.labelColor,
                    ),
                )
            }
        }

        fun closeTodoBottomSheet() {
            updateState { copy(showTodoBottomSheet = false) }
        }

        private fun clearEditingTodo() {
            todoFieldState.clearText()
            updateState { copy(editingTodo = EditingTodo()) }
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

        fun onDeleteCancel() {
            updateState { copy(delRequestedId = null) }
        }

        fun onDeleteConfirm() {
            deleteTodo()
        }

        private fun deleteTodo() {
            val todoId = uiState.value.delRequestedId ?: return

            updateState { copy(deleteState = Async.Loading()) }

            viewModelScope.launch {
                todoRepository.deleteTodo(todoId)
                    .onSuccess {
                        getTodos()
                        updateState { copy(delRequestedId = null, deleteState = Async.Success(Unit)) }
                        sendEffect(TodoSideEffect.ShowSnackbar("할 일이 삭제되었습니다."))
                    }
                    .onFailure { throwable ->
                        val message = when (throwable) {
                            is TodoError.IdNotFound -> {
                                getTodos()
                                updateState { copy(delRequestedId = null, deleteState = Async.Success(Unit)) }
                                "이미 삭제된 할 일 입니다."
                            }

                            is ApiError -> {
                                updateState { copy(deleteState = Async.Init) }
                                throwable.snackbarMsg()
                            }

                            else -> return@onFailure
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
    }
