package com.swyp.firsttodo.presentation.main.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.component.HaebomBasicDialog
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

enum class DrawerDialogType(
    val title: String,
    val description: String,
    val cancelBtnText: String,
    val confirmBtnText: String,
) {
    LOGOUT(
        title = "로그아웃",
        description = "로그아웃 하시겠습니까?",
        cancelBtnText = "아니요",
        confirmBtnText = "네",
    ),

    WITHDRAWAL(
        title = "탈퇴하기",
        description = "회원 탈퇴 시 개인정보와 설정이 모두 삭제됩니다.\n탈퇴를 진행하시겠습니까?",
        cancelBtnText = "아니요",
        confirmBtnText = "네",
    ),
}

@Composable
fun MainDrawerDialog(
    dialogType: DrawerDialogType,
    onDismiss: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HaebomBasicDialog(
        onDismiss = onDismiss,
        modifier = modifier.padding(horizontal = screenWidthDp(20.dp)),
    ) {
        DialogContent(
            dialogType = dialogType,
            onCancel = onCancel,
            onConfirm = onConfirm,
        )
    }
}

@Composable
fun DialogContent(
    dialogType: DrawerDialogType,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = dialogType.title,
            modifier = Modifier.padding(
                top = screenHeightDp(33.5.dp),
                bottom = screenHeightDp(8.dp),
            ),
            color = HaebomTheme.colors.black,
            textAlign = TextAlign.Center,
            style = HaebomTheme.typo.card,
        )

        Text(
            text = dialogType.description,
            modifier = Modifier.padding(bottom = screenHeightDp(33.5.dp)),
            color = HaebomTheme.colors.gray400,
            textAlign = TextAlign.Center,
            style = HaebomTheme.typo.description,
        )

        Row {
            DialogButton(
                text = dialogType.cancelBtnText,
                textColor = HaebomTheme.colors.black,
                backgroundColor = HaebomTheme.colors.gray100,
                onClick = onCancel,
                modifier = Modifier.weight(1f),
            )

            DialogButton(
                text = dialogType.confirmBtnText,
                textColor = HaebomTheme.colors.white,
                backgroundColor = HaebomTheme.colors.orange500,
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
fun DialogButton(
    text: String,
    textColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier
            .clickable(onClick = onClick)
            .heightIn(screenHeightDp(43.dp))
            .background(backgroundColor)
            .padding(all = 10.dp)
            .wrapContentSize(Alignment.Center),
        color = textColor,
        style = HaebomTheme.typo.placeholder,
    )
}

@Preview(showBackground = true)
@Composable
private fun MainDrawerDialogPreview() {
    HaebomTheme {
        DialogContent(
            dialogType = DrawerDialogType.WITHDRAWAL,
            onCancel = {},
            onConfirm = {},
        )
    }
}
