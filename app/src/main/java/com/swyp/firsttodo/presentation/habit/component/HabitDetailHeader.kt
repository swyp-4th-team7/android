package com.swyp.firsttodo.presentation.habit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun HabitDetailHeader(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = title,
            color = HaebomTheme.colors.black,
            style = HaebomTheme.typo.week,
        )

        Text(
            text = description,
            color = HaebomTheme.colors.gray400,
            style = HaebomTheme.typo.description,
        )
    }
}
