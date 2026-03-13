package com.swyp.firsttodo.domain.model

enum class ScheduleCategory(
    val displayName: String,
    val api: String,
) {
    FINAL_EXAM("기말고사", "FINAL_EXAM"),
    MIDTERM_EXAM("중간고사", "MIDTERM_EXAM"),
    PERFORMANCE_EVALUATION("수행평가", "PERFORMANCE_EVALUATION"),
    CONTEST("대회", "CONTEST"),
}
