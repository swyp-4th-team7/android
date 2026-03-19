package com.swyp.firsttodo.presentation.habit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun HabitDetailTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .background(HaebomTheme.colors.yellow400)
            .statusBarsPadding()
            .wrapContentSize(Alignment.CenterStart)
            .noRippleClickable(onBackClick)
            .padding(all = 16.dp),
        tint = HaebomTheme.colors.orange800,
    )
}

@Preview
@Composable
private fun HabitDetailTopBarPreview() {
    HaebomTheme {
        HabitDetailTopBar(
            onBackClick = {},
        )
    }
}
