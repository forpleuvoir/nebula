package moe.forpleuvoir.nebula.common.color

import moe.forpleuvoir.nebula.common.color.Color.Companion.fixValue

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