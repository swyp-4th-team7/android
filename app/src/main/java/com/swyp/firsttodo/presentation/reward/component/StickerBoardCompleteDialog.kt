package com.swyp.firsttodo.presentation.reward.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.common.extension.heightForScreenPercentage
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.common.extension.widthForScreenPercentage
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.component.HaebomBasicDialog
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun StickerBoardCompleteDialog(
    onDismiss: () -> Unit,
    onCompleteBtnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HaebomBasicDialog(
        onDismiss = onDismiss,
        modifier = modifier.padding(horizontal = screenWidthDp(20.dp)),
        dismissOnClickOutside = false,
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = screenHeightDp(24.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_close_24),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.End)
                    .noRippleClickable(onDismiss)
                    .offset(x = screenWidthDp((-12).dp), y = screenWidthDp(12.dp)),
                tint = Color.Unspecified,
            )

            Spacer(Modifier.heightForScreenPercentage(28.dp))

            Image(
                painter = painterResource(R.drawable.img_scticker_complete_cheer),
                contentDescription = null,
                modifier = Modifier
                    .widthForScreenPercentage(120.dp)
                    .heightForScreenPercentage(132.dp),
            )

            Spacer(Modifier.heightForScreenPercentage(16.dp))

            Text(
                text = "멋져요!\n30개의 스티커를 모았어요.",
                modifier = Modifier
                    .padding(vertical = 9.dp)
                    .padding(horizontal = screenWidthDp(24.dp))
                    .padding(bottom = screenHeightDp(24.dp)),
                color = HaebomTheme.colors.black,
                textAlign = TextAlign.Center,
                style = HaebomTheme.typo.screen,
            )

            Text(
                text = "확인하러 가기",
                modifier = Modifier
                    .noRippleClickable(onCompleteBtnClick)
                    .fillMaxWidth()
                    .heightIn(screenHeightDp(40.dp))
                    .padding(horizontal = screenWidthDp(24.dp))
                    .background(
                        color = HaebomTheme.colors.orange500,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .wrapContentSize(Alignment.Center),
                color = HaebomTheme.colors.white,
                style = HaebomTheme.typo.section,
            )
        }
    }
}

@Preview
@Composable
private fun StickerBoardCompleteDialogPreview() {
    HaebomTheme {
        StickerBoardCompleteDialog(
            onDismiss = {},
            onCompleteBtnClick = {},
        )
    }
}
