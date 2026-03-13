package com.swyp.firsttodo.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.LabelColor

@Composable
fun HaebomLabel(
    labelColor: LabelColor,
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier
            .heightIn(20.dp)
            .background(
                color = labelColor.background,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(horizontal = 8.dp)
            .wrapContentHeight(Alignment.CenterVertically),
        color = labelColor.text,
        style = HaebomTheme.typo.caption,
    )
}

private data class HaebomLabelParam(
    val labelColor: LabelColor,
    val text: String,
)

private class HaebomLabelPreviewProvider : PreviewParameterProvider<HaebomLabelParam> {
    override val values = sequenceOf(
        HaebomLabelParam(LabelColor.BLUE, "공부"),
        HaebomLabelParam(LabelColor.LIME, "집안일"),
        HaebomLabelParam(LabelColor.RED, "수행평가"),
    )
}

@Preview
@Composable
private fun HaebomLabelPreview(
    @PreviewParameter(HaebomLabelPreviewProvider::class) param: HaebomLabelParam,
) {
    HaebomTheme {
        HaebomLabel(
            labelColor = param.labelColor,
            text = param.text,
        )
    }
}
