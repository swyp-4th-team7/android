package com.swyp.firsttodo.presentation.habit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.LabelColor

@Composable
fun HabitMainBanner(
    description: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(screenHeightDp(132.dp))
            .background(LabelColor.YELLOW.completedBackground)
            .padding(horizontal = screenWidthDp(16.dp))
            .padding(top = screenHeightDp(28.dp)),
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopStart),
            verticalArrangement = Arrangement.spacedBy(screenHeightDp(8.dp)),
        ) {
            Text(
                text = "좋은 습관을 만들어 볼까요?",
                color = HaebomTheme.colors.black,
                style = HaebomTheme.typo.screen,
            )

            Text(
                text = description,
                color = HaebomTheme.colors.gray400,
                style = HaebomTheme.typo.description,
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .noRippleClickable(onButtonClick)
                .padding(bottom = screenHeightDp(12.dp)),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Text(
                text = "습관 만들기",
                modifier = Modifier
                    .background(
                        color = HaebomTheme.colors.orange500,
                        shape = RoundedCornerShape(18.dp),
                    )
                    .padding(horizontal = 18.dp, vertical = 6.dp),
                color = HaebomTheme.colors.white,
                style = HaebomTheme.typo.buttonM,
            )
        }
    }
}

@Preview
@Composable
private fun HabitMainBannerPreview() {
    HaebomTheme {
        HabitMainBanner(
            description = "가족과 보상을 정하고\n재미있는 습관을 실천해요!",
            onButtonClick = {},
        )
    }
}
