package com.swyp.firsttodo.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun HaebomCardLayout(
    title: String,
    modifier: Modifier = Modifier,
    onPlusClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .background(
                color = Color(0xFFF3F3F3),
                shape = RoundedCornerShape(8.dp),
            )
            .padding(top = 4.dp, bottom = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(top = 16.dp, bottom = 12.dp),
                color = HaebomTheme.colors.black,
                style = HaebomTheme.typo.section,
            )

            if (onPlusClick != null) {
                Icon(
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = null,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .size(24.dp)
                        .noRippleClickable(onPlusClick),
                    tint = Color(0xFF79716B),
                )
            }
        }

        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun HaebomCardLayoutPreview() {
    HaebomTheme {
        HaebomCardLayout(
            title = "오늘의 할 일",
            onPlusClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
        ) {
            Box(
                modifier = Modifier.size(100.dp),
            )
        }
    }
}
