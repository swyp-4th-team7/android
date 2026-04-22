package com.swyp.firsttodo.presentation.todo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.common.extension.skeleton
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.component.HaebomBasicBottomSheet
import com.swyp.firsttodo.core.designsystem.component.HaebomBottomSheetHandle
import com.swyp.firsttodo.core.designsystem.theme.BoldStyle
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.LabelColor
import com.swyp.firsttodo.core.designsystem.theme.RegularStyle
import com.swyp.firsttodo.core.designsystem.theme.SemiBoldStyle
import com.swyp.firsttodo.domain.model.todo.TodoCategoryModel
import com.swyp.firsttodo.presentation.common.component.HaebomLargeButton
import com.swyp.firsttodo.presentation.common.component.HaebomMultiLineTextField
import com.swyp.firsttodo.presentation.common.component.task.TaskCategoryList
import com.swyp.firsttodo.presentation.main.snackbar.HaebomSnackbarHost
import com.swyp.firsttodo.presentation.main.snackbar.LocalSnackbarHostState

enum class TodoBottomSheetType(
    val title: String,
    val description: String,
    val btnText: String,
) {
    CHILD_CREATE(
        title = "추가 할 일 작성 하기",
        description = "해야 할 일을 추가하고 하나씩 완료해 보세요.",
        btnText = "추가하기",
    ),
    CHILD_EDIT(
        title = "할 일 수정하기",
        description = "할 일 정보를 수정하고 업데이트하세요.",
        btnText = "수정하기",
    ),
    PARENT_CREATE(
        title = "추가 할 일 작성",
        description = "해야 할 일을 추가하고 하나씩 완료해 보세요.",
        btnText = "추가하기",
    ),
    PARENT_EDIT(
        title = "할 일 수정",
        description = "할 일 정보를 수정하고 업데이트하세요.",
        btnText = "수정하기",
    ),
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TodoBottomSheet(
    sheetType: TodoBottomSheetType,
    btnEnabled: Boolean,
    loadingStatus: Async<Unit>,
    categories: List<TodoCategoryModel>,
    selectedCategory: TodoCategoryModel?,
    selectedLabelColor: LabelColor?,
    titleFieldState: TextFieldState,
    onLabelColorClick: (LabelColor) -> Unit,
    onBtnClick: () -> Unit,
    onCategoryClick: (TodoCategoryModel) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()
    val snackbarHostState = LocalSnackbarHostState.current
    val isLoading = loadingStatus is Async.Loading
    val colors = HaebomTheme.colors

    LaunchedEffect(loadingStatus) {
        if (loadingStatus is Async.Success) {
            sheetState.hide()
            onDismiss()
        }
    }

    HaebomBasicBottomSheet(
        onDismiss = onDismiss,
        sheetState = sheetState,
        contentWindowInsets = { WindowInsets(0) },
        modifier = modifier,
    ) {
        Box(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBarsIgnoringVisibility)) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
            ) {
                HaebomBottomSheetHandle()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    if (isLoading) {
                        Spacer(
                            Modifier
                                .size(161.dp, 29.dp)
                                .skeleton(),
                        )
                    } else {
                        Text(
                            text = sheetType.title,
                            color = colors.black,
                            style = BoldStyle.copy(fontSize = 20.sp),
                        )
                    }

                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_close_24),
                        contentDescription = null,
                        modifier = Modifier
                            .noRippleClickable(onDismiss)
                            .padding(all = 12.dp),
                        tint = colors.black,
                    )
                }

                if (isLoading) {
                    Spacer(Modifier.height(2.dp))
                    Spacer(
                        Modifier
                            .size(241.dp, 20.dp)
                            .skeleton(),
                    )
                } else {
                    Text(
                        text = sheetType.description,
                        color = colors.gray400,
                        style = RegularStyle.copy(fontSize = 14.sp),
                    )
                }

                Spacer(Modifier.height(28.dp))

                if (isLoading) {
                    Spacer(
                        Modifier
                            .size(56.dp, 26.dp)
                            .skeleton(),
                    )
                } else {
                    Text(
                        text = "할 일",
                        color = colors.black,
                        style = SemiBoldStyle.copy(fontSize = 18.sp),
                    )
                }

                Spacer(Modifier.height(8.dp))

                if (isLoading) {
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .skeleton(),
                    )
                } else {
                    HaebomMultiLineTextField(
                        fieldState = titleFieldState,
                        placeholder = "할 일을 입력해 주세요.",
                        modifier = Modifier
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    )
                }

                if (isLoading) {
                    Spacer(
                        Modifier
                            .size(70.dp, 28.dp)
                            .skeleton(),
                    )
                } else {
                    Text(
                        text = "카테고리",
                        color = colors.black,
                        style = SemiBoldStyle.copy(fontSize = 18.sp),
                    )
                }

                Spacer(Modifier.height(8.dp))

                if (isLoading) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        categories.forEach { _ ->
                            Spacer(
                                Modifier
                                    .size(64.dp, 32.dp)
                                    .skeleton(),
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                } else {
                    Column(
                        modifier = Modifier.padding(bottom = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        TaskCategoryList(
                            categories = categories,
                            selectedCategory = selectedCategory,
                            onCategoryClick = onCategoryClick,
                            getDisplayName = { it.label },
                        )
                    }
                }

                if (isLoading) {
                    Spacer(
                        Modifier
                            .size(70.dp, 26.dp)
                            .skeleton(),
                    )
                } else {
                    Text(
                        text = "색상 선택",
                        color = colors.black,
                        style = SemiBoldStyle.copy(fontSize = 18.sp),
                    )
                }

                Spacer(Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 22.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    LabelColor.entries.forEach { labelColor ->
                        if (isLoading) {
                            Spacer(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(44.dp)
                                    .skeleton(),
                            )
                        } else {
                            LabelColorChip(
                                labelColor = labelColor,
                                selected = selectedLabelColor == labelColor,
                                onClick = { onLabelColorClick(labelColor) },
                            )
                        }
                    }
                }

                if (isLoading) {
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .skeleton(),
                    )
                } else {
                    HaebomLargeButton(
                        text = sheetType.btnText,
                        onClick = onBtnClick,
                        enabled = btnEnabled,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Spacer(Modifier.height(20.dp))
            }

            HaebomSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = screenHeightDp(40.dp))
                    .padding(horizontal = screenWidthDp(16.dp)),
            )
        }
    }
}

@Composable
private fun LabelColorChip(
    labelColor: LabelColor,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (backgroundColor, lineColor) = remember(selected, labelColor) {
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

private data class TodoBottomSheetPreviewParam(
    val sheetType: TodoBottomSheetType,
    val loadingStatus: Async<Unit>,
)

private class TodoBottomSheetPreviewProvider : PreviewParameterProvider<TodoBottomSheetPreviewParam> {
    override val values = sequenceOf(
        *TodoBottomSheetType.entries.map { TodoBottomSheetPreviewParam(it, Async.Init) }.toTypedArray(),
        TodoBottomSheetPreviewParam(TodoBottomSheetType.PARENT_CREATE, Async.Loading()),
    )
}

@Preview
@Composable
private fun TodoBottomSheetPreview(
    @PreviewParameter(TodoBottomSheetPreviewProvider::class) param: TodoBottomSheetPreviewParam,
) {
    var selectedCategory by remember { mutableStateOf<TodoCategoryModel?>(null) }
    var selectedLabelColor by remember { mutableStateOf<LabelColor?>(null) }
    val titleFieldState = rememberTextFieldState()
    val categories = listOf(
        TodoCategoryModel("공부", "공부"),
        TodoCategoryModel("숙제", "숙제"),
        TodoCategoryModel("운동", "운동"),
        TodoCategoryModel("정리", "정리"),
        TodoCategoryModel("독서", "독서"),
        TodoCategoryModel("집안일", "집안일"),
        TodoCategoryModel("창의활동", "창의활동"),
    )

    HaebomTheme {
        TodoBottomSheet(
            sheetType = param.sheetType,
            btnEnabled = titleFieldState.text.isNotBlank() && selectedCategory != null && selectedLabelColor != null,
            loadingStatus = param.loadingStatus,
            categories = categories,
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
