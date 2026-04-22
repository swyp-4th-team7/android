package com.swyp.firsttodo.core.base

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

abstract class BaseViewModel<S : UiState, E : UiEffect>(
    initialState: S,
) : ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _sideEffect = Channel<E>(Channel.BUFFERED)
    val sideEffect: Flow<E> = _sideEffect.receiveAsFlow()

    private val lastEffectTime = ConcurrentHashMap<String, Long>()

    protected fun updateState(reducer: S.() -> S) {
        _uiState.update { it.reducer() }
    }

    // 일반 이벤트 (스로틀링 미적용, Snackbar 등에 사용)
    protected fun sendEffect(effect: E) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }

    // 스로틀링 전용 이벤트 (Navigation 등에 사용)
    protected fun sendThrottledEffect(
        effect: E,
        throttleMillis: Long = 500L,
        customKey: String? = null,
    ) {
        val key = customKey ?: effect::class.qualifiedName ?: return

        val now = SystemClock.elapsedRealtime()
        if (now - (lastEffectTime[key] ?: 0L) < throttleMillis) return

        lastEffectTime[key] = now

        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}
