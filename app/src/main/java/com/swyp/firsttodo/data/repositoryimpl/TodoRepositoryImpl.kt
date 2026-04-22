package com.swyp.firsttodo.data.repositoryimpl

import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.core.network.util.ApiResponseHandler
import com.swyp.firsttodo.data.mapper.toModel
import com.swyp.firsttodo.data.remote.datasource.TodoDataSource
import com.swyp.firsttodo.data.remote.dto.request.todo.TodoCreateRequestDto
import com.swyp.firsttodo.data.remote.dto.request.todo.TodoEditRequestDto
import com.swyp.firsttodo.domain.error.TodoError
import com.swyp.firsttodo.domain.model.todo.TodoCategoryModel
import com.swyp.firsttodo.domain.model.todo.TodoColor
import com.swyp.firsttodo.domain.model.todo.TodoListModel
import com.swyp.firsttodo.domain.repository.TodoRepository
import javax.inject.Inject

class TodoRepositoryImpl
    @Inject
    constructor(
        private val apiResponseHandler: ApiResponseHandler,
        private val todoDataSource: TodoDataSource,
    ) : TodoRepository {
        override suspend fun createTodo(
            title: String,
            category: String,
            color: TodoColor,
        ): Result<Unit> =
            apiResponseHandler.safeApiCall {
                todoDataSource.postTodo(TodoCreateRequestDto(title, category, color.name))
            }.map { }
                .recoverCatching { e ->
                    throw if (e is ApiError.BadRequest) {
                        when (e.code) {
                            40009 -> TodoError.TitleEmpty(e.serverMsg)
                            40011 -> TodoError.CategoryEmpty(e.serverMsg)
                            40013 -> TodoError.ColorEmpty(e.serverMsg)
                            40018 -> TodoError.CategoryInvalid(e.serverMsg)
                            else -> e
                        }
                    } else {
                        e
                    }
                }

        override suspend fun editTodo(
            todoId: Long,
            title: String?,
            category: String?,
            color: TodoColor?,
            completed: Boolean?,
        ): Result<Unit> =
            apiResponseHandler.safeApiCall {
                todoDataSource.patchTodo(todoId, TodoEditRequestDto(title, category, color?.name, completed))
            }.recoverCatching { e ->
                throw when (e) {
                    is ApiError.NotFound -> TodoError.IdNotFound(e.serverMsg)
                    else -> e
                }
            }

        override suspend fun deleteTodo(todoId: Long): Result<Unit> =
            apiResponseHandler.safeApiCall {
                todoDataSource.deleteTodo(todoId)
            }.recoverCatching { e ->
                throw when (e) {
                    is ApiError.NotFound -> TodoError.IdNotFound(e.serverMsg)
                    else -> e
                }
            }

        override suspend fun getTodos(): Result<TodoListModel> =
            apiResponseHandler.safeApiCall {
                todoDataSource.getTodos()
            }.map { it.toModel() }

        override suspend fun getTodoCategories(): Result<List<TodoCategoryModel>> =
            apiResponseHandler.safeApiCall {
                todoDataSource.getTodoCategories()
            }.map { it.toModel() }
    }
