package com.swyp.firsttodo.domain.error

sealed class RewardError(
    override val message: String?,
) : Throwable(message) {
    class RewardValueEmpty(message: String) : RewardError(message) // reward 값이 없음

    class AccessDenied(message: String) : RewardError(message) // 접근 권한이 없음

    class RewardNotFound(message: String) : RewardError(message) // 유저 또는 리워드를 찾을 수 없음

    class InvalidStatus(message: String) : RewardError(message) // 현재 상태가 기대값과 달라 상태 변경 불가
}
