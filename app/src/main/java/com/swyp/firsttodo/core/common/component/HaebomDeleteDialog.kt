package com.swyp.firsttodo.core.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.common.extension.skeleton
import com.swyp.firsttodo.core.common.type.DeleteDialogType
import com.swyp.firsttodo.core.designsystem.component.HaebomBasicDialog
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun HaebomDeleteDialog(
    dialogType: DeleteDialogType,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    confirmBtnLabel: String = "네, 삭제할래요",
    cancelBtnLabel: String = "아니요",
) {
    HaebomBasicDialog(
        onDismiss = onDismiss,
        modifier = modifier.padding(horizontal = 20.dp),
    ) {
        DialogContent(
            dialogType = dialogType,
            onConfirm = onConfirm,
            onCancel = onCancel,
            confirmBtnLabel = confirmBtnLabel,
            cancelBtnLabel = cancelBtnLabel,
            isLoading = isLoading,
        )
    }
}

@Composable
fun DialogContent(
    dialogType: DeleteDialogType,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    isLoading: Boolean,
    confirmBtnLabel: String,
    cancelBtnLabel: String,
) {
    Column(
        modifier = Modifier.padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isLoading) {
            Spacer(
                modifier = Modifier
                    .size(198.dp, 26.dp)
                    .skeleton(),
            )
        } else {
            Text(
                text = dialogType.title,
                color = HaebomTheme.colors.black,
                textAlign = TextAlign.Center,
                style = HaebomTheme.typo.card,
            )
        }

        Spacer(Modifier.height(8.dp))

        if (isLoading) {
            Spacer(
                modifier = Modifier
                    .size(138.dp, 21.dp)
                    .skeleton(),
            )
        } else {
            Text(
                text = dialogType.description,
                color = HaebomTheme.colors.gray400,
                textAlign = TextAlign.Center,
                style = HaebomTheme.typo.description,
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .heightIn(min = 48.dp)
                .padding(vertical = 4.dp)
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DialogButton(
                text = cancelBtnLabel,
                textColor = HaebomTheme.colors.gray200,
                backgroundColor = HaebomTheme.colors.gray50,
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )

            DialogButton(
                text = confirmBtnLabel,
                textColor = HaebomTheme.colors.white,
                backgroundColor = HaebomTheme.colors.orange500,
                onClick = onConfirm,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
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
            .wrapContentSize(Alignment.Center),
        color = textColor,
        textAlign = TextAlign.Center,
        style = HaebomTheme.typo.buttonM,
    )
}

private class HaebomDeleteDialogContentPreviewProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(true, false)
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun HaebomDeleteDialogContentPreview(
    @PreviewParameter(HaebomDeleteDialogContentPreviewProvider::class) isLoading: Boolean,
) {
    HaebomTheme {
        DialogContent(
            dialogType = DeleteDialogType.Todo,
            onConfirm = {},
            onCancel = {},
            isLoading = isLoading,
            confirmBtnLabel = "네",
            cancelBtnLabel = "아니오",
        )
    }
}
