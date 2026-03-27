package com.swyp.firsttodo.presentation.onboarding.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.presentation.common.component.HaebomMultiLineTextField

@Composable
fun ProfileView(
    fieldState: TextFieldState,
    errorText: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "가족과 공유할\n당신의 이름을 적어 주세요.",
            modifier = Modifier.padding(bottom = 12.dp),
            color = HaebomTheme.colors.gray800,
            textAlign = TextAlign.Center,
            style = HaebomTheme.typo.hero,
        )

        Text(
            text = "한글 최대 12자 /영문,숫자,특수기호 불가",
            modifier = Modifier.padding(bottom = 56.dp),
            color = HaebomTheme.colors.gray300,
            style = HaebomTheme.typo.helperText,
        )

        HaebomMultiLineTextField(
            fieldState = fieldState,
            placeholder = "닉네임을 입력해 주세요.",
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            errorText = errorText,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileViewPreview() {
    HaebomTheme {
        ProfileView(
            fieldState = rememberTextFieldState(),
            errorText = "아아",
        )
    }
}
