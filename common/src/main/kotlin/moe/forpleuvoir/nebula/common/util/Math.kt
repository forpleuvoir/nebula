@file:Suppress("UNUSED")

package moe.forpleuvoir.nebula.common.util


fun Int.clamp(minValue: Number, maxValue: Number): Int {
    return if (this > maxValue.toDouble()) maxValue.toInt()
    else if (this < minValue.toDouble()) minValue.toInt()
    else this
}

fun Int.clamp(range: ClosedRange<Int>): Int {
    return if (this > range.endInclusive) range.endInclusive
    else if (this < range.start) range.start
    else this
}

fun Short.clamp(minValue: Number, maxValue: Number): Short {
    return if (this > maxValue.toDouble()) maxValue.toShort()
    else if (this < minValue.toDouble()) minValue.toShort()
    else this
}

fun Short.clamp(range: ClosedRange<Short>): Short {
    return if (this > range.endInclusive) range.endInclusive
    else if (this < range.start) range.start
    else this
}

fun Long.clamp(minValue: Number, maxValue: Number): Long {
    return if (this > maxValue.toDouble()) maxValue.toLong()
    else if (this < minValue.toDouble()) minValue.toLong()
    else this
}

fun Long.clamp(range: ClosedRange<Long>): Long {
    return if (this > range.endInclusive) range.endInclusive
    else if (this < range.start) range.start
    else this
}


fun Double.clamp(minValue: Number, maxValue: Number): Double {
    0.0..50.0
    return if (this > maxValue.toDouble()) maxValue.toDouble()
    else if (this < minValue.toDouble()) minValue.toDouble()
    else this
}

fun Double.clamp(range: ClosedRange<Double>): Double {
    return if (this > range.endInclusive) range.endInclusive
    else if (this < range.start) range.start
    else this
}

fun Float.clamp(minValue: Number, maxValue: Number): Float {
    return if (this > maxValue.toDouble()) maxValue.toFloat()
    else if (this < minValue.toDouble()) minValue.toFloat()
    else this
}

fun Float.clamp(range: ClosedRange<Float>): Float {
    return if (this > range.endInclusive) range.endInclusive
    else if (this < range.start) range.start
    else this
}