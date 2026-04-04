package com.swyp.firsttodo.presentation.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

enum class RoleSelectButtonType(
    val text: String,
    val fstIconRes: Int,
    val scdIconRes: Int,
) {
    PARENT(
        text = "부모입니다.",
        fstIconRes = R.drawable.ic_onboarding_mom_24,
        scdIconRes = R.drawable.ic_onboarding_dad_24,
    ),
    CHILD(
        text = "자녀입니다.",
        fstIconRes = R.drawable.ic_onboarding_boy_24,
        scdIconRes = R.drawable.ic_onboarding_girl_24,
    ),
}

@Composable
fun RoleSelectButton(
    btnType: RoleSelectButtonType,
    onClick: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = HaebomTheme.colors

    val (textColor, backgroundColor, borderColor) = remember(selected, colors) {
        when (selected) {
            true -> Triple(colors.orange500, colors.yellow50, colors.orange500)
            false -> Triple(colors.gray500, colors.white, colors.gray200)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick)
            .heightIn(76.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp),
            )
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 10.dp, vertical = 24.dp)
            .wrapContentWidth(Alignment.CenterHorizontally),
        horizontalArrangement = Arrangement.spacedBy(28.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(btnType.fstIconRes),
                contentDescription = null,
                tint = Color.Unspecified,
            )

            Icon(
                imageVector = ImageVector.vectorResource(btnType.scdIconRes),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }

        Text(
            text = btnType.text,
            color = textColor,
            textAlign = TextAlign.Center,
            style = HaebomTheme.typo.button,
        )
    }
}

private class RoleSelectButtonPreviewProvider : PreviewParameterProvider<Pair<RoleSelectButtonType, Boolean>> {
    override val values = sequenceOf(
        (RoleSelectButtonType.PARENT to true),
        (RoleSelectButtonType.PARENT to false),
        (RoleSelectButtonType.CHILD to true),
        (RoleSelectButtonType.CHILD to false),
    )
}

@Preview
@Composable
private fun RoleSelectButtonPreview(
    @PreviewParameter(RoleSelectButtonPreviewProvider::class) param: Pair<RoleSelectButtonType, Boolean>,
) {
    HaebomTheme {
        RoleSelectButton(
            btnType = param.first,
            onClick = {},
            selected = param.second,
            modifier = Modifier.width(294.dp),
        )
    }
}
