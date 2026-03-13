package com.swyp.firsttodo.presentation.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun RoleSelectButton(
    text: String,
    onClick: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = HaebomTheme.colors

    val (textColor, backgroundColor, borderColor) = remember(selected, colors) {
        when (selected) {
            true -> Triple(colors.orange500, colors.orange25, colors.orange500)
            false -> Triple(colors.gray500, colors.white, colors.gray400)
        }
    }

    Text(
        text = text,
        modifier = modifier
            .noRippleClickable(onClick)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp),
            )
            .border(
                width = 1.2.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 10.dp, vertical = 25.dp),
        color = textColor,
        textAlign = TextAlign.Center,
        style = HaebomTheme.typo.button,
    )
}

private class RoleSelectButtonPreviewProvider : PreviewParameterProvider<Pair<String, Boolean>> {
    override val values = sequenceOf(
        ("부모입니다." to true),
        ("부모입니다." to false),
        ("자녀입니다." to true),
        ("자녀입니다." to false),
    )
}

@Preview
@Composable
private fun RoleSelectButtonPreview(
    @PreviewParameter(RoleSelectButtonPreviewProvider::class) param: Pair<String, Boolean>,
) {
    HaebomTheme {
        RoleSelectButton(
            text = param.first,
            onClick = {},
            selected = param.second,
            modifier = Modifier.width(294.dp),
        )
    }
}
