package com.swyp.firsttodo.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

sealed interface HaebomHeaderTabType {
    val index: Int
    val label: String
}

enum class RewardHeaderTabType(
    override val index: Int,
    override val label: String,
) : HaebomHeaderTabType {
    STICKER(0, "할 일 스티커"),
    REWARD(1, "보상 관리"),
}

enum class GrowthHeaderTabType(
    override val index: Int,
    override val label: String,
) : HaebomHeaderTabType {
    TODO(0, "할 일"),
    REWARD(1, "보상"),
}

@Composable
fun <T : HaebomHeaderTabType> HaebomHeaderTab(
    currentTab: T,
    tabs: List<T>,
    onTabClick: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    TabRow(
        selectedTabIndex = currentTab.index,
        modifier = modifier,
        containerColor = HaebomTheme.colors.white,
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[currentTab.index])
                    .height(2.dp)
                    .padding(horizontal = screenWidthDp(23.dp))
                    .background(
                        color = HaebomTheme.colors.gray800,
                        shape = CircleShape,
                    ),
            )
        },
        divider = {
            HorizontalDivider(
                thickness = 2.dp,
                color = HaebomTheme.colors.gray200,
            )
        },
    ) {
        tabs.forEach { tab ->
            Text(
                text = tab.label,
                modifier = Modifier
                    .noRippleClickable({ onTabClick(tab) })
                    .padding(top = 25.dp, bottom = 16.dp),
                color = HaebomTheme.colors.black,
                textAlign = TextAlign.Center,
                style = HaebomTheme.typo.screen,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun HaebomHeaderTabPreview() {
    var currentTab by remember { mutableStateOf(RewardHeaderTabType.STICKER) }

    HaebomTheme {
        HaebomHeaderTab(
            currentTab = currentTab,
            tabs = RewardHeaderTabType.entries,
            onTabClick = { currentTab = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
        )
    }
}
