package com.swyp.firsttodo.presentation.todo.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.common.extension.widthForScreenPercentage
import com.swyp.firsttodo.core.designsystem.component.HaebomBasicBottomSheet
import com.swyp.firsttodo.core.designsystem.component.HaebomBasicTextField
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.TodoCategory
import com.swyp.firsttodo.presentation.common.component.HaebomLargeButton
import com.swyp.firsttodo.presentation.common.component.task.TaskCategoryList
import com.swyp.firsttodo.presentation.common.component.task.TaskInputSection
import com.swyp.firsttodo.presentation.common.component.task.TaskSheetHeader
import com.swyp.firsttodo.presentation.common.component.task.TaskTextField
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoBottomSheet(
    title: String,
    description: String,
    btnText: String,
    btnEnabled: Boolean,
    selectedCategory: TodoCategory?,
    todoFieldState: TextFieldState,
    dateFieldState: TextFieldState,
    onBtnClick: () -> Unit,
    onCategoryClick: (TodoCategory) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    HaebomBasicBottomSheet(
        onDismiss = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 26.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            TaskSheetHeader(
                title = title,
                description = description,
                onDismiss = onDismiss,
                modifier = Modifier.fillMaxWidth(),
            )

            TaskInputSection(
                title = "할 일",
            ) {
                TaskTextField(
                    fieldState = todoFieldState,
                    placeholder = "할 일을 입력해주세요. (최대 12자)",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                    ),
                    onKeyboardAction = { focusManager.moveFocus(FocusDirection.Next) },
                    maxCount = 12,
                )
            }

            TaskInputSection(
                title = "날짜",
            ) {
                DateTextField(
                    fieldState = dateFieldState,
                )
            }

            TaskInputSection(
                title = "카테고리",
            ) {
                TaskCategoryList(
                    categories = TodoCategory.entries,
                    selectedCategory = selectedCategory,
                    onCategoryClick = onCategoryClick,
                    getDisplayName = { it.displayName },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            HaebomLargeButton(
                text = btnText,
                onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onBtnClick()
                            onDismiss()
                        }
                    }
                },
                enabled = btnEnabled,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun DateTextField(
    fieldState: TextFieldState,
    modifier: Modifier = Modifier,
) {
    HaebomBasicTextField(
        state = fieldState,
        placeholder = "년/월/일",
        modifier = modifier.widthForScreenPercentage(262.dp),
        inputTransformation = InputTransformation {
            if (length > 8) delete(8, length)
            val digits = asCharSequence().filter { it.isDigit() }.toString()
            if (digits != asCharSequence().toString()) replace(0, length, digits)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        lineLimits = TextFieldLineLimits.SingleLine,
        outputTransformation = OutputTransformation {
            if (length >= 5) replace(4, 4, "/")
            if (length >= 8) replace(7, 7, "/")
        },
    )
}

@Preview
@Composable
private fun TodoBottomSheetPreview() {
    var selectedCategory by remember { mutableStateOf<TodoCategory?>(null) }
    val todoFieldState = rememberTextFieldState()
    val dateFieldState = rememberTextFieldState()

    HaebomTheme {
        TodoBottomSheet(
            title = "다가오는 일정 추가",
            description = "잊지 말아야 할 일정이나 약속을 등록해 보세요.",
            btnText = "추가하기",
            btnEnabled = true,
            selectedCategory = selectedCategory,
            todoFieldState = todoFieldState,
            dateFieldState = dateFieldState,
            onBtnClick = {},
            onCategoryClick = { selectedCategory = it },
            onDismiss = {},
        )
    }
}
