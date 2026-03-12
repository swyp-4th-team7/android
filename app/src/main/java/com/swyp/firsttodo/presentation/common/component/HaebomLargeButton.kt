package com.swyp.firsttodo.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun HaebomLargeButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = HaebomTheme.colors

    val (textColor, backgroundColor) = remember(enabled, colors) {
        when (enabled) {
            true -> colors.white to colors.orange500
            false -> colors.orange300 to colors.orange25
        }
    }

    Text(
        text = text,
        modifier = modifier
            .noRippleClickable(
                onClick = onClick,
                enabled = enabled,
            )
            .heightIn(52.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 10.dp)
            .wrapContentHeight(Alignment.CenterVertically),
        color = textColor,
        textAlign = TextAlign.Center,
        style = HaebomTheme.typo.button,
    )
}

private class HaebomLargeButtonPreviewProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(true, false)
}

@Preview
@Composable
private fun HaebomLargeButtonPreview(
    @PreviewParameter(HaebomLargeButtonPreviewProvider::class) enabled: Boolean,
) {
    HaebomTheme {
        HaebomLargeButton(
            text = "다음으로",
            onClick = {},
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
