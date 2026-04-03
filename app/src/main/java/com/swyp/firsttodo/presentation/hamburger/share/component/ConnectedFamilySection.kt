package com.swyp.firsttodo.presentation.hamburger.share.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.family.ConnectedFamilyModel

@Composable
fun ConnectedFamilySection(
    families: Async<List<ConnectedFamilyModel>>,
    onDisconnectBtnClick: (ConnectedFamilyModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    ShareSection(
        title = "공유된 가족 보기",
        modifier = modifier,
    ) {
        when (families) {
            Async.Empty -> EmptyView()

            is Async.Success -> SuccessView(
                data = families.data,
                onDisconnectBtnClick = onDisconnectBtnClick,
            )

            else -> Unit
        }
    }
}

@Composable
private fun EmptyView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 104.dp)
            .background(
                color = HaebomTheme.colors.gray50,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(all = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "아직 연결된 가족이 없어요.",
            color = HaebomTheme.colors.gray400,
            textAlign = TextAlign.Center,
            style = HaebomTheme.typo.card,
        )

        Text(
            text = "초대코드를 입력하여 가족과 연결해 볼까요?",
            color = HaebomTheme.colors.gray300,
            textAlign = TextAlign.Center,
            style = HaebomTheme.typo.description,
        )
    }
}

@Composable
private fun SuccessView(
    data: List<ConnectedFamilyModel>,
    onDisconnectBtnClick: (ConnectedFamilyModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(HaebomTheme.colors.gray50),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        data.forEachIndexed { index, family ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = family.nickname,
                    modifier = Modifier.weight(1f),
                    color = HaebomTheme.colors.gray500,
                    style = HaebomTheme.typo.subtitle,
                )

                Text(
                    text = "연결 끊기",
                    modifier = Modifier
                        .noRippleClickable(onClick = { onDisconnectBtnClick(family) })
                        .sizeIn(minWidth = 77.dp, minHeight = 24.dp)
                        .background(
                            color = HaebomTheme.colors.gray200,
                            shape = RoundedCornerShape(4.dp),
                        )
                        .padding(all = 4.dp)
                        .wrapContentSize(Alignment.Center),
                    color = HaebomTheme.colors.white,
                    style = HaebomTheme.typo.helperText,
                )
            }

            if (index < data.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = HaebomTheme.colors.gray200,
                )
            }
        }
    }
}

private class ConnectedFamilySectionPreviewProvider : PreviewParameterProvider<Async<List<ConnectedFamilyModel>>> {
    override val values = sequenceOf(
        Async.Init,
        Async.Loading(),
        Async.Empty,
        Async.Success(
            listOf(
                ConnectedFamilyModel(userId = 1L, nickname = "엄마"),
            ),
        ),
        Async.Success(
            listOf(
                ConnectedFamilyModel(userId = 1L, nickname = "엄마는외계인"),
                ConnectedFamilyModel(userId = 2L, nickname = "아빠"),
                ConnectedFamilyModel(userId = 3L, nickname = "닉네임이아주아주아주아주아주길어요"),
            ),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun ConnectedFamilySectionPreview(
    @PreviewParameter(ConnectedFamilySectionPreviewProvider::class) families: Async<List<ConnectedFamilyModel>>,
) {
    HaebomTheme {
        ConnectedFamilySection(
            families = families,
            onDisconnectBtnClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
