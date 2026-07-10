package moe.forpleuvoir.nebula.common.color

import moe.forpleuvoir.nebula.common.util.math.lerp

@Suppress("NOTHING_TO_INLINE", "unused")
@JvmInline
value class Color(val argb: Int) {

    companion object {

        @JvmStatic
        fun isValidColor(hex: String): Boolean = runCatching {
            hex2Int(hex)
            true
        }.getOrDefault(false)

        @JvmStatic
        fun hex2Int(hex: String): Int {
            val str = hex.removePrefix("0x").removePrefix("0X").removePrefix("#")
            return when (str.length) {
                8 -> str.toUInt(16).toInt()
                6 -> (0xFF000000u or str.toUInt(16)).toInt()
                else -> throw IllegalArgumentException("Invalid hex color string: $hex")
            }
        }

        @JvmStatic
        fun normalize(value: Int): Int = value.coerceIn(0, 255)

        @JvmStatic
        fun normalize(value: Float): Int = (value.coerceIn(0f, 1f) * 255).toInt()

        @JvmStatic
        fun normalizeHSV(value: Float): Float = value.coerceIn(0f, 1f)

        @JvmStatic
        fun fromARGB(red: Int, green: Int, blue: Int, alpha: Int = 255): Color =
            Color((alpha shl 24) or (red shl 16) or (green shl 8) or blue)

        operator fun of(vararg values: Int): Color =
            fromARGB(
                values.getOrElse(0) { 255 },
                values.getOrElse(1) { 255 },
                values.getOrElse(2) { 255 },
                values.getOrElse(3) { 255 }
            )

        @JvmStatic
        fun fromARGB(red: Float, green: Float, blue: Float, alpha: Float = 1f): Color {
            val r = normalize(red)
            val g = normalize(green)
            val b = normalize(blue)
            val a = normalize(alpha)
            return Color((a shl 24) or (r shl 16) or (g shl 8) or b)
        }

        @JvmStatic
        inline fun fromARGB(argb: Int): Color = Color(argb)

        @JvmStatic
        inline fun fromARGB(argb: Long): Color = Color(argb.toInt())

        @JvmStatic
        inline fun fromARGB(argb: UInt): Color = Color(argb.toInt())

        @JvmStatic
        inline fun fromRGB(rgb: Int): Color =
            Color((0xFF000000u or (rgb.toUInt() and 0x00FFFFFFu)).toInt())


        /**
         * 从HSV中获取颜色
         * @param hue 0-1 色相
         * @param saturation 0-1 饱和度
         * @param value 0-1 亮度
         * @param alpha float 0-1 ,int 0-255 透明度
         */
        @JvmStatic
        fun fromHSV(hue: Float, saturation: Float, value: Float, alpha: Int): Color {
            val h = normalizeHSV(hue)
            val s = normalizeHSV(saturation)
            val v = normalizeHSV(value)
            return fromRGB(HSVHelper.getOrPut(h, s, v)).alpha(alpha)
        }

        @JvmStatic
        fun fromHSV(hue: Float, saturation: Float, value: Float, alpha: Float = 1f): Color {
            val h = normalizeHSV(hue)
            val s = normalizeHSV(saturation)
            val v = normalizeHSV(value)
            return fromRGB(HSVHelper.getOrPut(h, s, v)).alpha(alpha)
        }

        @JvmStatic
        fun fromHexString(hex: String): Color = Color(hex2Int(hex))

    }

    val hexStr: String
        get() = if (alpha == 255) "#%06X".format(argb and 0xFFFFFF)
        else "#%08X".format(argb)

    override fun toString(): String =
        "Color(hex: $hexStr, alpha: $alpha, rgb: [$red, $green, $blue], hsv: [$hue, $saturation, $value])"

    //region RGB
    inline val rgb: Int get() = argb and 0x00FFFFFF

    inline fun rgb(rgb: Int): Color = Color(((argb.toUInt() and 0xFF000000u) or (rgb.toUInt() and 0x00FFFFFFu)).toInt())

    inline val red: Int get() = argb shr 16 and 0xFF

    inline val redF: Float get() = red / 255f

    inline fun red(red: Int): Color = Color(
        ((normalize(red) shl 16) or (argb.toUInt() and 0xFF00FFFFu).toInt())
    )

    inline fun red(red: Float): Color = Color(
        ((normalize(red) shl 16) or (argb.toUInt() and 0xFF00FFFFu).toInt())
    )

    inline val green: Int get() = argb shr 8 and 0xFF

    inline val greenF: Float get() = green / 255f

    inline fun green(green: Int): Color = Color(
        ((normalize(green) shl 8) or (argb.toUInt() and 0xFFFF00FFu).toInt())
    )

    inline fun green(green: Float): Color = Color(
        ((normalize(green) shl 8) or (argb.toUInt() and 0xFFFF00FFu).toInt())
    )

    inline val blue: Int get() = argb and 0xFF

    inline val blueF: Float get() = blue / 255f

    inline fun blue(blue: Int): Color = Color(
        ((normalize(blue) and 0xFF) or (argb.toUInt() and 0xFFFFFF00u).toInt())
    )

    inline fun blue(blue: Float): Color = Color(
        ((normalize(blue) and 0xFF) or (argb.toUInt() and 0xFFFFFF00u).toInt())
    )

    inline val alpha: Int get() = argb shr 24 and 0xFF

    inline val alphaF: Float get() = alpha / 255f

    inline fun alpha(alpha: Int): Color = Color(
        ((normalize(alpha) shl 24) or (argb.toUInt() and 0x00FFFFFFu).toInt())
    )

    inline fun alpha(alpha: Float): Color = Color(
        ((normalize(alpha) shl 24) or (argb.toUInt() and 0x00FFFFFFu).toInt())
    )

    inline fun opacity(opacity: Int): Color = alpha((alpha * normalize(opacity) / 255f).toInt())

    inline fun opacity(opacity: Float): Color = alpha((alpha * opacity.coerceIn(0f, 1f)).toInt())
    //endregion


    //region HSV
    /**
     * 色相 范围 0-1
     */
    val hue: Float get() = HSVHelper.getHue(this)

    /**
     * 设置色相
     * @param hue 范围 0-1
     */
    fun hue(hue: Float): Color =
        HSVHelper.setHue(this, normalizeHSV(hue))

    /**
     * 饱和度 0-1
     */
    val saturation: Float get() = HSVHelper.getSaturation(this)

    /**
     * 设置饱和度
     * @param saturation 0-1
     */
    fun saturation(saturation: Float): Color =
        HSVHelper.setSaturation(this, normalizeHSV(saturation))

    /**
     * 亮度 0-1
     */
    val value: Float get() = HSVHelper.getValue(this)

    /**
     * 设置亮度
     * @param value 0-1
     */
    fun value(value: Float): Color =
        HSVHelper.setValue(this, normalizeHSV(value))
    //endregion

    fun lerp(to: Color, fraction: Float, alpha: Boolean = false): Color {
        val r = this.red.lerp(to.red, fraction)
        val g = this.green.lerp(to.green, fraction)
        val b = this.blue.lerp(to.blue, fraction)
        val a = if (alpha) this.alpha.lerp(to.alpha, fraction) else this.alpha
        return fromARGB(r, g, b, a)
    }

    fun hsvLerp(to: Color, fraction: Float, alpha: Boolean = false): Color {
        val self = HSVHelper.getOrPut(this.rgb)
        val toHSV = HSVHelper.getOrPut(to.rgb)
        val h = self.hue.lerp(toHSV.hue, fraction)
        val s = self.saturation.lerp(toHSV.saturation, fraction)
        val v = self.value.lerp(toHSV.value, fraction)
        val a = if (alpha) this.alpha.lerp(to.alpha, fraction) else this.alpha
        return fromHSV(h, s, v, a)
    }

    fun reverse(alpha: Boolean = false): Color {
        val r = 255 - red
        val g = 255 - green
        val b = 255 - blue
        val a = if (alpha) 255 - this.alpha else this.alpha
        return fromARGB(r, g, b, a)
    }

    operator fun plus(other: Color): Color {
        val red = (this.red + other.red).coerceIn(0, 255)
        val green = (this.green + other.green).coerceIn(0, 255)
        val blue = (this.blue + other.blue).coerceIn(0, 255)
        val alpha = (this.alpha + other.alpha).coerceIn(0, 255)
        return fromARGB(red, green, blue, alpha)
    }

    operator fun minus(other: Color): Color {
        val red = (this.red - other.red).coerceIn(0, 255)
        val green = (this.green - other.green).coerceIn(0, 255)
        val blue = (this.blue - other.blue).coerceIn(0, 255)
        val alpha = (this.alpha - other.alpha).coerceIn(0, 255)
        return fromARGB(red, green, blue, alpha)
    }

    operator fun times(other: Color): Color {
        val red = this.redF * other.redF
        val green = this.greenF * other.greenF
        val blue = this.blueF * other.blueF
        val alpha = this.alphaF * other.alphaF
        return fromARGB(red, green, blue, alpha)
    }

    operator fun div(other: Color): Color {
        val red = (this.redF / other.redF).coerceIn(0f, 1f)
        val green = (this.greenF / other.greenF).coerceIn(0f, 1f)
        val blue = (this.blueF / other.blueF).coerceIn(0f, 1f)
        val alpha = (this.alphaF / other.alphaF).coerceIn(0f, 1f)
        return fromARGB(red, green, blue, alpha)
    }

}