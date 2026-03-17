package com.swyp.firsttodo.presentation.todo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.component.HaebomBasicBottomSheet
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.LabelColor
import com.swyp.firsttodo.domain.model.TodoCategory
import com.swyp.firsttodo.domain.model.TodoChildCategory
import com.swyp.firsttodo.presentation.common.component.HaebomLargeButton
import com.swyp.firsttodo.presentation.common.component.task.TaskCategoryList
import com.swyp.firsttodo.presentation.common.component.task.TaskInputSection
import com.swyp.firsttodo.presentation.common.component.task.TaskSheetHeader
import com.swyp.firsttodo.presentation.common.component.task.TaskTextField
import kotlinx.coroutines.launch

enum class TodoBottomSheetType(
    val title: String,
    val description: String,
    val btnText: String,
) {
    CHILD_CREATE(
        title = "추가 할 일 작성",
        description = "해야 할 일을 추가하고 하나씩 완료해보세요.",
        btnText = "추가하기",
    ),
    CHILD_EDIT(
        title = "할 일 수정하기",
        description = "할 일 정보를 수정하고 업데이트하세요.",
        btnText = "수정하기",
    ),
    PARENT_CREATE(
        title = "추가 할 일 작성",
        description = "해야 할 일을 추가하고 하나씩 완료해보세요.",
        btnText = "추가하기",
    ),
    PARENT_EDIT(
        title = "할 일 수정하기",
        description = "할 일 정보를 수정하고 업데이트하세요.",
        btnText = "수정하기",
    ),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoBottomSheet(
    sheetType: TodoBottomSheetType,
    btnEnabled: Boolean,
    loadingStatus: Async<Unit>,
    categories: List<TodoCategory>,
    selectedCategory: TodoCategory?,
    selectedLabelColor: LabelColor?,
    titleFieldState: TextFieldState,
    onLabelColorClick: (LabelColor) -> Unit,
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
                title = "할 일",
            ) {
                TaskTextField(
                    fieldState = titleFieldState,
                    placeholder = "할 일을 입력해 주세요.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                    ),
                    onKeyboardAction = { focusManager.moveFocus(FocusDirection.Next) },
                )
            }

            TaskInputSection(
                title = "카테고리",
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    TaskCategoryList(
                        categories = categories,
                        selectedCategory = selectedCategory,
                        onCategoryClick = onCategoryClick,
                        getDisplayName = { it.displayName },
                    )
                }
            }

            TaskInputSection(
                title = "색상 선택",
            ) {
                @OptIn(ExperimentalLayoutApi::class)
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    LabelColor.entries.forEach { labelColor ->
                        LabelColorChip(
                            labelColor = labelColor,
                            selected = selectedLabelColor == labelColor,
                            onClick = { onLabelColorClick(labelColor) },
                        )
                    }
                }
            }

            HaebomLargeButton(
                text = sheetType.btnText,
                onClick = {
                    scope.launch {
                        onBtnClick()

                        if (loadingStatus is Async.Success) {
                            onDismiss()
                            sheetState.hide()
                        }
                    }
                },
                enabled = btnEnabled && loadingStatus is Async.Init,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun LabelColorChip(
    labelColor: LabelColor,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (backgroundColor, lineColor) = remember(selected) {
        when (selected) {
            true -> labelColor.background to labelColor.text
            false -> labelColor.completedBackground to labelColor.completedText
        }
    }

    Box(
        modifier = modifier
            .noRippleClickable(onClick)
            .padding(all = 4.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_check),
            contentDescription = null,
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(3.33.dp),
                )
                .border(
                    width = 1.25.dp,
                    color = lineColor,
                    shape = RoundedCornerShape(3.33.dp),
                )
                .padding(all = 10.dp),
            tint = lineColor,
        )
    }
}

private class TodoBottomSheetPreviewProvider : PreviewParameterProvider<TodoBottomSheetType> {
    override val values = sequenceOf(*TodoBottomSheetType.entries.toTypedArray())
}

@Preview
@Composable
private fun TodoBottomSheetPreview(
    @PreviewParameter(TodoBottomSheetPreviewProvider::class) sheetType: TodoBottomSheetType,
) {
    var selectedCategory by remember { mutableStateOf<TodoCategory?>(null) }
    var selectedLabelColor by remember { mutableStateOf<LabelColor?>(null) }
    val titleFieldState = rememberTextFieldState()

    HaebomTheme {
        TodoBottomSheet(
            sheetType = sheetType,
            btnEnabled = titleFieldState.text.isNotBlank() && selectedCategory != null && selectedLabelColor != null,
            loadingStatus = Async.Init,
            categories = TodoChildCategory.entries,
            selectedCategory = selectedCategory,
            selectedLabelColor = selectedLabelColor,
            titleFieldState = titleFieldState,
            onLabelColorClick = { selectedLabelColor = it },
            onBtnClick = {},
            onCategoryClick = { selectedCategory = it },
            onDismiss = {},
        )
    }
}
