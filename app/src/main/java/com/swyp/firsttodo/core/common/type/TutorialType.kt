package com.swyp.firsttodo.core.common.type

import androidx.annotation.DrawableRes
import com.swyp.firsttodo.R

enum class TutorialType(
    @param:DrawableRes val tooltipIconRes: Int,
    val tooltipEndMarginDp: Int,
) {
    RETRY(
        tooltipIconRes = R.drawable.ic_retry_tooltip,
        tooltipEndMarginDp = 16,
    ),
    SCHEDULE(
        tooltipIconRes = R.drawable.ic_schedule_tooltip,
        tooltipEndMarginDp = 28,
    ),
}
