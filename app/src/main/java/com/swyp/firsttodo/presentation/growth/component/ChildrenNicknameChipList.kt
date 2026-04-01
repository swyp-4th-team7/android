package com.swyp.firsttodo.presentation.growth.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.growth.ChildGrowthModel

@Composable
fun ChildrenNicknameChipList(
    childrenGrowth: List<ChildGrowthModel>,
    selectedChildIdx: Int,
    onChildChipClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(items = childrenGrowth, key = { _, item -> item.childId }) { index, model ->
            ChildChip(
                nickname = model.nickname,
                selected = index == selectedChildIdx,
                onClick = { onChildChipClick(index) },
            )
        }
    }
}

@Composable
private fun ChildChip(
    nickname: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = HaebomTheme.colors

    val (textColor, backgroundColor, borderColor) = when (selected) {
        true -> Triple(colors.white, colors.orange400, colors.orange400)
        false -> Triple(colors.gray300, colors.gray50, colors.gray100)
    }

    Text(
        text = nickname,
        modifier = modifier
            .noRippleClickable(onClick)
            .padding(vertical = 8.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(24.dp),
            )
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(24.dp),
            )
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .widthIn(32.dp)
            .wrapContentSize(Alignment.Center),
        color = textColor,
        style = HaebomTheme.typo.buttonL,
    )
}

@Preview(showBackground = true)
@Composable
private fun ChildrenNicknameChipListPreview() {
    val nicknames = listOf("나", "두글자", "다섯글자닉네임", "여덟글자닉네임이야", "열두글자닉네임입니다짝짝")
    val children = nicknames.mapIndexed { idx, nickname ->
        ChildGrowthModel(
            childId = idx.toLong(),
            nickname = nickname,
            todoStarCount = 0,
            habitStarCount = 0,
            weekRange = "",
        )
    }
    HaebomTheme {
        ChildrenNicknameChipList(
            childrenGrowth = children,
            selectedChildIdx = 0,
            onChildChipClick = {},
        )
    }
}
