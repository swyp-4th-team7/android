package com.swyp.firsttodo.presentation.habit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun HabitListEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = HaebomTheme.colors.gray50,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 12.dp)
            .padding(top = 20.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = "내 습관 모음집",
            color = HaebomTheme.colors.black,
            style = HaebomTheme.typo.section,
        )

        Text(
            text = "습관 만들기를 눌러 습관을 추가하세요!",
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = HaebomTheme.colors.white,
                    shape = RoundedCornerShape(4.dp),
                )
                .heightIn(56.dp)
                .padding(all = 16.dp)
                .wrapContentHeight(Alignment.CenterVertically),
            color = HaebomTheme.colors.gray400,
            textAlign = TextAlign.Center,
            style = HaebomTheme.typo.subtitle,
        )
    }
}

@Preview
@Composable
private fun HabitListEmptyPreview() {
    HaebomTheme {
        HabitListEmpty()
    }
}
