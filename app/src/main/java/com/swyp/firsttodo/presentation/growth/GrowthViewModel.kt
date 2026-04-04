package com.swyp.firsttodo.presentation.growth

import com.swyp.firsttodo.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GrowthViewModel
    @Inject
    constructor() : BaseViewModel<GrowthUiState, GrowthSideEffect>(GrowthUiState())
