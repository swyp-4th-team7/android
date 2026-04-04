package com.swyp.firsttodo.presentation.hamburger.family.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun FamilyDashBoardEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = HaebomTheme.colors.gray50,
                shape = RoundedCornerShape(4.dp),
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = "가족과 함께 시작해 보세요.",
            color = HaebomTheme.colors.gray400,
            textAlign = TextAlign.Center,
            style = HaebomTheme.typo.card,
        )

        Text(
            text = "가족을 추가하면 이곳에서\n실천 현황을 한 눈에 볼 수 있어요.",
            color = HaebomTheme.colors.gray300,
            textAlign = TextAlign.Center,
            style = HaebomTheme.typo.description,
        )
    }
}
