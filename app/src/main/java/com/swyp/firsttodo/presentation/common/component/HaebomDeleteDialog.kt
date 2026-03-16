package com.swyp.firsttodo.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.common.extension.heightForScreenPercentage
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.component.HaebomBasicDialog
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun HaebomDeleteDialog(
    title: String,
    description: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    confirmBtnLabel: String = "네, 삭제할래요",
    cancelBtnLabel: String = "아니요",
) {
    val colors = HaebomTheme.colors

    HaebomBasicDialog(
        onDismiss = onDismiss,
        modifier = modifier.padding(horizontal = screenWidthDp(20.dp)),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = screenWidthDp(20.dp))
                .padding(top = screenHeightDp(32.dp), bottom = screenHeightDp(20.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                color = colors.black,
                style = HaebomTheme.typo.card,
            )

            Spacer(Modifier.heightForScreenPercentage(8.dp))

            Text(
                text = description,
                color = colors.gray400,
                style = HaebomTheme.typo.description,
            )

            Spacer(Modifier.heightForScreenPercentage(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(screenWidthDp(16.dp)),
            ) {
                DialogButton(
                    text = cancelBtnLabel,
                    textColor = colors.gray200,
                    backgroundColor = colors.gray50,
                    onClick = onCancel,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(40.dp),
                )

                DialogButton(
                    text = confirmBtnLabel,
                    textColor = colors.white,
                    backgroundColor = colors.orange500,
                    onClick = onConfirm,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(40.dp),
                )
            }
        }
    }
}

@Composable
private fun DialogButton(
    text: String,
    textColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier
            .noRippleClickable(onClick)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(8.dp)
            .wrapContentHeight(Alignment.CenterVertically),
        color = textColor,
        textAlign = TextAlign.Center,
        style = HaebomTheme.typo.buttonM,
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun HaebomDeleteDialogPreview() {
    HaebomTheme {
        HaebomDeleteDialog(
            title = "선택한 할 일을 삭제할까요?",
            description = "입력한 할 일이 사라져요!",
            onConfirm = {},
            onCancel = {},
            onDismiss = {},
        )
    }
}
