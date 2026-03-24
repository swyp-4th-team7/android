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
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.sticker.ChildStickerModel

@Composable
fun ParentStickerList(
    stickers: Async<List<ChildStickerModel>>,
    modifier: Modifier = Modifier,
) {
    when (stickers) {
        Async.Empty -> ParentRewardEmptyView(
            title = "아직 관리할 스티커가 없습니다.",
            description = "자녀의 할 일을 만들어주세요.",
            modifier = modifier,
        )

        else -> {
            when (val data = stickers.getDataOrNull()) {
                null -> Unit

                else -> Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    data.forEach { sticker ->
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
                                text = sticker.nickname ?: "",
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
                                        text = "${sticker.boardNumber}번째",
                                        color = HaebomTheme.colors.gray300,
                                        style = HaebomTheme.typo.helperText,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )

                                    Text(
                                        text = sticker.startDate ?: "",
                                        color = HaebomTheme.colors.gray300,
                                        style = HaebomTheme.typo.helperText,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .padding(start = 8.dp),
                                ) {
                                    Text(
                                        text = "${sticker.filledSlots} / 30",
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
    ChildStickerModel(
        childId = 1L,
        nickname = "해봄이",
        boardNumber = 2,
        filledSlots = 15,
        boardSize = 30,
        startDate = "2026.03.17 (화)",
    ),
    ChildStickerModel(
        childId = 2L,
        nickname = "해봄이",
        boardNumber = 20,
        filledSlots = 15,
        boardSize = 30,
        startDate = "2026.03.17 (화)",
    ),
)

private class ParentStickerListPreviewProvider : PreviewParameterProvider<Async<List<ChildStickerModel>>> {
    override val values: Sequence<Async<List<ChildStickerModel>>> = sequenceOf(
        Async.Success(sampleStickers),
        Async.Empty,
        Async.Loading(),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun ParentStickerListPreview(
    @PreviewParameter(ParentStickerListPreviewProvider::class) stickers: Async<List<ChildStickerModel>>,
) {
    HaebomTheme {
        ParentStickerList(
            stickers = stickers,
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
        )
    }
}
