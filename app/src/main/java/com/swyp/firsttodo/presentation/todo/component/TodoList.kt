package com.swyp.firsttodo.presentation.todo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.component.HaebomLabel
import com.swyp.firsttodo.core.common.component.TaskItemPopup
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.LabelColor
import com.swyp.firsttodo.domain.model.todo.TodoCategoryModel

data class TodayTodoUiModel(
    val todoId: Long,
    val title: String,
    val completed: Boolean,
    val category: TodoCategoryModel,
    val labelColor: LabelColor,
)

@Composable
fun TodoList(
    todos: Async<List<TodayTodoUiModel>>,
    onPlusClick: () -> Unit,
    onCheckClick: (TodayTodoUiModel) -> Unit,
    onEditClick: (TodayTodoUiModel) -> Unit,
    onDeleteClick: (TodayTodoUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    TodoCard(
        title = "오늘의 할 일",
        onPlusClick = onPlusClick,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            when (todos) {
                is Async.Empty -> TodoCardEmptyContent(
                    text = "+를 눌러 오늘 할 일을 적어 주세요!",
                )

                else -> when (val data = todos.getDataOrNull()) {
                    null -> Spacer(Modifier.height(56.dp))

                    else -> data.forEach { todo ->
                        key(todo.todoId) {
                            TodoItem(
                                todo = todo,
                                onCheckClick = { onCheckClick(todo) },
                                onEditClick = { onEditClick(todo) },
                                onDeleteClick = { onDeleteClick(todo) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TodoItem(
    todo: TodayTodoUiModel,
    onCheckClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showPopup by remember { mutableStateOf(false) }

    val (iconRes, labelTextColor, labelBgColor) = remember(todo.completed, todo.labelColor) {
        if (todo.completed) {
            Triple(R.drawable.ic_check_filled, todo.labelColor.completedText, todo.labelColor.completedBackground)
        } else {
            Triple(R.drawable.ic_check_unfilled, todo.labelColor.text, todo.labelColor.background)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(42.dp)
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null,
            modifier = Modifier
                .noRippleClickable(onCheckClick)
                .fillMaxHeight()
                .background(
                    color = Color(0xFFFAFAFA),
                    shape = RoundedCornerShape(4.dp),
                )
                .padding(all = 12.dp),
            tint = Color.Unspecified,
        )

        Box {
            Row(
                modifier = Modifier
                    .noRippleClickable({ showPopup = true })
                    .background(
                        color = HaebomTheme.colors.white,
                        shape = RoundedCornerShape(4.dp),
                    )
                    .padding(horizontal = 8.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = todo.title,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    color = if (todo.completed) HaebomTheme.colors.gray200 else HaebomTheme.colors.gray700,
                    style = HaebomTheme.typo.description,
                )

                HaebomLabel(
                    textColor = labelTextColor,
                    backgroundColor = labelBgColor,
                    text = todo.category.label,
                )
            }

            if (showPopup) {
                TaskItemPopup(
                    onFirstClick = onEditClick,
                    onDeleteClick = onDeleteClick,
                    onDismiss = { showPopup = false },
                )
            }
        }
    }
}

private val previewCategory = TodoCategoryModel(name = "STUDY", label = "공부")

private class TodoListPreviewProvider : PreviewParameterProvider<Async<List<TodayTodoUiModel>>> {
    override val values = sequenceOf(
        Async.Success(
            listOf(
                TodayTodoUiModel(1L, "수학 숙제 풀기", completed = false, previewCategory, LabelColor.BLUE),
                TodayTodoUiModel(
                    2L,
                    "30분 조깅하고 스트레칭까지 빠짐없이 하기",
                    completed = false,
                    previewCategory,
                    LabelColor.PINK,
                ),
                TodayTodoUiModel(3L, "방 청소 및 책상 정리", completed = true, previewCategory, LabelColor.ORANGE),
                TodayTodoUiModel(
                    4L,
                    "오늘 읽을 책 30페이지 독서하기 오늘 읽을 책 30페이지 독서하기",
                    completed = true,
                    previewCategory,
                    LabelColor.PURPLE,
                ),
            ),
        ),
        Async.Empty,
    )
}

@Preview(showBackground = true)
@Composable
private fun TodoListPreview(
    @PreviewParameter(TodoListPreviewProvider::class) todos: Async<List<TodayTodoUiModel>>,
) {
    HaebomTheme {
        TodoList(
            todos = todos,
            onPlusClick = {},
            onCheckClick = {},
            onEditClick = {},
            onDeleteClick = {},
        )
    }
}
