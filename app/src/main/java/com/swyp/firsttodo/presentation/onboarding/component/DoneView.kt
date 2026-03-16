package com.swyp.firsttodo.presentation.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DoneView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(200.dp)
            .background(Color.Yellow),
    )
}
