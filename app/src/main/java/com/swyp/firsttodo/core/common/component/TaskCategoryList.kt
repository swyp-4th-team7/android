package com.swyp.firsttodo.core.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> TaskCategoryList(
    categories: ImmutableList<T>,
    selectedCategory: T?,
    onCategoryClick: (T) -> Unit,
    getDisplayName: (T) -> String,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        categories.forEach { category ->
            HaebomTag(
                label = getDisplayName(category),
                onClick = { onCategoryClick(category) },
                selected = category == selectedCategory,
                modifier = Modifier.widthIn(64.dp),
            )
        }
    }
}
