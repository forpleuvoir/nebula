@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate")

package moe.forpleuvoir.nebula.common.color

import moe.forpleuvoir.nebula.common.color.Color.Companion.fixValue
import moe.forpleuvoir.nebula.common.util.clamp
import moe.forpleuvoir.nebula.common.util.fillBefore

class HSVColor(
    hue: Float = 360f,
    saturation: Float = 1f,
    value: Float = 1f,
    alpha: Float = 1f,
    private val checkRange: Boolean = true
) : ARGBColor, Cloneable {

    constructor(argb: Int, checkRange: Boolean = true) : this(checkRange = checkRange) {
        this.argb = argb
    }

    constructor(color: Color) : this(color.argb, false)

    override var argb: Int
        get() {
            return ((alphaF * 255).toInt() shl 24) or java.awt.Color.HSBtoRGB(hue / 360f, saturation, value)
        }
        set(value) {
            val r = value shr 16 and 0xFF
            val g = value shr 8 and 0xFF
            val b = value and 0xFF
            val arr = FloatArray(3)
            java.awt.Color.RGBtoHSB(r, g, b, arr)
            this.hue = (arr[0] * 360).clamp(0f, 360f)
            this.saturation = arr[1].clamp(0f, 1f)
            this.value = arr[2].clamp(0f, 1f)
            this.alphaF = ((value shr 24 and 0xFF) / 255f).clamp(alphaFRange)
        }

    override var rgb: Int
        get() = argb and 0xFFFFFF
        set(value) {
            argb = argb and 0xFF000000.toInt() or value
        }

    override val hexStr: String get() = "#${argb.toUInt().toString(16).fillBefore(8, '0').uppercase()}"

    /**
     * 色相 Range(0.0F ~ 360.0F)
     */
    var hue: Float = hue.fixValue(checkRange, "Hue", maxValue = 360f)
        set(value) {
            field = value.fixValue(checkRange, "Hue", maxValue = 360f)
        }

    fun hue(hue: Float): HSVColor = this.apply { this.hue = hue }

    /**
     * 饱和度 Range(0.0f ~ 1.0f)
     */
    var saturation: Float = saturation.fixValue(checkRange, "Saturation", maxValue = 1f)
        set(value) {
            field = value.fixValue(checkRange, "Saturation", maxValue = 1f)
        }

    fun saturation(saturation: Float): HSVColor = this.apply { this.saturation = saturation }

    /**
     * 明度 Range(0.0f ~ 1.0f)
     */
    var value: Float = value.fixValue(checkRange, "Value", maxValue = 1f)
        set(value) {
            field = value.fixValue(checkRange, "Value", maxValue = 1f)
        }

    fun value(value: Float): HSVColor = this.apply { this.value = value }

    /**
     * 不透明度 Range(0.0F ~ 1.0F)
     */

    override var alpha: Int
        get() = argb shr 24 and 0xFF
        set(value) {
            argb = (value.fixValue(
                checkRange,
                "Alpha"
            ) and 0xFF shl 24) or (red and 0xFF shl 16) or (green and 0xFF shl 8) or (blue and 0xFF)
        }

    override var alphaF: Float = alpha.fixValue(checkRange, "Alpha")
        set(value) {
            field = value.fixValue(checkRange, "Alpha")
        }

    override val red: Int
        get() = argb shr 16 and 0xFF
    override val redF: Float
        get() = red.toFloat() / 255
    override val green: Int
        get() = argb shr 8 and 0xFF
    override val greenF: Float
        get() = green.toFloat() / 255
    override val blue: Int
        get() = argb shr 0 and 0xFF
    override val blueF: Float
        get() = blue.toFloat() / 255

    fun alpha(alpha: Float): HSVColor = this.apply { this.alphaF = alpha }

    /**
     * 获取调整不透明度之后的颜色复制对象
     *
     * 不透明度 = 当前不透明度 * opacity
     *
     * @param opacity [Float] Range(0.0F ~ 1.0F)
     * @return [HSVColor] 复制对象
     */
    fun opacity(opacity: Float): HSVColor = this.clone().apply { alphaF *= opacity.fixValue(checkRange, "Opacity") }

    override operator fun plus(other: ARGBColor): HSVColor {
        return HSVColor(super.plus(other).argb)
    }

    operator fun plusAssign(other: ARGBColor) {
        this.argb = Color(
            red = (redF + other.redF).fixValue(false, "RED"),
            green = (greenF + other.greenF).fixValue(false, "GREEN"),
            blue = (blueF + other.blueF).fixValue(false, "BLUE"),
            alpha = (alphaF + other.alphaF).fixValue(false, "ALPHA"),
            checkRange = false
        ).argb
    }

    override operator fun minus(other: ARGBColor): HSVColor {
        return HSVColor(super.minus(other).argb)
    }

    operator fun minusAssign(other: ARGBColor) {
        this.argb = Color(
            red = (redF - other.redF).fixValue(false, "RED"),
            green = (greenF - other.greenF).fixValue(false, "GREEN"),
            blue = (blueF - other.blueF).fixValue(false, "BLUE"),
            alpha = (alphaF - other.alphaF).fixValue(false, "ALPHA"),
            checkRange = false
        ).argb
    }

    override operator fun times(other: ARGBColor): HSVColor {
        return HSVColor(super.times(other).argb)
    }

    operator fun timesAssign(other: ARGBColor) {
        this.argb = Color(
            red = (redF * other.redF).fixValue(false, "RED"),
            green = (greenF * other.greenF).fixValue(false, "GREEN"),
            blue = (blueF * other.blueF).fixValue(false, "BLUE"),
            alpha = (alphaF * other.alphaF).fixValue(false, "ALPHA"),
            false
        ).argb
    }

    override operator fun div(other: ARGBColor): HSVColor {
        return HSVColor(super.div(other).argb)
    }

    operator fun divAssign(other: ARGBColor) {
        this.argb = Color(
            red = (redF / other.redF).fixValue(false, "RED"),
            green = (greenF / other.greenF).fixValue(false, "GREEN"),
            blue = (blueF / other.blueF).fixValue(false, "BLUE"),
            alpha = (alphaF / other.alphaF).fixValue(false, "ALPHA"),
            false,
        ).argb
    }

    public override fun clone(): HSVColor = HSVColor(hue, saturation, value, alphaF)

    override fun toString(): String {
        return "HSBColor(argb=$argb, hexStr='$hexStr', hue=$hue, saturation=$saturation, value=$value, alpha=$alphaF)"
    }


}

