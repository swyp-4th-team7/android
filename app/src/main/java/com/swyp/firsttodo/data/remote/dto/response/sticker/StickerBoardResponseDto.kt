package com.swyp.firsttodo.data.remote.dto.response.sticker

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StickerBoardResponseDto(
    @SerialName("boardSize")
    val boardSize: Int,
    @SerialName("filledSlotCount")
    val filledSlotCount: Int,
    @SerialName("showCompletePopup")
    val showCompletePopup: Boolean,
)
