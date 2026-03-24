package com.swyp.firsttodo.data.mapper

import com.swyp.firsttodo.data.remote.dto.response.sticker.ChildStickerResponseDto
import com.swyp.firsttodo.data.remote.dto.response.sticker.ChildrenStickerResponseDto
import com.swyp.firsttodo.data.remote.dto.response.sticker.StickerBoardResponseDto
import com.swyp.firsttodo.data.remote.dto.response.sticker.StickerListResponseDto
import com.swyp.firsttodo.data.remote.dto.response.sticker.StickerResponseDto
import com.swyp.firsttodo.domain.model.sticker.ChildStickerModel
import com.swyp.firsttodo.domain.model.sticker.StickerBoardModel
import com.swyp.firsttodo.domain.model.sticker.WeeklyStickerModel
import com.swyp.firsttodo.domain.model.sticker.WeeklyStickersModel

fun StickerListResponseDto.toModel(): WeeklyStickersModel =
    WeeklyStickersModel(
        weekLabel = this.weekLabel,
        weekOffset = this.weekOffset,
        startDate = this.startDate,
        endDate = this.endDate,
        stickers = this.stickers.map { it.toModel() },
    )

fun StickerResponseDto.toModel(): WeeklyStickerModel =
    WeeklyStickerModel(
        date = this.date,
        stickerCode = this.stickerCode,
    )

fun StickerBoardResponseDto.toModel(): StickerBoardModel =
    StickerBoardModel(
        boardSize = this.boardSize,
        filledSlotCount = this.filledSlotCount,
        showCompletionPopup = this.showCompletionPupup,
    )

fun ChildrenStickerResponseDto.toModel(): List<ChildStickerModel> = this.children.map { it.toModel() }

fun ChildStickerResponseDto.toModel(): ChildStickerModel =
    ChildStickerModel(
        childId = this.childId,
        nickname = this.nickname,
        boardNumber = this.boardNumber,
        filledSlots = this.filledSlots,
        boardSize = this.boardSize,
        startDate = this.startDate,
    )
