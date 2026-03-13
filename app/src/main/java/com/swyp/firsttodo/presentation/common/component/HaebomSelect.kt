package com.swyp.firsttodo.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.LabelColor

@Composable
fun HaebomSelect(
    labelColor: LabelColor,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = HaebomTheme.colors

    Icon(
        imageVector = ImageVector.vectorResource(R.drawable.ic_check),
        contentDescription = null,
        modifier = modifier
            .noRippleClickable(onClick)
            .background(
                color = labelColor.background,
                shape = RoundedCornerShape(4.dp),
            )
            .border(
                width = 1.5.dp,
                color = if (selected) labelColor.text else colors.gray200,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(6.dp),
        tint = if (selected) labelColor.text else colors.gray200,
    )
}

private class HaebomSelectPreviewProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(true, false)
}

@Preview
@Composable
private fun HaebomSelectPreview(
    @PreviewParameter(HaebomSelectPreviewProvider::class) selected: Boolean,
) {
    HaebomTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.width(80.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(LabelColor.entries) {
                Box {
                    HaebomSelect(
                        labelColor = it,
                        selected = selected,
                        onClick = {},
                    )
                }
            }
        }
    }
}
