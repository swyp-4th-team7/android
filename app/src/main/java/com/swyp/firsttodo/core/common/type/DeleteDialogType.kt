package com.swyp.firsttodo.core.common.type

sealed class DeleteDialogType(
    val title: String,
    val description: String,
) {
    data object Todo : DeleteDialogType(
        title = "선택한 할 일을 삭제할까요?",
        description = "입력한 할 일이 사라져요!",
    )

    data object Schedule : DeleteDialogType(
        title = "선택한 일정을 삭제할까요?",
        description = "입력한 일정이 사라져요!",
    )

    data object Habit : DeleteDialogType(
        title = "선택한 습관을 삭제할까요?",
        description = "입력한 습관이 사라져요!",
    )

    data object FailedHabit : DeleteDialogType(
        title = "실패한 습관을 삭제할까요?",
        description = "실패한 습관이 사라져요!",
    )

    class Disconnect(nickname: String) : DeleteDialogType(
        title = "${nickname}님과의 연결을 끊을까요?",
        description = "연동이 끊기면 ${nickname}님의\n활동을 볼 수 없습니다.",
    )
}
