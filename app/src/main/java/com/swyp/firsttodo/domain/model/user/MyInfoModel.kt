package com.swyp.firsttodo.domain.model.user

data class MyInfoModel(
    val userId: Long,
    val email: String,
    val nickname: String?,
    val userType: String?,
    val profileCompleted: Boolean,
    val termsAgreed: Boolean,
)
