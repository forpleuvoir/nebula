package moe.forpleuvoir.nebula.common.util

import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class SerializableDuration(
    val value: Double,
    val unit: DurationUnit
) {
    val duration = value.toDuration(unit)
}