package com.swyp.firsttodo.presentation.reward.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun RewardMangeEmptyView(
    onCreateHabitBtnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(screenHeightDp(356.dp))
            .border(
                width = 1.5.dp,
                shape = RoundedCornerShape(4.dp),
                color = HaebomTheme.colors.gray100,
            )
            .wrapContentHeight(Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "아직 습관 보상이 없어요.",
            modifier = Modifier.padding(bottom = screenHeightDp(8.dp)),
            color = HaebomTheme.colors.gray400,
            style = HaebomTheme.typo.card,
        )

        Text(
            text = "아직 습관 보상이 없어요.",
            modifier = Modifier.padding(bottom = screenHeightDp(32.dp)),
            color = HaebomTheme.colors.gray300,
            style = HaebomTheme.typo.description,
        )

        Text(
            text = "습관 만들기",
            modifier = Modifier
                .clickable(onClick = onCreateHabitBtnClick)
                .sizeIn(124.dp, 36.dp)
                .background(
                    color = HaebomTheme.colors.orange500,
                    shape = RoundedCornerShape(18.dp),
                )
                .padding(all = 8.dp)
                .wrapContentHeight(Alignment.CenterVertically),
            color = HaebomTheme.colors.white,
            textAlign = TextAlign.Center,
            style = HaebomTheme.typo.buttonM,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RewardMangeEmptyViewPreview() {
    HaebomTheme {
        RewardMangeEmptyView(
            onCreateHabitBtnClick = {},
            modifier = Modifier.padding(all = 16.dp),
        )
    }
}
