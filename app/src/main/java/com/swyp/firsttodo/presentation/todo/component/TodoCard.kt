package com.swyp.firsttodo.presentation.todo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
fun TodoCard(
    title: String,
    onPlusClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit),
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = HaebomTheme.colors.gray50,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(bottom = 12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .padding(start = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                color = HaebomTheme.colors.black,
                style = HaebomTheme.typo.section,
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_plus),
                contentDescription = null,
                modifier = Modifier
                    .noRippleClickable(onPlusClick)
                    .padding(all = 12.dp),
                tint = Color.Unspecified,
            )
        }

        content()
    }
}

@Preview
@Composable
private fun TodoCardPreview() {
    HaebomTheme {
        TodoCard(
            title = "오늘의 할 일",
            onPlusClick = {},
        ) {
            Box(modifier = Modifier.size((100.dp)))
        }
    }
}
