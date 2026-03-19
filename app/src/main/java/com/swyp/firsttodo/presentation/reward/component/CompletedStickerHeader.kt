package com.swyp.firsttodo.presentation.reward.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun CompletedStickerHeader(
    completedSticker: Async<Int>,
    modifier: Modifier = Modifier,
) {
    val count = when (completedSticker) {
        is Async.Success -> completedSticker.data
        else -> "  "
    }

    Row(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .background(
                color = HaebomTheme.colors.yellow50,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(vertical = 4.dp),
    ) {
        Text(
            text = "완료 스티커",
            modifier = Modifier.weight(1f),
            color = HaebomTheme.colors.orange400,
            textAlign = TextAlign.Center,
            style = HaebomTheme.typo.card,
        )

        VerticalDivider(
            modifier = Modifier.clip(CircleShape),
            thickness = 0.8.dp,
            color = HaebomTheme.colors.yellow300,
        )

        Text(
            text = "${count}개",
            modifier = Modifier.weight(1f),
            color = HaebomTheme.colors.orange400,
            textAlign = TextAlign.Center,
            style = HaebomTheme.typo.card,
        )
    }
}

private class CompletedStickerHeaderPreviewProvider : PreviewParameterProvider<Async<Int>> {
    override val values = sequenceOf(Async.Success(1), Async.Success(30), Async.Success(0), Async.Init)
}

@Preview
@Composable
private fun CompletedStickerHeaderPreview(
    @PreviewParameter(CompletedStickerHeaderPreviewProvider::class) completedSticker: Async<Int>,
) {
    HaebomTheme {
        CompletedStickerHeader(completedSticker)
    }
}
