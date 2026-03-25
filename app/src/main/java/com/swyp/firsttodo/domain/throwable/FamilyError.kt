package com.swyp.firsttodo.domain.throwable

sealed class FamilyError(
    override val message: String?,
) : Throwable(message) {
    class InviteCodeEmpty(message: String) : FamilyError(message) // 초대 코드가 비어있음

    class InviteCodeMySelf(message: String) : FamilyError(message) // 자신의 초대 코드 입력함

    class InviteCodeInvalid(message: String) : FamilyError(message) // 초대코드가 유효하지 않음

    class InviteAlreadyDone(message: String) : FamilyError(message) // 이미 연결된 유저

    class ConnectInvalid(message: String) : FamilyError(message) // 해당하는 유저랑 연결 안되어있음

    class OnboardingUncompleted(message: String) : FamilyError(message) // 온보딩 미완료됨
}
