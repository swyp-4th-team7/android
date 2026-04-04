package com.swyp.firsttodo.data.remote.dto.response.sticker

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChildrenStickerResponseDto(
    @SerialName("children")
    val children: List<ChildStickerResponseDto>,
)

@Serializable
data class ChildStickerResponseDto(
    @SerialName("childId")
    val childId: Long,
    @SerialName("nickname")
    val nickname: String?,
    @SerialName("boardNumber")
    val boardNumber: Int,
    @SerialName("filledSlots")
    val filledSlots: Int,
    @SerialName("boardSize")
    val boardSize: Int,
    @SerialName("startDate")
    val startDate: String?,
)
