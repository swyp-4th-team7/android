package com.swyp.firsttodo.presentation.auth.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.common.extension.figmaDropShadow
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun IntroPager(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(
        pageCount = { 4 },
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = pagerState,
        ) {
            val imageRes = when (it) {
                0 -> R.drawable.img_onboarding_1
                1 -> R.drawable.img_onboarding_2
                2 -> R.drawable.img_onboarding_3
                else -> R.drawable.img_onboarding_4
            }

            val text = when (it) {
                0 -> "나는\n스스로 할 수 있다_!"
                1 -> "오늘 할 일\n스스로 적고 실행!"
                2 -> "전부 해내고 받는\n약속된 선물!"
                else -> "매일 완료하면\n좋은 습관 완성!"
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(30.dp),
            ) {
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = null,
                )

                Text(
                    text = text,
                    modifier = Modifier.fillMaxWidth(),
                    color = HaebomTheme.colors.black,
                    textAlign = TextAlign.Center,
                    style = HaebomTheme.typo.week,
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(pagerState.pageCount) { current ->
                val color = when (current) {
                    pagerState.currentPage -> HaebomTheme.colors.orange500
                    else -> HaebomTheme.colors.gray100
                }

                Box(
                    modifier = Modifier
                        .background(color, CircleShape)
                        .size(8.dp)
                        .figmaDropShadow(
                            shape = CircleShape,
                            dpOffset = DpOffset(1.dp, 1.dp),
                            blur = 1.dp,
                            spread = 0.dp,
                            color = HaebomTheme.colors.black.copy(alpha = 0.2f),
                        ),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IntroPagerPreview() {
    HaebomTheme {
        IntroPager()
    }
}
