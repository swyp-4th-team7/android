package com.swyp.firsttodo.presentation.reward.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

data class ParentStickerListUiModel(
    val id: Long,
    val title: String,
    val boardCount: Int,
    val startDate: String,
    val stickerCount: Int,
)

@Composable
fun ParentStickerList(
    stickers: Async<List<ParentStickerListUiModel>>,
    onLabelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (stickers) {
        Async.Empty -> ParentRewardEmptyView(
            title = "아직 관리할 스티커가 없습니다.",
            description = "자녀의 할 일을 만들어주세요.",
        )

        else -> {
            when (val data = stickers.getDataOrNull()) {
                null -> Unit

                else -> LazyColumn(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(items = data, key = { it.id }) { sticker ->
                        Column(
                            modifier = Modifier
                                .background(
                                    color = HaebomTheme.colors.gray50,
                                    shape = RoundedCornerShape(8.dp),
                                )
                                .padding(horizontal = 8.dp)
                                .padding(top = 16.dp, bottom = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            Text(
                                text = sticker.title,
                                color = HaebomTheme.colors.black,
                                style = HaebomTheme.typo.buttonL,
                            )

                            Row(
                                modifier = Modifier
                                    .background(
                                        color = HaebomTheme.colors.white,
                                        shape = RoundedCornerShape(4.dp),
                                    )
                                    .padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    Text(
                                        text = "스티커판 : ",
                                        color = HaebomTheme.colors.gray300,
                                        style = HaebomTheme.typo.helperText,
                                    )

                                    Text(
                                        text = "시작 날짜 : ",
                                        color = HaebomTheme.colors.gray300,
                                        style = HaebomTheme.typo.helperText,
                                    )
                                }

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    Text(
                                        text = "${sticker.boardCount}번째",
                                        color = HaebomTheme.colors.gray300,
                                        style = HaebomTheme.typo.helperText,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )

                                    Text(
                                        text = sticker.startDate,
                                        color = HaebomTheme.colors.gray300,
                                        style = HaebomTheme.typo.helperText,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .noRippleClickable(onLabelClick)
                                        .padding(vertical = 8.dp)
                                        .padding(start = 8.dp),
                                ) {
                                    Text(
                                        text = "${sticker.stickerCount} / 30",
                                        modifier = Modifier
                                            .sizeIn(84.dp, 36.dp)
                                            .background(
                                                color = HaebomTheme.colors.yellow300,
                                                shape = RoundedCornerShape(4.dp),
                                            )
                                            .padding(all = 5.dp)
                                            .wrapContentSize(Alignment.Center),
                                        color = HaebomTheme.colors.orange500,
                                        style = HaebomTheme.typo.card,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private val sampleStickers = listOf(
    ParentStickerListUiModel(
        id = 1L,
        title = "수학 공부하기",
        boardCount = 2,
        startDate = "2026.03.17 (화)",
        stickerCount = 15,
    ),
    ParentStickerListUiModel(
        id = 2L,
        title = "영어 단어 외우기",
        boardCount = 1,
        startDate = "2026.03.17 (화)",
        stickerCount = 3,
    ),
)

private class ParentStickerListPreviewProvider : PreviewParameterProvider<Async<List<ParentStickerListUiModel>>> {
    override val values: Sequence<Async<List<ParentStickerListUiModel>>> = sequenceOf(
        Async.Success(sampleStickers),
        Async.Empty,
        Async.Loading(),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun ParentStickerListPreview(
    @PreviewParameter(ParentStickerListPreviewProvider::class) stickers: Async<List<ParentStickerListUiModel>>,
) {
    HaebomTheme {
        ParentStickerList(
            stickers = stickers,
            onLabelClick = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
        )
    }
}
