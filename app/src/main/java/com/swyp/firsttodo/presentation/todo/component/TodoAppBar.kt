package com.swyp.firsttodo.presentation.todo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun TodoAppBar(
    onMenuClick: () -> Unit,
    onAlarmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(52.dp)
            .background(HaebomTheme.colors.yellow400),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_menu),
            contentDescription = null,
            modifier = Modifier
                .noRippleClickable(onMenuClick)
                .padding(all = 16.dp),
            tint = Color.Unspecified,
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_alarm),
            contentDescription = null,
            modifier = Modifier
                .noRippleClickable(onAlarmClick)
                .padding(all = 16.dp),
            tint = Color.Unspecified,
        )
    }
}

@Preview
@Composable
private fun TodoAppBarPreview() {
    HaebomTheme {
        TodoAppBar(
            onMenuClick = {},
            onAlarmClick = {},
        )
    }
}
