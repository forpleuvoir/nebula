package moe.forpleuvoir.nebula.common.color

import moe.forpleuvoir.nebula.common.color.Color.Companion.fixValue

interface ARGBColor : RGBColor {

    val argb: Int

    val alpha: Int

    val alphaF: Float

    operator fun component4(): Int = alpha

    override fun clone(): ARGBColor

    operator fun plus(other: ARGBColor): ARGBColor {
        return Color(
            red = (redF + other.redF).fixValue(false, "Red"),
            green = (greenF + other.greenF).fixValue(false, "Green"),
            blue = (blueF + other.blueF).fixValue(false, "Blue"),
            alpha = (alphaF + other.alphaF).fixValue(false, "Alpha"),
            checkRange = false
        )
    }

    operator fun minus(other: ARGBColor): ARGBColor {
        return Color(
            red = (redF - other.redF).fixValue(false, "Red"),
            green = (greenF - other.greenF).fixValue(false, "Green"),
            blue = (blueF - other.blueF).fixValue(false, "Blue"),
            alpha = (alphaF - other.alphaF).fixValue(false, "Alpha"),
            checkRange = false
        )
    }

    operator fun times(other: ARGBColor): ARGBColor {
        return Color(
            red = (redF * other.redF).fixValue(false, "Red"),
            green = (greenF * other.greenF).fixValue(false, "Green"),
            blue = (blueF * other.blueF).fixValue(false, "Blue"),
            alpha = (alphaF * other.alphaF).fixValue(false, "Alpha"),
            checkRange = false
        )
    }

    operator fun div(other: ARGBColor): ARGBColor {
        return Color(
            red = (redF / other.redF).fixValue(false, "Red"),
            green = (greenF / other.greenF).fixValue(false, "Green"),
            blue = (blueF / other.blueF).fixValue(false, "Blue"),
            alpha = (alphaF / other.alphaF).fixValue(false, "Alpha"),
            checkRange = false
        )
    }

}

val alphaRange: IntRange get() = 0..255
val alphaFRange: ClosedFloatingPointRange<Float> get() = 0f..1f