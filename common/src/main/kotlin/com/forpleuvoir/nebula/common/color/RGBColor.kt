package com.forpleuvoir.nebula.common.color

interface RGBColor {

    val rgb: Int

    val red: Int

    val redF: Float

    val green: Int

    val greenF: Float

    val blue: Int

    val blueF: Float

    val hexStr: String

}

val redRange: IntRange get() = 0..255

val greenRange: IntRange get() = 0..255

val blueRange: IntRange get() = 0..255

val redFRange: ClosedFloatingPointRange<Float> get() = 0f..1f

val greenFRange: ClosedFloatingPointRange<Float> get() = 0f..1f

val blueFRange: ClosedFloatingPointRange<Float> get() = 0f..1f