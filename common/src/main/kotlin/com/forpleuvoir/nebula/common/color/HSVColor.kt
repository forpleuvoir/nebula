@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate")

package com.forpleuvoir.nebula.common.color

import com.forpleuvoir.nebula.common.color.Color.Companion.fixValue
import com.forpleuvoir.nebula.common.util.clamp
import com.forpleuvoir.nebula.common.util.fillBefore
import kotlin.math.abs
import kotlin.math.roundToInt

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
            val h: Float = hue
            val s: Float = saturation
            val l: Float = value

            val c = (1f - abs(2 * l - 1f)) * s
            val m = l - 0.5f * c
            val x = c * (1f - abs(h / 60f % 2f - 1f))

            val hueSegment = h.toInt() / 60

            var r = 0
            var g = 0
            var b = 0

            when (hueSegment) {
                0    -> {
                    r = (255 * (c + m)).roundToInt()
                    g = (255 * (x + m)).roundToInt()
                    b = (255 * m).roundToInt()
                }

                1    -> {
                    r = (255 * (x + m)).roundToInt()
                    g = (255 * (c + m)).roundToInt()
                    b = (255 * m).roundToInt()
                }

                2    -> {
                    r = (255 * m).roundToInt()
                    g = (255 * (c + m)).roundToInt()
                    b = (255 * (x + m)).roundToInt()
                }

                3    -> {
                    r = (255 * m).roundToInt()
                    g = (255 * (x + m)).roundToInt()
                    b = (255 * (c + m)).roundToInt()
                }

                4    -> {
                    r = (255 * (x + m)).roundToInt()
                    g = (255 * m).roundToInt()
                    b = (255 * (c + m)).roundToInt()
                }

                5, 6 -> {
                    r = (255 * (c + m)).roundToInt()
                    g = (255 * m).roundToInt()
                    b = (255 * (x + m)).roundToInt()
                }
            }
            r = r.clamp(0, 255)
            g = g.clamp(0, 255)
            b = b.clamp(0, 255)
            return ((alphaF * 255).toInt() shl 24) or (r shl 16) or (g shl 8) or (b shl 0)
        }
        set(value) {
            val r = value shr 16 and 0xFF
            val g = value shr 8 and 0xFF
            val b = value and 0xFF

            val rf: Float = r / 255f
            val gf: Float = g / 255f
            val bf: Float = b / 255f

            val max = rf.coerceAtLeast(gf.coerceAtLeast(bf))
            val min = rf.coerceAtMost(gf.coerceAtMost(bf))
            val deltaMaxMin = max - min

            var h: Float
            val s: Float
            val l = (max + min) / 2f

            if (max == min) {
                // Monochromatic
                s = 0f
                h = s
            } else {
                h = when (max) {
                    rf   -> {
                        (gf - bf) / deltaMaxMin % 6f
                    }

                    gf   -> {
                        (bf - rf) / deltaMaxMin + 2f
                    }

                    else -> {
                        (rf - gf) / deltaMaxMin + 4f
                    }
                }
                s = deltaMaxMin / (1f - abs(2f * l - 1f))
            }

            h = h * 60f % 360f
            if (h < 0) {
                h += 360f
            }

            hue = h.clamp(0f, 360f)
            saturation = s.clamp(0f, 1f)
            this.value = l.clamp(0f, 1f)
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

