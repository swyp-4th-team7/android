package com.swyp.firsttodo.presentation.common.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun HaebomBasicTextField(
    state: TextFieldState,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    inputTransformation: InputTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    outputTransformation: OutputTransformation? = null,
    scrollState: ScrollState? = null,
) {
    val resolvedScrollState = scrollState ?: rememberScrollState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        state = state,
        modifier = modifier
            .background(
                color = HaebomTheme.colors.gray200,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        enabled = enabled,
        readOnly = readOnly,
        inputTransformation = inputTransformation,
        textStyle = HaebomTheme.typo.description.copy(
            color = HaebomTheme.colors.black,
        ),
        keyboardOptions = keyboardOptions,
        onKeyboardAction = { performDefaultAction ->
            if (keyboardOptions.imeAction == ImeAction.Done) {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
            onKeyboardAction?.onKeyboardAction(performDefaultAction)
        },
        lineLimits = lineLimits,
        outputTransformation = outputTransformation,
        decorator = { innerTextField ->
            innerTextField()

            if (state.text.isEmpty()) {
                Text(
                    text = placeholder,
                    color = HaebomTheme.colors.gray300,
                    style = HaebomTheme.typo.description,
                )
            }
        },
        scrollState = resolvedScrollState,
    )
}

private class HaebomTextFieldPreviewProvider : PreviewParameterProvider<Pair<TextFieldState, String>> {
    override val values = sequenceOf(
        (TextFieldState() to "MM/DD/YYYY"),
        (TextFieldState("하루에 책 10장 읽기") to "습관을 작성해주세요. (최대 12자)"),
    )
}

@Preview
@Composable
private fun HaebomBasicTextFieldPreview(
    @PreviewParameter(HaebomTextFieldPreviewProvider::class) param: Pair<TextFieldState, String>,
) {
    HaebomTheme {
        HaebomBasicTextField(
            state = param.first,
            placeholder = param.second,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
