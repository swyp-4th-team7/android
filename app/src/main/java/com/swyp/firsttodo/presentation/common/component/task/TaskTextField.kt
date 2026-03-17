package com.swyp.firsttodo.presentation.common.component.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.designsystem.component.HaebomBasicTextField
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun TaskTextField(
    fieldState: TextFieldState,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    sampleText: String? = null,
    errorText: String? = null,
    onKeyboardAction: KeyboardActionHandler? = null,
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null,
) {
    val colors = HaebomTheme.colors

    val borderColor = remember(errorText) {
        when (errorText) {
            null -> colors.gray50
            else -> colors.semanticRed
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        HaebomBasicTextField(
            state = fieldState,
            placeholder = placeholder,
            modifier = Modifier.fillMaxWidth(),
            inputTransformation = inputTransformation,
            outputTransformation = outputTransformation,
            borderColor = borderColor,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            lineLimits = TextFieldLineLimits.SingleLine,
        )

        when {
            errorText != null -> Text(
                text = errorText,
                color = HaebomTheme.colors.semanticRed,
                style = HaebomTheme.typo.helperText,
            )

            sampleText != null -> Text(
                text = sampleText,
                color = HaebomTheme.colors.gray300,
                style = HaebomTheme.typo.helperText,
            )

            else -> Unit
        }
    }
}
