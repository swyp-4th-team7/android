package com.swyp.firsttodo.presentation.auth.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.MediumStyle

@Composable
fun LegalLinks(
    onTosClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy((screenHeightDp(4.dp))),
    ) {
        Text(
            text = "로그인하시면 아래 내용에 동의하는 것으로 간주됩니다.",
            color = HaebomTheme.colors.gray300,
            style = MediumStyle.copy(fontSize = 10.sp),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            Text(
                text = "개인정보 처리방침",
                modifier = Modifier.noRippleClickable(onPrivacyClick),
                color = HaebomTheme.colors.gray300,
                style = MediumStyle.copy(
                    fontSize = 10.sp,
                    textDecoration = TextDecoration.Underline,
                ),
            )

            Text(
                text = "이용약관",
                modifier = Modifier.noRippleClickable(onTosClick),
                color = HaebomTheme.colors.gray300,
                style = MediumStyle.copy(
                    fontSize = 10.sp,
                    textDecoration = TextDecoration.Underline,
                ),
            )
        }
    }
}

@Preview
@Composable
private fun LegalLinksPreview() {
    HaebomTheme {
        LegalLinks(
            onTosClick = {},
            onPrivacyClick = {},
        )
    }
}
