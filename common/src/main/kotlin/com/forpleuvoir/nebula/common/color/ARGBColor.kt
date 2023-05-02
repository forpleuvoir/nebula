package com.forpleuvoir.nebula.common.color

interface ARGBColor : RGBColor {

    val argb: Int

    val alpha: Int

    val alphaF: Float

}

val alphaRange: IntRange get() = 0..255
val alphaFRange: ClosedFloatingPointRange<Float> get() = 0f..1f