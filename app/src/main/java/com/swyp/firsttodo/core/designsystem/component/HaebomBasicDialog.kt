
package com.swyp.firsttodo.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun HaebomBasicDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    content: @Composable (() -> Unit),
) {
    val dialogProperties = remember(dismissOnBackPress, dismissOnClickOutside) {
        DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            usePlatformDefaultWidth = false,
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = dialogProperties,
    ) {
        val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window

        SideEffect {
            dialogWindow?.setDimAmount(0.5f)
        }

        Surface(
            modifier = modifier
                .fillMaxWidth()
                .widthIn(max = 640.dp),
            shape = RoundedCornerShape(16.dp),
            color = HaebomTheme.colors.white,
        ) {
            content()
        }
    }
}
