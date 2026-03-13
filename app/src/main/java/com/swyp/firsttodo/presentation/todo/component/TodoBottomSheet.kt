package com.swyp.firsttodo.presentation.todo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.common.extension.widthForScreenPercentage
import com.swyp.firsttodo.core.designsystem.theme.BoldStyle
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.RegularStyle
import com.swyp.firsttodo.core.designsystem.theme.SemiBoldStyle
import com.swyp.firsttodo.presentation.common.component.HaebomBasicTextField
import com.swyp.firsttodo.presentation.common.component.HaebomLargeButton
import com.swyp.firsttodo.presentation.common.component.HaebomTag
import kotlinx.coroutines.launch

enum class TodoCategory(
    val displayName: String,
) {
    FINAL_EXAM("기말고사"),
    MIDTERM_EXAM("중간고사"),
    PERFORMANCE_EVALUATION("수행평가"),
    CONTEST("대회"),
}

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
    val colors = HaebomTheme.colors

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = colors.white,
        scrimColor = colors.black.copy(alpha = 0.5f),
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 26.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 12.dp, bottom = 4.dp)
                        .size(32.dp, 4.dp)
                        .background(
                            color = Color(0xFF79747E),
                            shape = CircleShape,
                        ),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = title,
                        color = colors.black,
                        style = BoldStyle.copy(fontSize = 20.sp),
                    )

                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_close_24),
                        contentDescription = null,
                        modifier = Modifier
                            .noRippleClickable(onDismiss)
                            .padding(all = 12.dp),
                        tint = colors.gray400,
                    )
                }

                Text(
                    text = description,
                    color = colors.gray400,
                    style = RegularStyle.copy(fontSize = 14.sp),
                )
            }

            InputSection(
                title = "할 일",
            ) {
                TodoTextField(
                    fieldState = todoFieldState,
                )
            }

            InputSection(
                title = "날짜",
            ) {
                DateTextField(
                    fieldState = dateFieldState,
                )
            }

            InputSection(
                title = "카테고리",
            ) {
                CategoryList(
                    selctedCategory = selectedCategory,
                    onCategoryClick = onCategoryClick,
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
private fun InputSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = title,
            color = HaebomTheme.colors.black,
            style = SemiBoldStyle.copy(fontSize = 18.sp),
        )

        content()
    }
}

@Composable
private fun TodoTextField(
    fieldState: TextFieldState,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    HaebomBasicTextField(
        state = fieldState,
        placeholder = "다가오는 일정을 입력해주세요. (최대 12자)",
        modifier = modifier.fillMaxWidth(),
        inputTransformation = InputTransformation {
            if (length > 12) delete(12, length)
            val text = asCharSequence().toString()
            val filtered = text
                .filter { !it.isSurrogate() && Character.getType(it.code) != Character.OTHER_SYMBOL.toInt() }
            if (filtered != text) replace(0, length, filtered)
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
        ),
        onKeyboardAction = KeyboardActionHandler {
            focusManager.moveFocus(FocusDirection.Next)
        },
        lineLimits = TextFieldLineLimits.SingleLine,
    )
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoryList(
    selctedCategory: TodoCategory?,
    onCategoryClick: (TodoCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TodoCategory.entries.forEach { category ->
            HaebomTag(
                label = category.displayName,
                onClick = { onCategoryClick(category) },
                selected = category == selctedCategory,
            )
        }
    }
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
