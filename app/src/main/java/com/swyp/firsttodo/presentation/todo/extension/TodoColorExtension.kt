package com.swyp.firsttodo.presentation.todo.extension

import com.swyp.firsttodo.core.designsystem.theme.LabelColor
import com.swyp.firsttodo.domain.model.todo.TodoColor

fun TodoColor.toLabelColor(): LabelColor =
    when (this) {
        TodoColor.PINK -> LabelColor.PINK
        TodoColor.PURPLE -> LabelColor.PURPLE
        TodoColor.BLUE -> LabelColor.BLUE
        TodoColor.SKYBLUE -> LabelColor.SKY_BLUE
        TodoColor.MINT -> LabelColor.MINT
        TodoColor.EMERALD -> LabelColor.MINT
        TodoColor.LIME -> LabelColor.LIME
        TodoColor.YELLOW -> LabelColor.YELLOW
        TodoColor.ORANGE -> LabelColor.ORANGE
        TodoColor.BROWN -> LabelColor.BROWN
        TodoColor.GRAY -> LabelColor.GRAY
    }

fun LabelColor.toTodoColor() =
    when (this) {
        LabelColor.PINK -> TodoColor.PINK
        LabelColor.PURPLE -> TodoColor.PURPLE
        LabelColor.BLUE -> TodoColor.BLUE
        LabelColor.SKY_BLUE -> TodoColor.SKYBLUE
        LabelColor.MINT -> TodoColor.MINT
        LabelColor.LIME -> TodoColor.LIME
        LabelColor.YELLOW -> TodoColor.YELLOW
        LabelColor.ORANGE -> TodoColor.ORANGE
        LabelColor.BROWN -> TodoColor.BROWN
        LabelColor.GRAY -> TodoColor.GRAY
    }
