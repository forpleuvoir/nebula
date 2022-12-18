@file:Suppress("UNUSED")

package com.forpleuvoir.nebula.common.util


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