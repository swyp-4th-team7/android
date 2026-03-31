package com.swyp.firsttodo.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.heightForScreenPercentage
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.common.extension.skeleton
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.component.HaebomBasicDialog
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

sealed class DeleteDialogType(
    val title: String,
    val description: String,
) {
    data object Todo : DeleteDialogType(
        title = "선택한 할 일을 삭제할까요?",
        description = "입력한 할 일이 사라져요!",
    )

    data object Habit : DeleteDialogType(
        title = "선택한 습관을 삭제할까요?",
        description = "입력한 습관이 사라져요!",
    )

    class Disconnect(nickname: String) : DeleteDialogType(
        title = "${nickname}님과의 연결을 끊을까요?",
        description = "연동이 끊기면 ${nickname}님의\n활동을 볼 수 없습니다.",
    )
}

@Composable
fun HaebomDeleteDialog(
    dialogType: DeleteDialogType,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    loadingState: Async<Unit>,
    modifier: Modifier = Modifier,
    confirmBtnLabel: String = "네, 삭제할래요",
    cancelBtnLabel: String = "아니요",
) {
    LaunchedEffect(loadingState) {
        if (loadingState is Async.Success) onDismiss()
    }

    HaebomBasicDialog(
        onDismiss = onDismiss,
        modifier = modifier.padding(horizontal = screenWidthDp(20.dp)),
    ) {
        val isLoading = loadingState is Async.Loading

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
        modifier = Modifier
            .padding(all = screenWidthDp(24.dp)),
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

        Spacer(Modifier.heightForScreenPercentage(8.dp))

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
            .padding(8.dp)
            .wrapContentHeight(Alignment.CenterVertically),
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
