package com.swyp.firsttodo.presentation.habit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp.firsttodo.core.common.extension.toast
import com.swyp.firsttodo.core.common.util.HandleSideEffects
import com.swyp.firsttodo.core.designsystem.theme.HeabomTheme

@Composable
fun HabitRoute(
    modifier: Modifier = Modifier,
    viewModel: HabitViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            is HabitSideEffect.ShowToast -> context.toast(effect.message)
        }
    }

    HabitScreen(
        uiState = uiState,
        modifier = modifier,
    )
}

@Composable
fun HabitScreen(
    uiState: HabitUiState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "HABIT",
            modifier = Modifier,
            style = HeabomTheme.typo.description,
        )
    }
}
