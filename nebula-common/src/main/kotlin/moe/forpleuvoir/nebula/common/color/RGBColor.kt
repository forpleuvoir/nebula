package moe.forpleuvoir.nebula.common.color

import moe.forpleuvoir.nebula.common.color.Color.Companion.fixValue
import moe.forpleuvoir.nebula.common.util.math.lerp

interface RGBColor : Cloneable {

    val rgb: Int

    val red: Int

    val redF: Float

    val green: Int

    val greenF: Float

    val blue: Int

    val blueF: Float

    val hexStr: String

    operator fun component1(): Int = red

    operator fun component2(): Int = green

    operator fun component3(): Int = blue

    public override fun clone(): RGBColor

    fun lerp(to: RGBColor, fraction: Float): RGBColor {
        check(fraction in 0.0..1.0) { "fraction must be between 0.0 and 1.0" }
        return Color(
            lerp(redF, to.redF, fraction),
            lerp(greenF, to.greenF, fraction),
            lerp(blueF, to.blueF, fraction),
        )
    }

    operator fun plus(other: RGBColor): RGBColor {
        return Color(
            red = (redF + other.redF).fixValue(false, "Red"),
            green = (greenF + other.greenF).fixValue(false, "Green"),
            blue = (blueF + other.blueF).fixValue(false, "Blue"),
            checkRange = false
        )
    }

    operator fun minus(other: RGBColor): RGBColor {
        return Color(
            red = (redF - other.redF).fixValue(false, "Red"),
            green = (greenF - other.greenF).fixValue(false, "Green"),
            blue = (blueF - other.blueF).fixValue(false, "Blue"),
            checkRange = false
        )
    }

    operator fun times(other: RGBColor): RGBColor {
        return Color(
            red = (redF * other.redF).fixValue(false, "Red"),
            green = (greenF * other.greenF).fixValue(false, "Green"),
            blue = (blueF * other.blueF).fixValue(false, "Blue"),
            checkRange = false
        )
    }

    operator fun div(other: RGBColor): RGBColor {
        return Color(
            red = (redF / other.redF).fixValue(false, "Red"),
            green = (greenF / other.greenF).fixValue(false, "Green"),
            blue = (blueF / other.blueF).fixValue(false, "Blue"),
            checkRange = false
        )
    }

}

val redRange: IntRange get() = 0..255

val greenRange: IntRange get() = 0..255

val blueRange: IntRange get() = 0..255

val redFRange: ClosedFloatingPointRange<Float> get() = 0f..1f

val greenFRange: ClosedFloatingPointRange<Float> get() = 0f..1f

val blueFRange: ClosedFloatingPointRange<Float> get() = 0f..1f