package moe.forpleuvoir.nebula.common.color

import moe.forpleuvoir.nebula.common.util.math.lerp

interface RGBColor : Cloneable {

    companion object {
        fun equals(color1: RGBColor, color2: RGBColor): Boolean =
            when (color1) {
                is ARGBColor if color2 is ARGBColor   -> ARGBColor.equals(color1, color2)
                !is ARGBColor if color2 !is ARGBColor -> color1.rgb == color2.rgb
                else                                  -> false
            }
    }

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

    fun reverse(): RGBColor {
        return Color(
            red = (255 - red).coerceIn(redRange),
            green = (255 - green).coerceIn(greenRange),
            blue = (255 - blue).coerceIn(blueRange)
        )
    }

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
            red = (red + other.red).coerceIn(redRange),
            green = (green + other.green).coerceIn(greenRange),
            blue = (blue + other.blue).coerceIn(blueRange),
            checkRange = false
        )
    }

    operator fun minus(other: RGBColor): RGBColor {
        return Color(
            red = (red - other.red).coerceIn(redRange),
            green = (green - other.green).coerceIn(greenRange),
            blue = (blue - other.blue).coerceIn(blueRange),
            checkRange = false
        )
    }

    operator fun times(other: RGBColor): RGBColor {
        return Color(
            red = (redF * other.redF).coerceIn(redFRange),
            green = (greenF * other.greenF).coerceIn(greenFRange),
            blue = (blueF * other.blueF).coerceIn(blueFRange),
            checkRange = false
        )
    }

    operator fun div(other: RGBColor): RGBColor {
        return Color(
            red = (red / other.red).coerceIn(redRange),
            green = (green / other.green).coerceIn(greenRange),
            blue = (blue / other.blue).coerceIn(blueRange),
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

