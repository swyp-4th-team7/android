package com.swyp.firsttodo.presentation.todo.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.designsystem.component.HaebomBasicBottomSheet
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.ScheduleCategory
import com.swyp.firsttodo.presentation.common.component.HaebomLargeButton
import com.swyp.firsttodo.presentation.common.component.HaebomTag
import com.swyp.firsttodo.presentation.common.component.task.TaskInputSection
import com.swyp.firsttodo.presentation.common.component.task.TaskSheetHeader
import com.swyp.firsttodo.presentation.common.component.task.TaskTextField

enum class ScheduleBottomSheetType(
    val title: String,
    val description: String,
    val btnText: String,
) {
    CHILD_CREATE(
        title = "다가오는 일정 추가 하기",
        description = "잊지 말아야 할 일정이나 약속을 등록해 보세요.",
        btnText = "추가하기",
    ),
    CHILD_EDIT(
        title = "다가오는 일정 수정하기",
        description = "다가오는 일정 정보를 수정하고 업데이트하세요.",
        btnText = "수정하기",
    ),
    PARENT_CREATE(
        title = "자녀 일정 관리 추가",
        description = "자녀의 잊지 말아야 할 중요한 일정을 등록해 보세요.",
        btnText = "추가하기",
    ),
    PARENT_EDIT(
        title = "자녀 일정 관리 수정",
        description = "자녀 일정을 수정하고 업데이트하세요.",
        btnText = "수정하기",
    ),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleBottomSheet(
    sheetType: ScheduleBottomSheetType,
    btnEnabled: Boolean,
    loadingStatus: Async<Unit>,
    selectedCategory: ScheduleCategory?,
    titleFieldState: TextFieldState,
    dateFieldState: TextFieldState,
    dateErrorText: String?,
    onBtnClick: () -> Unit,
    onCategoryClick: (ScheduleCategory) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(loadingStatus) {
        if (loadingStatus is Async.Success) {
            sheetState.hide()
            onDismiss()
        }
    }

    HaebomBasicBottomSheet(
        onDismiss = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp)
                .verticalScroll(scrollState),
        ) {
            TaskSheetHeader(
                title = sheetType.title,
                description = sheetType.description,
                onDismiss = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp),
            )

            TaskInputSection(
                title = "일정",
            ) {
                TaskTextField(
                    fieldState = titleFieldState,
                    placeholder = "다가오는 일정을 입력해주세요.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp),
                    sampleText = "예시: 영어 시험, 국어 시험, 논술 대회...",
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                    ),
                    onKeyboardAction = { focusManager.moveFocus(FocusDirection.Next) },
                )
            }

            TaskInputSection(
                title = "날짜",
            ) {
                TaskTextField(
                    fieldState = dateFieldState,
                    placeholder = "년/월/일",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp),
                    errorText = dateErrorText,
                    inputTransformation = InputTransformation {
                        if (length > 8) delete(8, length)
                        val digits = asCharSequence().filter { it.isDigit() }.toString()
                        if (digits != asCharSequence().toString()) replace(0, length, digits)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    outputTransformation = OutputTransformation {
                        if (length >= 5) replace(4, 4, "/")
                        if (length >= 8) replace(7, 7, "/")
                    },
                )
            }

            TaskInputSection(
                title = "카테고리",
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    ScheduleCategory.entries.chunked(3).forEach { rowItems ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                        ) {
                            rowItems.forEach { category ->
                                HaebomTag(
                                    label = category.displayName,
                                    onClick = { onCategoryClick(category) },
                                    selected = selectedCategory == category,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                            repeat(3 - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            HaebomLargeButton(
                text = sheetType.btnText,
                onClick = onBtnClick,
                enabled = btnEnabled && loadingStatus !is Async.Loading,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private class ScheduleBottomSheetPreviewProvider : PreviewParameterProvider<ScheduleBottomSheetType> {
    override val values = sequenceOf(*ScheduleBottomSheetType.entries.toTypedArray())
}

@Preview
@Composable
private fun ScheduleBottomSheetPreview(
    @PreviewParameter(ScheduleBottomSheetPreviewProvider::class) sheetType: ScheduleBottomSheetType,
) {
    var selectedCategory by remember { mutableStateOf<ScheduleCategory?>(null) }
    val titleFieldState = rememberTextFieldState()
    val dateFieldState = rememberTextFieldState()

    HaebomTheme {
        ScheduleBottomSheet(
            sheetType = sheetType,
            btnEnabled = true,
            loadingStatus = Async.Init,
            selectedCategory = selectedCategory,
            titleFieldState = titleFieldState,
            dateFieldState = dateFieldState,
            dateErrorText = null,
            onBtnClick = {},
            onCategoryClick = { selectedCategory = it },
            onDismiss = {},
        )
    }
}
