package com.swyp.firsttodo.presentation.hamburger.family.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun FamilyHeader(modifier: Modifier = Modifier) {
    Text(
        text = "가족 실천 현황",
        color = HaebomTheme.colors.black,
        style = HaebomTheme.typo.screen,
    )
}
