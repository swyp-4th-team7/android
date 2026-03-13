package com.swyp.firsttodo.presentation.common.component.task

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.swyp.firsttodo.core.designsystem.component.HaebomBasicTextField

@Composable
fun TaskTextField(
    fieldState: TextFieldState,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    maxCount: Int? = null,
) {
    HaebomBasicTextField(
        state = fieldState,
        placeholder = placeholder,
        modifier = modifier,
        inputTransformation = InputTransformation {
            if (maxCount != null && length > maxCount) delete(maxCount, length)
            val text = asCharSequence().toString()
            val filtered = text
                .filter { !it.isSurrogate() && Character.getType(it.code) != Character.OTHER_SYMBOL.toInt() }
            if (filtered != text) replace(0, length, filtered)
        },
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        lineLimits = TextFieldLineLimits.SingleLine,
    )
}
