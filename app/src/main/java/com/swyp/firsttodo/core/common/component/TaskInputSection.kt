package com.swyp.firsttodo.core.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.MediumStyle
import com.swyp.firsttodo.core.designsystem.theme.SemiBoldStyle

@Composable
fun TaskInputSection(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = title,
            color = HaebomTheme.colors.black,
            style = SemiBoldStyle.copy(fontSize = 18.sp),
        )

        if (description != null) {
            Text(
                text = description,
                color = HaebomTheme.colors.gray400,
                style = MediumStyle.copy(fontSize = 12.sp),
            )
        }

        content()
    }
}
