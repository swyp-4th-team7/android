package com.swyp.firsttodo.core.designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HaebomBasicBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    content: @Composable (ColumnScope.() -> Unit),
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = sheetState,
        properties = properties,
        contentWindowInsets = contentWindowInsets,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = HaebomTheme.colors.white,
        scrimColor = HaebomTheme.colors.black.copy(alpha = 0.5f),
        dragHandle = null,
    ) {
        content()
    }
}
