package com.swyp.firsttodo.presentation.hamburger.share

import com.swyp.firsttodo.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareViewModel
    @Inject
    constructor() : BaseViewModel<ShareUiState, ShareSideEffect>(ShareUiState())
