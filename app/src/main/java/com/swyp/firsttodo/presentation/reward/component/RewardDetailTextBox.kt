package com.swyp.firsttodo.presentation.reward.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun RewardDetailTextBox(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(42.dp)
            .background(
                color = HaebomTheme.colors.gray50,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .wrapContentHeight(Alignment.CenterVertically),
        color = HaebomTheme.colors.black,
        style = HaebomTheme.typo.buttonL,
    )
}
