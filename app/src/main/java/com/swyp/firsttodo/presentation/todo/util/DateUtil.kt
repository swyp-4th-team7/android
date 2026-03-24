package com.swyp.firsttodo.presentation.todo.util

import androidx.core.text.isDigitsOnly
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

fun String.toDateOrNull(): LocalDate? {
    return try {
        if (!this.isDigitsOnly() || this.length != 8) return null
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        return LocalDate.parse(this, formatter)
    } catch (_: DateTimeParseException) {
        null
    }
}

fun LocalDate.isTodayOrAfter(): Boolean {
    val today = LocalDate.now()
    return this.isAfter(today) || this.isEqual(today)
}

// yyyyMMdd -> yyyy-MM-dd
fun String.toDashedDate(): String {
    if (this.length != 8) return this
    return "${this.substring(0, 4)}-${this.substring(4, 6)}-${this.substring(6, 8)}"
}

// yyyy-MM-dd -> yyyyMMdd
fun String.removeDashes(): String = replace("-", "")

// yyyy-MM-dd -> "2026.03.12.일요일"
fun String.toDisplayDate(): String {
    return try {
        val input = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val output = DateTimeFormatter.ofPattern("yyyy.MM.dd.EEEE", Locale.KOREAN)
        LocalDate.parse(this, input).format(output)
    } catch (_: DateTimeParseException) {
        this
    }
}
