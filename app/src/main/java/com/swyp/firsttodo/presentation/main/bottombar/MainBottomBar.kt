package com.swyp.firsttodo.presentation.main.bottombar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.designsystem.theme.HeabomTheme

@Composable
fun MainBottomBar(
    visible: Boolean,
    currentTab: MainTab?,
    onTabClick: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)) + slideIn(animationSpec = tween(300)) { IntOffset(0, it.height) },
        exit = fadeOut(animationSpec = tween(300)) + slideOut(animationSpec = tween(300)) { IntOffset(0, it.height) },
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
        ) {
            MainTab.entries.forEach { tab ->
                BottomBarItem(
                    tab = tab,
                    selected = tab == currentTab,
                    onClick = { onTabClick(tab) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    tab: MainTab,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = HeabomTheme.colors
    val (itemColor, iconRes) = remember(selected, colors) {
        when (selected) {
            true -> colors.orange500 to tab.selectedIconRes
            false -> colors.gray300 to tab.defaultIconRes
        }
    }

    Column(
        modifier = modifier
            .heightIn(64.dp)
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.Tab,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            )
            .background(colors.white),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null,
            tint = itemColor,
        )

        Text(
            text = tab.label,
            color = itemColor,
            style = HeabomTheme.typo.bottomNavbar,
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun MainBottomBarPreview() {
    HeabomTheme {
        MainBottomBar(
            visible = true,
            currentTab = MainTab.TODO,
            onTabClick = {},
        )
    }
}
