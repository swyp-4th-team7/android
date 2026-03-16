package com.swyp.firsttodo.presentation.onboarding.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.Role

@Composable
fun RoleSelectView(
    selectedRole: Role?,
    onRoleClick: (Role) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "반갑습니다!\n당신의 역할은 무엇인가요?",
            modifier = Modifier.padding(bottom = 12.dp),
            color = HaebomTheme.colors.gray800,
            textAlign = TextAlign.Center,
            style = HaebomTheme.typo.hero,
        )

        Text(
            text = "당신의 역할을 선택해주세요.",
            modifier = Modifier.padding(bottom = 50.dp),
            color = HaebomTheme.colors.gray500,
            style = HaebomTheme.typo.description,
        )

        RoleSelectButton(
            text = "부모입니다.",
            onClick = { onRoleClick(Role.PARENT) },
            selected = selectedRole == Role.PARENT,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
        )

        RoleSelectButton(
            text = "자녀입니다.",
            onClick = { onRoleClick(Role.CHILD) },
            selected = selectedRole == Role.CHILD,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RoleSelectViewPreview() {
    HaebomTheme {
        RoleSelectView(
            selectedRole = Role.CHILD,
            onRoleClick = {},
        )
    }
}
