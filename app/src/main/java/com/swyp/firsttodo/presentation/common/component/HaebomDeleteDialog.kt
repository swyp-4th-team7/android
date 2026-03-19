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

enum class DeleteDialogType(
    val title: String,
    val description: String,
) {
    TODO(
        title = "선택한 할 일을 삭제할까요?",
        description = "입력한 할 일이 사라져요!",
    ),
    SCHEDULE(
        title = "선택한 일정을 삭제할까요?",
        description = "입력한 일정이 사라져요!",
    ),
    HABIT(
        title = "선택한 할 일을 삭제할까요?",
        description = "입력한 할 일이 사라져요!",
    ),
}

@Composable
fun HaebomDeleteDialog(
    dialogType: DeleteDialogType,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    confirmBtnLabel: String = "네, 삭제할래요",
    cancelBtnLabel: String = "아니요",
) {
    HaebomBasicDialog(
        onDismiss = onDismiss,
        modifier = modifier.padding(horizontal = screenWidthDp(20.dp)),
    ) {
        DialogContent(
            dialogType = dialogType,
            onConfirm = onConfirm,
            onCancel = onCancel,
            confirmBtnLabel = confirmBtnLabel,
            cancelBtnLabel = cancelBtnLabel,
        )
    }
}

@Composable
fun DialogContent(
    dialogType: DeleteDialogType,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    confirmBtnLabel: String = "네, 삭제할래요",
    cancelBtnLabel: String = "아니요",
) {
    Column(
        modifier = Modifier
            .padding(all = screenWidthDp(24.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = dialogType.title,
            color = HaebomTheme.colors.black,
            style = HaebomTheme.typo.card,
        )

        Spacer(Modifier.heightForScreenPercentage(8.dp))

        Text(
            text = dialogType.description,
            color = HaebomTheme.colors.gray400,
            style = HaebomTheme.typo.description,
        )

        Spacer(Modifier.heightForScreenPercentage(10.dp))

        Row(
            modifier = Modifier.padding(vertical = screenHeightDp(4.dp)),
            horizontalArrangement = Arrangement.spacedBy(screenWidthDp(8.dp)),
        ) {
            DialogButton(
                text = cancelBtnLabel,
                textColor = HaebomTheme.colors.gray200,
                backgroundColor = HaebomTheme.colors.gray50,
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(40.dp),
            )

            DialogButton(
                text = confirmBtnLabel,
                textColor = HaebomTheme.colors.white,
                backgroundColor = HaebomTheme.colors.orange500,
                onClick = onConfirm,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(40.dp),
            )
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
            .padding(10.dp)
            .wrapContentHeight(Alignment.CenterVertically),
        color = textColor,
        textAlign = TextAlign.Center,
        style = HaebomTheme.typo.buttonM,
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun HaebomDeleteDialogContentPreview() {
    HaebomTheme {
        DialogContent(
            dialogType = DeleteDialogType.TODO,
            onConfirm = {},
            onCancel = {},
        )
    }
}
