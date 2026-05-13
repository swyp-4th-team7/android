package com.swyp.firsttodo.domain.error

sealed class ScheduleError(
    override val message: String?,
) : Throwable(message) {
    class TitleEmpty(message: String) : ScheduleError(message) // 제목 누락

    class CategoryEmpty(message: String) : ScheduleError(message) // 카테고리 누락

    class DateEmpty(message: String) : ScheduleError(message) // 날짜 누락

    class ScheduleNotFound(message: String) : ScheduleError(message) // 찾을 수 없는 일정
}
