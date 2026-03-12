package com.swyp.firsttodo.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.BoldStyle
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
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
) {
    val dialogProperties = remember(dismissOnBackPress, dismissOnClickOutside) {
        DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            usePlatformDefaultWidth = false,
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = dialogProperties,
    ) {
        val colors = HaebomTheme.colors
        val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window

        SideEffect {
            dialogWindow?.setDimAmount(0.5f)
        }

        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            color = colors.white,
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp, bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    color = colors.black,
                    style = BoldStyle.copy(fontSize = 18.sp),
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = description,
                    color = colors.gray400,
                    style = BoldStyle.copy(fontSize = 14.sp),
                )

                Spacer(Modifier.height(22.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    DialogButton(
                        text = cancelBtnLabel,
                        textColor = colors.gray200,
                        backgroundColor = colors.gray50,
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                    )

                    DialogButton(
                        text = confirmBtnLabel,
                        textColor = colors.white,
                        backgroundColor = colors.orange500,
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                    )
                }
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
            .minimumInteractiveComponentSize()
            .padding(10.dp),
        color = textColor,
        textAlign = TextAlign.Center,
        style = BoldStyle.copy(fontSize = 14.sp),
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
