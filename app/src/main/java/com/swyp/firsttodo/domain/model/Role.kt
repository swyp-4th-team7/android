package com.swyp.firsttodo.domain.model

enum class Role(
    val request: String,
) {
    PARENT("PARENT"),
    CHILD("CHILD"),
}
