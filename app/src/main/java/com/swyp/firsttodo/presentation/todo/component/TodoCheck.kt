package com.swyp.firsttodo.presentation.todo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
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
fun TodoCheck(
    onClick: () -> Unit,
    checked: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 16.dp)
            .background(
                color = HaebomTheme.colors.white,
                shape = RoundedCornerShape(4.dp),
            )
            .noRippleClickable(onClick),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_check),
            contentDescription = null,
            modifier = Modifier
                .size(16.dp)
                .padding(4.dp)
                .background(
                    color = if (checked) HaebomTheme.colors.orange400 else Color(0xFFE5E5E5),
                    shape = CircleShape,
                ),
            tint = HaebomTheme.colors.white,
        )
    }
}

private class CheckedPreviewProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(true, false)
}

@Preview(showBackground = true)
@Composable
private fun TodoCheckPreview(
    @PreviewParameter(CheckedPreviewProvider::class) checked: Boolean,
) {
    HaebomTheme {
        TodoCheck(
            onClick = {},
            checked = checked,
        )
    }
}
