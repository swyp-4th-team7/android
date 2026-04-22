package com.swyp.firsttodo.domain.throwable

sealed class HabitError(
    override val message: String?,
) : Throwable(message) {
    class TitleEmpty(message: String) : HabitError(message) // title 값이 없음 (공백 포함)

    class DurationEmpty(message: String) : HabitError(message) // duration 값이 없음

    class CompletedEmpty(message: String) : HabitError(message) // isCompleted 값이 없음

    class HabitNotFound(message: String) : HabitError(message) // 해당하는 습관 없음

    class RewardEmpty(message: String) : HabitError(message) // 자녀가 reward를 입력하지 않음
}
