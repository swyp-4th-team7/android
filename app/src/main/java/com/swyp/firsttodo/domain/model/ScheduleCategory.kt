package com.swyp.firsttodo.domain.model

sealed interface ScheduleCategory {
    val displayName: String
    val api: String
}

enum class ScheduleChildCategory(
    override val displayName: String,
    override val api: String,
) : ScheduleCategory {
    FINAL_EXAM("기말고사", "FINAL_EXAM"),
    MIDTERM_EXAM("중간고사", "MIDTERM_EXAM"),
    PERFORMANCE_EVALUATION("수행평가", "PERFORMANCE_EVALUATION"),
    CONTEST("대회", "CONTEST"),
}

enum class ScheduleParentCategory(
    override val displayName: String,
    override val api: String,
) : ScheduleCategory {
    FINAL_EXAM("기말고사", "FINAL_EXAM"),
    MIDTERM_EXAM("중간고사", "MIDTERM_EXAM"),
    PERFORMANCE_EVALUATION("수행평가", "PERFORMANCE_EVALUATION"),
    CONTEST("대회", "CONTEST"),
}
