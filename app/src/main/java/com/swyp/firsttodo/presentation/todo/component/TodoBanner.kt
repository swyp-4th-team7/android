package com.swyp.firsttodo.presentation.todo.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.heightForScreenPercentage
import com.swyp.firsttodo.core.common.extension.widthForScreenPercentage
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.LabelColor

@Composable
fun TodoBanner(
    @DrawableRes imageRes: Int?,
    bubbleText: String?,
    remainTodo: Async<Int>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .heightForScreenPercentage(132.dp)
            .background(LabelColor.YELLOW.completedBackground)
            .clipToBounds(),
    ) {
        val text = remember(remainTodo) {
            when (remainTodo) {
                is Async.Success -> "남은 할 일 : ${remainTodo.data}개"
                else -> ""
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .widthForScreenPercentage(360.dp)
                .align(Alignment.Center),
        ) {
            imageRes?.let {
                Image(
                    painter = painterResource(it),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .wrapContentSize(align = Alignment.TopEnd, unbounded = true)
                        .size(screenHeightDp(176.dp)),
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = screenWidthDp(10.dp), y = screenHeightDp(12.dp)),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_speech_bubble),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center),
                    tint = Color.Unspecified,
                )

                bubbleText?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .padding(bottom = 7.dp)
                            .align(Alignment.Center),
                        color = HaebomTheme.colors.white,
                        style = HaebomTheme.typo.card,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = screenWidthDp(24.dp), y = screenHeightDp((-24).dp)),
            ) {
                Text(
                    text = text,
                    modifier = Modifier
                        .widthIn(112.dp)
                        .heightIn(24.dp)
                        .background(
                            color = HaebomTheme.colors.white,
                            shape = RoundedCornerShape(4.dp),
                        )
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .wrapContentHeight(Alignment.CenterVertically),
                    color = HaebomTheme.colors.gray600,
                    textAlign = TextAlign.Center,
                    style = HaebomTheme.typo.caption,
                )
            }
        }
    }
}

private data class TodoBannerPreviewModel(
    val progressPercent: Async<Int>,
    @param:DrawableRes val imageRes: Int?,
    val bubbleText: String?,
)

private class TodoBannerPreviewProvider : PreviewParameterProvider<TodoBannerPreviewModel> {
    override val values = sequenceOf(
        TodoBannerPreviewModel(
            progressPercent = Async.Success(100),
            imageRes = R.drawable.img_todo_perfect_176,
            bubbleText = "완전 대단해!!",
        ),
        TodoBannerPreviewModel(
            progressPercent = Async.Success(70),
            imageRes = R.drawable.img_todo_cheer_176,
            bubbleText = "잘하고 있어! 힘내!!",
        ),
        TodoBannerPreviewModel(
            progressPercent = Async.Success(10),
            imageRes = R.drawable.img_todo_nagging_176,
            bubbleText = "뭐하고 있어! 빨리 해야해!!",
        ),
    )
}

@Preview(widthDp = 500, showBackground = true)
@Composable
private fun TodoBannerPreview(
    @PreviewParameter(TodoBannerPreviewProvider::class)
    model: TodoBannerPreviewModel,
) {
    HaebomTheme {
        TodoBanner(
            imageRes = model.imageRes,
            bubbleText = model.bubbleText,
            remainTodo = model.progressPercent,
        )
    }
}
