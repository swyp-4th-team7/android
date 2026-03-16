package com.swyp.firsttodo.presentation.auth.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun IntroPager(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(
        pageCount = { 4 },
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier,
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Gray),
            )
        }

        Row {
            repeat(pagerState.pageCount) { current ->
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(if (current == pagerState.currentPage) Color.Red else Color.Yellow)
                        .size(16.dp),
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
