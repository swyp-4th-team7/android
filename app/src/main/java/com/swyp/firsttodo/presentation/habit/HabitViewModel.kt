package com.swyp.firsttodo.presentation.habit

import com.swyp.firsttodo.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HabitViewModel
    @Inject
    constructor() : BaseViewModel<HabitUiState, HabitSideEffect>(HabitUiState())
