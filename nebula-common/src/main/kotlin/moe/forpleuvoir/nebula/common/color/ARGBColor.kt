package moe.forpleuvoir.nebula.common.color

import moe.forpleuvoir.nebula.common.color.Color.Companion.fixValue
import moe.forpleuvoir.nebula.common.util.math.lerp

interface ARGBColor : RGBColor {

    val argb: Int

    val alpha: Int

    val alphaF: Float

    operator fun component4(): Int = alpha

    override fun clone(): ARGBColor

    override fun reverse(): ARGBColor {
        return Color(
            red = (255 - red).coerceIn(redRange),
            green = (255 - green).coerceIn(greenRange),
            blue = (255 - blue).coerceIn(blueRange),
            alpha = alpha
        )
    }

    fun lerp(to: ARGBColor, fraction: Float): ARGBColor {
        check(fraction in 0.0..1.0) { "fraction must be between 0.0 and 1.0" }
        return Color(
            lerp(redF, to.redF, fraction),
            lerp(greenF, to.greenF, fraction),
            lerp(blueF, to.blueF, fraction),
            lerp(alphaF, to.alphaF, fraction)
        )
    }

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