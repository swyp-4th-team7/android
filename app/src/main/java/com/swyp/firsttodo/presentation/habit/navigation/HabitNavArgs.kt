package com.swyp.firsttodo.presentation.habit.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.domain.model.habit.HabitModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class HabitNavArgs(
    val habitId: Long,
    val duration: SerializableHabitDuration,
    val isCompleted: Boolean,
    val title: String,
    val reward: String?,
)

@Serializable
enum class SerializableHabitDuration {
    THREE_DAYS,
    SEVEN_DAYS,
    FOURTEEN_DAYS,
    TWENTYONE_DAYS,
    SIXTYSIX_DAYS,
    NINETYNINE_DAYS,
}

val HabitNavArgsNavType = object : NavType<HabitNavArgs?>(isNullableAllowed = true) {
    override fun get(
        bundle: Bundle,
        key: String,
    ): HabitNavArgs? = bundle.getString(key)?.let { Json.decodeFromString(it) }

    override fun parseValue(value: String): HabitNavArgs? {
        val decoded = Uri.decode(value)
        if (decoded == "null") return null
        return Json.decodeFromString(decoded)
    }

    override fun serializeAsValue(value: HabitNavArgs?): String =
        if (value == null) "null" else Uri.encode(Json.encodeToString(HabitNavArgs.serializer(), value))

    override fun put(
        bundle: Bundle,
        key: String,
        value: HabitNavArgs?,
    ) {
        bundle.putString(key, value?.let { Json.encodeToString(HabitNavArgs.serializer(), it) })
    }
}

fun HabitModel.toNavArgs() =
    HabitNavArgs(
        habitId = habitId,
        duration = duration.toSerializable(),
        isCompleted = isCompleted,
        title = title,
        reward = reward,
    )

fun HabitNavArgs.toModel() =
    HabitModel(
        habitId = habitId,
        duration = duration.toDomain(),
        isCompleted = isCompleted,
        title = title,
        reward = reward,
    )

private fun HabitDuration.toSerializable() =
    when (this) {
        HabitDuration.THREE_DAYS -> SerializableHabitDuration.THREE_DAYS
        HabitDuration.SEVEN_DAYS -> SerializableHabitDuration.SEVEN_DAYS
        HabitDuration.FOURTEEN_DAYS -> SerializableHabitDuration.FOURTEEN_DAYS
        HabitDuration.TWENTYONE_DAYS -> SerializableHabitDuration.TWENTYONE_DAYS
        HabitDuration.SIXTYSIX_DAYS -> SerializableHabitDuration.SIXTYSIX_DAYS
        HabitDuration.NINETYNINE_DAYS -> SerializableHabitDuration.NINETYNINE_DAYS
    }

private fun SerializableHabitDuration.toDomain() =
    when (this) {
        SerializableHabitDuration.THREE_DAYS -> HabitDuration.THREE_DAYS
        SerializableHabitDuration.SEVEN_DAYS -> HabitDuration.SEVEN_DAYS
        SerializableHabitDuration.FOURTEEN_DAYS -> HabitDuration.FOURTEEN_DAYS
        SerializableHabitDuration.TWENTYONE_DAYS -> HabitDuration.TWENTYONE_DAYS
        SerializableHabitDuration.SIXTYSIX_DAYS -> HabitDuration.SIXTYSIX_DAYS
        SerializableHabitDuration.NINETYNINE_DAYS -> HabitDuration.NINETYNINE_DAYS
    }
