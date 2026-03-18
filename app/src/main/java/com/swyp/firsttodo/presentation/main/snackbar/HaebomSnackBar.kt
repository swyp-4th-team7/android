package com.swyp.firsttodo.presentation.main.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

data class HaebomSnackbarVisuals(
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration,
    override val message: String,
    override val withDismissAction: Boolean = false,
) : SnackbarVisuals

@Composable
fun HaebomSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
        snackbar = {
            HaebomSnackbarContent(
                message = it.visuals.message,
            )
        },
    )
}

@Composable
private fun HaebomSnackbarContent(
    message: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = message,
        modifier = modifier
            .sizeIn(screenWidthDp(256.dp), screenHeightDp(40.dp))
            .wrapContentHeight(Alignment.CenterVertically)
            .background(
                color = HaebomTheme.colors.yellow200,
                shape = RoundedCornerShape(20.dp),
            )
            .border(
                width = 1.dp,
                color = HaebomTheme.colors.yellow600,
                shape = RoundedCornerShape(20.dp),
            )
            .padding(all = 8.dp),
        color = HaebomTheme.colors.yellow600,
        textAlign = TextAlign.Center,
        style = HaebomTheme.typo.section,
    )
}

val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided")
}

private class HaebomSnackBarPreviewProvider : PreviewParameterProvider<String> {
    override val values = sequenceOf("할 일이 추가되었습니다.", "할 일이 추가되었습니다. 할 일이 추가되었습니다. 할 일이 추가되었습니다.")
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun HaebomSnackbarContentPreview(
    @PreviewParameter(HaebomSnackBarPreviewProvider::class) text: String,
) {
    HaebomTheme {
        HaebomSnackbarContent(
            message = text,
        )
    }
}
