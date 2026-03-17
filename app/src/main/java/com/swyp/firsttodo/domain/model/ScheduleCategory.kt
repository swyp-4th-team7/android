package com.swyp.firsttodo.domain.model

enum class ScheduleCategory(
    val displayName: String,
    val request: String,
) {
    SCHOOL_EXAM("학교시험", "SCHOOL_EXAM"),
    ACADEMY_EXAM("학원시험", "ACADEMY_EXAM"),
    OUT_EXAM("외부시험", "PERFORMANCE_EVALUATION"),
    CONTEST("대회·경시", "CONTEST"),
}
