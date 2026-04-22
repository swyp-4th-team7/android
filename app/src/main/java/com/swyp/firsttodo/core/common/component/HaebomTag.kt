package com.swyp.firsttodo.core.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
fun HaebomTag(
    label: String,
    onClick: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = HaebomTheme.colors

    val (textColor, backgroundColor, borderColor) = remember(selected, colors) {
        when (selected) {
            true -> Triple(colors.orange500, colors.yellow50, colors.orange500)
            false -> Triple(colors.gray300, colors.gray50, colors.gray50)
        }
    }

    Text(
        text = label,
        modifier = modifier
            .noRippleClickable(onClick)
            .heightIn(32.dp)
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(4.dp),
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(horizontal = 16.dp)
            .wrapContentHeight(Alignment.CenterVertically),
        color = textColor,
        textAlign = TextAlign.Center,
        style = HaebomTheme.typo.bottomNavbar,
    )
}

private class HaebomTagPreviewProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(true, false)
}

@Preview
@Composable
private fun HaebomTagPreview(
    @PreviewParameter(HaebomTagPreviewProvider::class) selected: Boolean,
) {
    HaebomTheme {
        HaebomTag(
            label = "공부",
            onClick = {},
            selected = selected,
        )
    }
}
