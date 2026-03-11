package com.swyp.firsttodo.presentation.todo

import com.swyp.firsttodo.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodoViewModel
    @Inject
    constructor() : BaseViewModel<TodoUiState, TodoSideEffect>(TodoUiState())
