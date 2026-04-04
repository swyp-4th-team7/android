package com.swyp.firsttodo.presentation.reward.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.dashedCircleBorder
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

private const val COLUMNS = 5

@Composable
fun StickerBoard(
    completedSticker: Async<Int>,
    modifier: Modifier = Modifier,
    boardSize: Int = 30,
) {
    val data = completedSticker.getDataOrNull()
    val rows = (boardSize + COLUMNS - 1) / COLUMNS

    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = HaebomTheme.colors.gray50,
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(
                    horizontal = screenWidthDp(20.dp),
                    vertical = screenHeightDp(32.dp),
                )
                .then(
                    if (completedSticker is Async.Empty) Modifier.blur(4.dp) else Modifier,
                ),
            verticalArrangement = Arrangement.spacedBy(screenHeightDp(40.dp)),
        ) {
            for (row in 0 until rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(screenWidthDp(12.dp)),
                ) {
                    for (col in 0 until COLUMNS) {
                        val index = row * COLUMNS + col

                        when {
                            index < boardSize -> Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f),
                            ) {
                                when (data) {
                                    null -> EmptySticker(index + 1)
                                    else -> if (index < data) Sticker() else EmptySticker(index + 1)
                                }
                            }

                            else -> Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        if (completedSticker is Async.Empty) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        color = HaebomTheme.colors.black.copy(alpha = 0.04f),
                        shape = RoundedCornerShape(8.dp),
                    ),
            )

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_sticker_clover),
                    contentDescription = null,
                    modifier = Modifier
                        .size(92.dp)
                        .padding(bottom = 24.dp),
                    colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }),
                )

                Text(
                    text = "아직 스티커가 없어요!",
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = HaebomTheme.colors.gray600,
                    style = HaebomTheme.typo.buttonL,
                )

                Text(
                    text = "오늘 할 일을 완료하면\n스티커를 받을 수 있어요.",
                    color = HaebomTheme.colors.gray400,
                    textAlign = TextAlign.Center,
                    style = HaebomTheme.typo.description,
                )
            }
        }
    }
}

@Composable
private fun EmptySticker(
    number: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = number.toString(),
        modifier = modifier
            .fillMaxSize()
            .background(
                color = HaebomTheme.colors.white,
                shape = CircleShape,
            )
            .dashedCircleBorder(
                color = HaebomTheme.colors.gray200,
                strokeWidth = 2.dp,
            )
            .wrapContentSize(Alignment.Center),
        color = HaebomTheme.colors.gray200,
        textAlign = TextAlign.Center,
        style = HaebomTheme.typo.screen,
    )
}

@Composable
private fun Sticker(
    modifier: Modifier = Modifier,
    @DrawableRes stickerRes: Int = R.drawable.img_sticker_clover,
) {
    Image(
        painter = painterResource(id = stickerRes),
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
    )
}

private class StickerBoardPreviewProvider : PreviewParameterProvider<Async<Int>> {
    override val values = sequenceOf(
        Async.Success(0),
        Async.Success(10),
        Async.Success(30),
        Async.Empty,
        Async.Init,
    )
}

@Preview(showBackground = true)
@Composable
private fun StickerBoardPreview(
    @PreviewParameter(StickerBoardPreviewProvider::class) completedSticker: Async<Int>,
) {
    HaebomTheme {
        StickerBoard(
            completedSticker = completedSticker,
            modifier = Modifier.padding(all = 16.dp),
        )
    }
}
