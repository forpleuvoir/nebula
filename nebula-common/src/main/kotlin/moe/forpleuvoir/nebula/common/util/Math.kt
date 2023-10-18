@file:Suppress("UNUSED")

package moe.forpleuvoir.nebula.common.util


fun <T> T.clamps(minValue: T, maxValue: T): T where T : Number, T : Comparable<T> {
    return if (this > maxValue) maxValue
    else if (this < minValue) minValue
    else this
}

fun <T> T.clamp(range: ClosedRange<T>): T where T : Number, T : Comparable<T> {
    return if (this > range.endInclusive) range.endInclusive
    else if (this < range.start) range.start
    else this
}

fun Int.clamp(minValue: Number, maxValue: Number): Int {
    return if (this > maxValue.toDouble()) maxValue.toInt()
    else if (this < minValue.toDouble()) minValue.toInt()
    else this
}

fun Short.clamp(minValue: Number, maxValue: Number): Short {
    return if (this > maxValue.toDouble()) maxValue.toShort()
    else if (this < minValue.toDouble()) minValue.toShort()
    else this
}

fun Long.clamp(minValue: Number, maxValue: Number): Long {
    return if (this > maxValue.toDouble()) maxValue.toLong()
    else if (this < minValue.toDouble()) minValue.toLong()
    else this
}

fun Double.clamp(minValue: Number, maxValue: Number): Double {
    return if (this > maxValue.toDouble()) maxValue.toDouble()
    else if (this < minValue.toDouble()) minValue.toDouble()
    else this
}

fun Float.clamp(minValue: Number, maxValue: Number): Float {
    return if (this > maxValue.toDouble()) maxValue.toFloat()
    else if (this < minValue.toDouble()) minValue.toFloat()
    else this
}

fun Byte.clamp(minValue: Number, maxValue: Number): Byte {
    return if (this > maxValue.toDouble()) maxValue.toByte()
    else if (this < minValue.toDouble()) minValue.toByte()
    else this
}
