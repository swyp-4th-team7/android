package com.swyp.firsttodo.presentation.hamburger.share.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.designsystem.component.HaebomBasicTextField
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun CodeInputSection(
    fieldState: TextFieldState,
    onDoneAction: () -> Unit,
    errorText: String?,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    ShareSection(
        title = "초대코드 입력",
        modifier = modifier,
    ) {
        InviteTextField(
            fieldState = fieldState,
            errorText = errorText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp)
                .wrapContentHeight(Alignment.CenterVertically),
            onKeyboardAction = {
                keyboardController?.hide()
                focusManager.clearFocus()
                onDoneAction()
            },
        )
    }
}

@Composable
private fun InviteTextField(
    fieldState: TextFieldState,
    errorText: String?,
    modifier: Modifier = Modifier,
    onKeyboardAction: KeyboardActionHandler? = null,
) {
    val (borderColor, textColor) = when (errorText) {
        null -> HaebomTheme.colors.gray50 to Color.Transparent
        else -> HaebomTheme.colors.semanticRed to HaebomTheme.colors.semanticRed
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        HaebomBasicTextField(
            state = fieldState,
            placeholder = "상대방의 초대코드를 입력해 주세요.",
            modifier = Modifier.fillMaxWidth(),
            borderColor = borderColor,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onKeyboardAction = onKeyboardAction,
            lineLimits = TextFieldLineLimits.SingleLine,
        )

        Text(
            text = errorText ?: "",
            color = textColor,
            style = HaebomTheme.typo.helperText,
        )
    }
}

private class CodeInputSectionPreviewProvider : PreviewParameterProvider<String?> {
    override val values = sequenceOf(
        null,
        "올바르지 않은 초대 코드예요. 다시 확인해 주세요.",
    )
}

@Preview(showBackground = true)
@Composable
private fun CodeInputSectionPreview(
    @PreviewParameter(CodeInputSectionPreviewProvider::class) errorText: String?,
) {
    HaebomTheme {
        CodeInputSection(
            fieldState = rememberTextFieldState(),
            onDoneAction = {},
            errorText = errorText,
            modifier = Modifier.padding(16.dp),
        )
    }
}
