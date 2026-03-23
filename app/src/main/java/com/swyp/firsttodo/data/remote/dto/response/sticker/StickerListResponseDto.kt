package com.swyp.firsttodo.data.remote.dto.response.sticker

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StickerListResponseDto(
    @SerialName("weekLabel")
    val weekLabel: String,
    @SerialName("weekOffset")
    val weekOffset: Int,
    @SerialName("startDate")
    val startDate: String,
    @SerialName("endDate")
    val endDate: String,
    @SerialName("stickers")
    val stickers: List<StickerResponseDto>,
)

@Serializable
data class StickerResponseDto(
    @SerialName("date")
    val date: String,
    @SerialName("stickerCode")
    val stickerCode: String?,
)
