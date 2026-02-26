package com.swyp.firsttodo.core.common.extension

import com.swyp.firsttodo.core.base.Async

inline fun <T> Async<T>.onSuccess(block: (T) -> Unit) {
    if (this is Async.Success) {
        block(data)
    }
}

fun <T> Async<T>.getSuccessDataOrNull(): T? {
    return (this as? Async.Success)?.data
}
