package com.swyp.firsttodo.presentation.habit.list

import androidx.lifecycle.viewModelScope
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.domain.model.habit.Habit
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitListViewModel
    @Inject
    constructor() : BaseViewModel<HabitListUiState, HabitListSideEffect>(HabitListUiState()) {
        fun getHabits() {
            updateState { copy(habits = Async.Loading((this.habits as? Async.Success)?.data)) }

            viewModelScope.launch {
                delay(500)

                // TODO: 실제 GET 호출
                val sampleTitles = listOf(
                    "하루에 책 10장 읽기",
                    "매일 아침 스트레칭 10분",
                    "하루에 물 2리터 마시기",
                    "취침 전 일기 쓰기",
                    "매일 영어 단어 20개 외우기",
                    "하루 30분 산책하기",
                    "아침 7시 기상하기",
                    "저녁 식사 후 설거지 바로 하기",
                    "하루 1개 감사한 일 기록하기",
                    "주 3회 근력 운동하기",
                    "매일 스마트폰 사용 2시간 이내",
                    "취침 전 핸드폰 끄기",
                    "매일 과일 한 가지 먹기",
                    "하루 10분 명상하기",
                    "점심 후 10분 낮잠 자기",
                    "매일 계단 이용하기",
                    "주 1회 방 청소하기",
                    "매일 아침 단백질 챙겨 먹기",
                    "하루 1시간 독서하기",
                    "취침 전 스트레칭 5분",
                )
                val sampleRewards = listOf(
                    "가족이랑 놀이공원 가기",
                    "좋아하는 카페 가기",
                    "새 운동화 구매하기",
                    "가족과 외식하기",
                    "영화 보러 가기",
                    "맛집 탐방하기",
                    "원하는 책 구매하기",
                    "주말 하루 쉬기",
                    "좋아하는 디저트 먹기",
                    "가족과 여행 계획 세우기",
                )
                val newHabits = List(20) { index ->
                    Habit(
                        habitId = index.toLong(),
                        duration = HabitDuration.entries[index % HabitDuration.entries.size],
                        isCompleted = index % 3 == 0,
                        title = sampleTitles[index % sampleTitles.size],
                        reward = sampleRewards[index % sampleRewards.size],
                    )
                }

                updateState { copy(habits = if (newHabits.isEmpty()) Async.Empty else Async.Success(newHabits)) }
            }
        }

        fun onCreateClick() {
            sendEffect(HabitListSideEffect.NavigateToHabitDetail(null))
        }

        fun onCheckClick(habit: Habit) {
        }

        fun onEditClick(habit: Habit) {
            sendEffect(HabitListSideEffect.NavigateToHabitDetail(habit))
        }

        fun onDeleteClick(habit: Habit) {
            updateState { copy(delRequestedId = habit.habitId) }
        }

        fun onDeleteConfirm() {
            // TODO: delRequestedId 실제 삭제 로직
            // TODO: 아래 로직은 삭제 성공 시에만
            updateState { copy(delRequestedId = null) }
            sendEffect(HabitListSideEffect.ShowToast("습관 삭제가 완료되었어요."))
            getHabits()
        }

        fun onDeleteCancel() {
            updateState { copy(delRequestedId = null) }
        }
    }
