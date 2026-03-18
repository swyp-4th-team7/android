package com.swyp.firsttodo.presentation.main.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState

suspend fun SnackbarHostState.showHaebomSnackbar(
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short,
) = this.showSnackbar(
    HaebomSnackbarVisuals(
        message = message,
        duration = duration,
    ),
)
