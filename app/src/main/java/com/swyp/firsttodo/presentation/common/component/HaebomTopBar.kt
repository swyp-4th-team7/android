package com.swyp.firsttodo.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun HaebomTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = HaebomTheme.colors

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.white)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                val yOffset = size.height - strokeWidth / 2f

                drawLine(
                    color = colors.gray50,
                    start = Offset(0f, yOffset),
                    end = Offset(size.width, yOffset),
                    strokeWidth = strokeWidth,
                )
            },
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
            contentDescription = null,
            modifier = Modifier
                .noRippleClickable(onBackClick)
                .padding(all = 16.dp)
                .size(24.dp)
                .align(Alignment.CenterStart),
            tint = colors.gray400,
        )

        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            color = colors.gray400,
            style = HaebomTheme.typo.screen,
        )
    }
}

@Preview
@Composable
private fun HaebomTopBarPreview() {
    HaebomTheme {
        HaebomTopBar(
            title = "역할 선택",
            onBackClick = {},
        )
    }
}
