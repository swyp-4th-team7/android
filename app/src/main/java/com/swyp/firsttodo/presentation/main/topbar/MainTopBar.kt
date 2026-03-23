package com.swyp.firsttodo.presentation.main.topbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

const val MAIN_TOP_BAR_HEIGHT = 56

@Composable
fun MainTopBar(
    visible: Boolean,
    onMenuClick: () -> Unit,
    onAlarmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300)),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MAIN_TOP_BAR_HEIGHT.dp)
                .background(HaebomTheme.colors.yellow400),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_menu),
                contentDescription = null,
                modifier = Modifier
                    .noRippleClickable(onMenuClick)
                    .padding(all = 16.dp),
                tint = Color.Unspecified,
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_alarm),
                contentDescription = null,
                modifier = Modifier
                    .noRippleClickable(onAlarmClick)
                    .padding(all = 16.dp),
                tint = Color.Unspecified,
            )
        }
    }
}

@Preview
@Composable
private fun MainTopBarPreview() {
    HaebomTheme {
        MainTopBar(
            visible = true,
            onMenuClick = {},
            onAlarmClick = {},
        )
    }
}
