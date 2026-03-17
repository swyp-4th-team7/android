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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.presentation.common.component.task.TaskTextField

@Composable
fun ProfileView(
    nickNameFieldState: TextFieldState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "닉네임 설정",
            modifier = Modifier.padding(bottom = 12.dp),
            color = HaebomTheme.colors.gray800,
            style = HaebomTheme.typo.hero,
        )

        Text(
            text = "해봄이 불러줄 당신의 이름",
            modifier = Modifier.padding(bottom = 4.dp),
            color = HaebomTheme.colors.gray500,
            style = HaebomTheme.typo.description,
        )

        Text(
            text = "한글 최대 12자 /영문,숫자,특수기호 불가",
            modifier = Modifier.padding(bottom = 52.dp),
            color = HaebomTheme.colors.gray300,
            style = HaebomTheme.typo.helperText,
        )

        TaskTextField(
            fieldState = nickNameFieldState,
            placeholder = "이름을 입력해주세요.",
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileViewPreview() {
    HaebomTheme {
        ProfileView(
            nickNameFieldState = rememberTextFieldState(),
        )
    }
}
