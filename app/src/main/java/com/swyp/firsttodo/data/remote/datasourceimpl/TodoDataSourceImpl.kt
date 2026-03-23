package com.swyp.firsttodo.data.remote.datasourceimpl

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.datasource.TodoDataSource
import com.swyp.firsttodo.data.remote.dto.request.todo.TodoCreateRequestDto
import com.swyp.firsttodo.data.remote.dto.request.todo.TodoEditRequestDto
import com.swyp.firsttodo.data.remote.dto.response.todo.TodoCategoryListDto
import com.swyp.firsttodo.data.remote.dto.response.todo.TodoListResponseDto
import com.swyp.firsttodo.data.remote.dto.response.todo.TodoResponseDto
import com.swyp.firsttodo.data.remote.service.TodoService
import javax.inject.Inject

class TodoDataSourceImpl
    @Inject
    constructor(
        private val todoService: TodoService,
    ) : TodoDataSource {
        override suspend fun postTodo(request: TodoCreateRequestDto): BaseResponse<TodoResponseDto> =
            todoService.postTodo(request)

        override suspend fun patchTodo(
            todoId: Long,
            request: TodoEditRequestDto,
        ): BaseResponse<Unit> = todoService.patchTodo(todoId, request)

        override suspend fun deleteTodo(todoId: Long): BaseResponse<Unit> = todoService.deleteTodo(todoId)

        override suspend fun getTodos(): BaseResponse<TodoListResponseDto> = todoService.getTodos()

        override suspend fun getTodoCategories(): BaseResponse<TodoCategoryListDto> = todoService.getTodoCategories()
    }
