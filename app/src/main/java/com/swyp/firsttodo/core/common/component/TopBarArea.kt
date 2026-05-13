package com.swyp.firsttodo.core.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

const val MAIN_TOP_BAR_HEIGHT = 56

@Composable
fun TopBarArea(
    modifier: Modifier = Modifier,
    color: Color = HaebomTheme.colors.yellow400,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(MAIN_TOP_BAR_HEIGHT.dp)
            .background(color),
    )
}
