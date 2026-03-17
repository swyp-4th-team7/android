package com.swyp.firsttodo.presentation.todo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.heightForScreenPercentage
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun TodoBanner(
    remainTodo: Async<Int>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightForScreenPercentage(132.dp)
            .background(Color(0xFFFFFAC9)),
    ) {
        val text = remember(remainTodo) {
            when (remainTodo) {
                is Async.Success -> "남은 할 일 : ${remainTodo.data}개"
                else -> ""
            }
        }

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_fighting_bubble),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = screenWidthDp(24.dp), top = screenHeightDp(12.dp)),
            tint = Color.Unspecified,
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = screenWidthDp(24.dp), bottom = screenHeightDp(24.dp)),
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .widthIn(screenWidthDp(112.dp))
                    .heightIn(screenHeightDp(24.dp))
                    .background(
                        color = HaebomTheme.colors.white,
                        shape = RoundedCornerShape(4.dp),
                    )
                    .padding(horizontal = 6.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                color = HaebomTheme.colors.gray600,
                textAlign = TextAlign.Center,
                style = HaebomTheme.typo.caption,
            )
        }
    }
}

private class TodoBannerPreviewProvider : PreviewParameterProvider<Async<Int>> {
    override val values = sequenceOf(Async.Success(3), Async.Success(9999999), Async.Init)
}

@Preview(widthDp = 360)
@Composable
private fun TodoBannerPreview(
    @PreviewParameter(TodoBannerPreviewProvider::class) remainTodo: Async<Int>,
) {
    HaebomTheme {
        TodoBanner(
            remainTodo = remainTodo,
        )
    }
}
