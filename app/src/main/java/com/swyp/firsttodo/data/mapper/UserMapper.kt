package com.swyp.firsttodo.data.mapper

import com.swyp.firsttodo.data.remote.dto.response.user.MyInfoResponseDto
import com.swyp.firsttodo.domain.model.user.MyInfoModel

fun MyInfoResponseDto.toModel(): MyInfoModel =
    MyInfoModel(
        userId = userId,
        email = email,
        nickname = nickname,
        userType = userType,
        profileCompleted = profileCompleted,
        termsAgreed = termsAgreed,
    )
