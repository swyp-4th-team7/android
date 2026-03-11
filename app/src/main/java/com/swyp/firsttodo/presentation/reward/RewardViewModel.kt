package com.swyp.firsttodo.presentation.reward

import com.swyp.firsttodo.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RewardViewModel
    @Inject
    constructor() : BaseViewModel<RewardUiState, RewardSideEffect>(RewardUiState())
