package com.swyp.firsttodo.core.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.BoldStyle
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.RegularStyle

@Composable
fun BottomSheetHeader(
    title: String,
    description: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = HaebomTheme.colors

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = title,
                color = HaebomTheme.colors.black,
                style = BoldStyle.copy(fontSize = 20.sp),
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_close_24),
                contentDescription = null,
                modifier = Modifier
                    .noRippleClickable(onDismiss)
                    .padding(all = 12.dp),
                tint = colors.black,
            )
        }

        Text(
            text = description,
            color = colors.gray400,
            style = RegularStyle.copy(fontSize = 14.sp),
        )
    }
}
