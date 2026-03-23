package com.swyp.firsttodo.presentation.hamburger.share.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun MyCodeSection(
    inviteCode: Async<String>,
    onCopyBtnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current

    ShareSection(
        title = "내 초대코드",
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = HaebomTheme.colors.gray50,
                    shape = RoundedCornerShape(4.dp),
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            when (inviteCode) {
                is Async.Success -> Text(
                    text = inviteCode.data,
                    color = HaebomTheme.colors.black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = HaebomTheme.typo.week,
                )

                else -> Spacer(Modifier.width(100.dp))
            }

            Text(
                text = "초대코드 복사하기",
                modifier = Modifier
                    .clickable(onClick = {
                        if (inviteCode is Async.Success) {
                            clipboardManager.setText(AnnotatedString(inviteCode.data))
                        }
                        onCopyBtnClick()
                    })
                    .heightIn(min = 40.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(HaebomTheme.colors.orange500)
                    .padding(horizontal = 15.dp, vertical = 10.dp),
                color = HaebomTheme.colors.white,
                style = HaebomTheme.typo.buttonM,
            )
        }
    }
}

private class MyCodeSectionPreviewProvider : PreviewParameterProvider<Async<String>> {
    override val values = sequenceOf(
        Async.Success("ABC123XY"),
        Async.Loading(),
    )
}

@Preview(showBackground = true)
@Composable
private fun MyCodeSectionPreview(
    @PreviewParameter(MyCodeSectionPreviewProvider::class) inviteCode: Async<String>,
) {
    HaebomTheme {
        MyCodeSection(
            inviteCode = inviteCode,
            onCopyBtnClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
