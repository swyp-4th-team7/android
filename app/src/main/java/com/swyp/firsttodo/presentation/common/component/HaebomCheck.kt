package com.swyp.firsttodo.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun HaebomCheck(
    onClick: () -> Unit,
    checked: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .noRippleClickable(onClick)
            .background(
                color = HaebomTheme.colors.white,
                shape = RoundedCornerShape(4.dp),
            )
            .size(width = 48.dp, height = 56.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(
                if (checked) R.drawable.ic_check_filled else R.drawable.ic_check_unfilled,
            ),
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }
}

private class CheckedPreviewProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(true, false)
}

@Preview
@Composable
private fun HaebomCheckPreview(
    @PreviewParameter(CheckedPreviewProvider::class) checked: Boolean,
) {
    HaebomTheme {
        HaebomCheck(
            onClick = {},
            checked = checked,
        )
    }
}
