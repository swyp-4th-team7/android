package com.swyp.firsttodo.presentation.todo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun TodoCardEmptyContent(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(56.dp)
            .background(
                color = HaebomTheme.colors.white,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(all = 16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = text,
            color = HaebomTheme.colors.gray400,
            style = HaebomTheme.typo.subtitle,
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_curved_arrow),
            contentDescription = null,
            tint = HaebomTheme.colors.gray400,
        )
    }
}

@Preview
@Composable
private fun TodoCardEmptyContentPreview() {
    TodoCardEmptyContent(
        text = "+를 눌러 오늘 할 일을 적어 주세요!",
    )
}
