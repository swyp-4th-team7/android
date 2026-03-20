package com.swyp.firsttodo.data.remote.service

import com.swyp.firsttodo.core.network.model.BaseResponse
import com.swyp.firsttodo.data.remote.dto.request.todo.TodoCreateRequestDto
import com.swyp.firsttodo.data.remote.dto.request.todo.TodoEditRequestDto
import com.swyp.firsttodo.data.remote.dto.response.todo.TodoCategoryListDto
import com.swyp.firsttodo.data.remote.dto.response.todo.TodoListResponseDto
import com.swyp.firsttodo.data.remote.dto.response.todo.TodoResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TodoService {
    @POST("/api/v1/todos")
    suspend fun postTodo(
        @Body request: TodoCreateRequestDto,
    ): BaseResponse<TodoResponseDto>

    @PATCH("/api/v1/todos/{todoId}")
    suspend fun patchTodo(
        @Path("todoId") todoId: Long,
        @Body request: TodoEditRequestDto,
    ): BaseResponse<Unit>

    @DELETE("/api/v1/todos/{todoId}")
    suspend fun deleteTodo(
        @Path("todoId") todoId: Long,
    ): BaseResponse<Unit>

    @GET("/api/v1/todos")
    suspend fun getTodos(): BaseResponse<TodoListResponseDto>

    @GET("/api/v1/todos/categories")
    suspend fun getTodoCategories(): BaseResponse<TodoCategoryListDto>
}
