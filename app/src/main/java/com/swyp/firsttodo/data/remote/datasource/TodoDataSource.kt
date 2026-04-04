package com.swyp.firsttodo.data.remote.datasource

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.request.todo.TodoCreateRequestDto
import com.swyp.firsttodo.data.remote.dto.request.todo.TodoEditRequestDto
import com.swyp.firsttodo.data.remote.dto.response.todo.TodoCategoryListDto
import com.swyp.firsttodo.data.remote.dto.response.todo.TodoListResponseDto
import com.swyp.firsttodo.data.remote.dto.response.todo.TodoResponseDto

interface TodoDataSource {
    suspend fun postTodo(request: TodoCreateRequestDto): BaseResponse<TodoResponseDto>

    suspend fun patchTodo(
        todoId: Long,
        request: TodoEditRequestDto,
    ): BaseResponse<Unit>

    suspend fun deleteTodo(todoId: Long): BaseResponse<Unit>

    suspend fun getTodos(): BaseResponse<TodoListResponseDto>

    suspend fun getTodoCategories(): BaseResponse<TodoCategoryListDto>
}
