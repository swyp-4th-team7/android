package com.swyp.firsttodo.domain.error

sealed class TodoError(
    override val message: String?,
) : Throwable(message) {
    class TitleEmpty(message: String) : TodoError(message) // title 값이 없음 ( 공백 포함)

    class CategoryEmpty(message: String) : TodoError(message) // category 값이 없음

    class ColorEmpty(message: String) : TodoError(message) // color 값이 없음

    class CategoryInvalid(message: String) : TodoError(message) // category 가 유효하지 않음

    class IdNotFound(message: String) : TodoError(message) // 해당하는 할 일 없음
}
