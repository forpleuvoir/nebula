@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate")

package com.forpleuvoir.nebula.common.color

import com.forpleuvoir.nebula.common.color.Color.Companion.fixValue
import com.forpleuvoir.nebula.common.util.clamp
import com.forpleuvoir.nebula.common.util.fillBefore
import kotlin.math.floor

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
            var r = 0
            var g = 0
            var b = 0
            if (saturation == 0f) {
                b = (value * 255.0f + 0.5f).toInt()
                g = b
                r = g
            } else {
                val h = (hue - floor(hue.toDouble()).toFloat()) * 6.0f
                val f = h - floor(h.toDouble()).toFloat()
                val p: Float = value * (1.0f - saturation)
                val q: Float = value * (1.0f - saturation * f)
                val t: Float = value * (1.0f - saturation * (1.0f - f))
                when (h.toInt()) {
                    0 -> {
                        r = (value * 255.0f + 0.5f).toInt()
                        g = (t * 255.0f + 0.5f).toInt()
                        b = (p * 255.0f + 0.5f).toInt()
                    }

                    1 -> {
                        r = (q * 255.0f + 0.5f).toInt()
                        g = (value * 255.0f + 0.5f).toInt()
                        b = (p * 255.0f + 0.5f).toInt()
                    }

                    2 -> {
                        r = (p * 255.0f + 0.5f).toInt()
                        g = (value * 255.0f + 0.5f).toInt()
                        b = (t * 255.0f + 0.5f).toInt()
                    }

                    3 -> {
                        r = (p * 255.0f + 0.5f).toInt()
                        g = (q * 255.0f + 0.5f).toInt()
                        b = (value * 255.0f + 0.5f).toInt()
                    }

                    4 -> {
                        r = (t * 255.0f + 0.5f).toInt()
                        g = (p * 255.0f + 0.5f).toInt()
                        b = (value * 255.0f + 0.5f).toInt()
                    }

                    5 -> {
                        r = (value * 255.0f + 0.5f).toInt()
                        g = (p * 255.0f + 0.5f).toInt()
                        b = (q * 255.0f + 0.5f).toInt()
                    }
                }
            }
            return ((alphaF * 255).toInt() shl 24) or (r shl 16) or (g shl 8) or (b shl 0)
        }
        set(value) {
            val r = value shr 16 and 0xFF
            val g = value shr 8 and 0xFF
            val b = value and 0xFF

            var hue: Float
            val saturation: Float
            val brightness: Float
            var cmax: Int = if (r > g) r else g
            if (b > cmax) cmax = b
            var cmin: Int = if (r < g) r else g
            if (b < cmin) cmin = b

            brightness = cmax.toFloat() / 255.0f
            saturation = if (cmax != 0) (cmax - cmin).toFloat() / cmax.toFloat() else 0f
            if (saturation == 0f) hue = 0f else {
                val redc: Float = (cmax - r).toFloat() / (cmax - cmin).toFloat()
                val greenc: Float = (cmax - g).toFloat() / (cmax - cmin).toFloat()
                val bluec: Float = (cmax - b).toFloat() / (cmax - cmin).toFloat()
                hue = if (r == cmax) bluec - greenc else if (g == cmax) 2.0f + redc - bluec else 4.0f + greenc - redc
                hue /= 6.0f
                if (hue < 0) hue += 1.0f
            }

            this.hue = hue.clamp(0f, 360f)
            this.saturation = saturation.clamp(0f, 1f)
            this.value = brightness.clamp(0f, 1f)
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

    public override fun clone(): HSVColor = HSVColor(hue, saturation, value, alphaF)

    override fun toString(): String {
        return "HSBColor(argb=$argb, hexStr='$hexStr', hue=$hue, saturation=$saturation, value=$value, alpha=$alphaF)"
    }


}

