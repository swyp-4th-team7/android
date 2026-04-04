package com.swyp.firsttodo.presentation.growth.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun CharacterBubble(
    starCount: Int,
    bubbleImageRes: Int,
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(bubbleImageRes),
            contentDescription = null,
        )

        if (starCount == 0) {
            Text(
                text = text,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 12.dp),
                color = HaebomTheme.colors.gray400,
                style = HaebomTheme.typo.screen,
            )
        } else {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = text,
                    color = HaebomTheme.colors.black,
                    style = HaebomTheme.typo.screen,
                )

                Row {
                    repeat(3) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_star_40),
                            contentDescription = null,
                            tint = if (it < starCount) HaebomTheme.colors.yellow400 else HaebomTheme.colors.gray100,
                        )
                    }
                }
            }
        }
    }
}

private class CharacterBubblePreviewProvider : PreviewParameterProvider<Int> {
    override val values = sequenceOf(0, 1, 2, 3)
}

@Preview(showBackground = true)
@Composable
private fun CharacterBubblePreview(
    @PreviewParameter(CharacterBubblePreviewProvider::class) starCount: Int,
) {
    val bubbleImageRes = when (starCount) {
        0 -> R.drawable.img_growth_speech_bubble_monochrome
        else -> R.drawable.img_growth_speech_bubble
    }
    val text = when (starCount) {
        0 -> "아직 기록이 없어요."
        1 -> "조금만 더 힘내볼까요?"
        2 -> "지금 잘하고 있어요!"
        else -> "너무 대단해요!"
    }

    HaebomTheme {
        CharacterBubble(
            starCount = starCount,
            bubbleImageRes = bubbleImageRes,
            text = text,
        )
    }
}
