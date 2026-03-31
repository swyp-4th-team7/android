package com.swyp.firsttodo.presentation.reward.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun ParentRewardEmptyView(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(356.dp)
            .border(
                width = 1.dp,
                color = HaebomTheme.colors.gray100,
                shape = RoundedCornerShape(4.dp),
            )
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            color = HaebomTheme.colors.gray400,
            style = HaebomTheme.typo.buttonL,
        )

        Text(
            text = description,
            color = HaebomTheme.colors.gray400,
            style = HaebomTheme.typo.subtitle,
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun ParentRewardEmptyViewPreview() {
    HaebomTheme {
        ParentRewardEmptyView(
            title = "아직 관리할 스티커가 없습니다.",
            description = "자녀의 할 일을 만들어 주세요.",
            modifier = Modifier.padding(all = 16.dp),
        )
    }
}
