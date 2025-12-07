@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate")

package moe.forpleuvoir.nebula.common.color

import moe.forpleuvoir.nebula.common.util.primitive.fillBefore

class Color : ARGBColor {

    companion object {

        /**
         * 解码字符串颜色 AARRGGBB 十六进制字符串
         *
         * 格式: 0xFFFFFFFF,0xFFFFFFFF,#FFFFFFFF
         *
         * @param color String
         * @return Int
         */
        @JvmStatic
        fun decode(color: String): Int {
            val hex: String = color.replace(Regex("0x|0X"), "").replace("#", "")
            return when (hex.length) {
                8    -> hex.toUInt(16).toInt()
                6    -> 0xFF000000.toInt() or hex.toUInt(16).toInt()
                else -> throw IllegalArgumentException("Unable to parse color information from [${color}]")
            }
        }

        @JvmStatic
        fun isValidColor(color: Int): Boolean {
            // 检查颜色值是否在0到0xFFFFFFFF之间
            if (color.toUInt() > 0xFFFFFFFFu) {
                return false
            }

            // 使用位运算符检查颜色值的alpha、红、绿、蓝四个分量是否在0-FF范围内
            val alpha = (color shr 24) and 0xFF
            val red = (color shr 16) and 0xFF
            val green = (color shr 8) and 0xFF
            val blue = color and 0xFF
            return !(alpha in alphaRange || red in redRange || green in greenRange || blue in blueRange)
        }

        internal fun Int.fixValue(
            checkRange: Boolean,
            parameterName: String,
            minValue: Int = 0,
            maxValue: Int = 255
        ): Int {
            if ((this !in minValue..maxValue) && checkRange) {
                throw IllegalArgumentException("[$parameterName : $this]Color parameter outside of expected range[$minValue ~ $maxValue]")
            }
            return this.coerceIn(minValue, maxValue)
        }

        internal fun Float.fixValue(
            checkRange: Boolean,
            parameterName: String,
            minValue: Float = 0.0F,
            maxValue: Float = 1.0F
        ): Float {
            if ((this !in minValue..maxValue) && checkRange) {
                throw IllegalArgumentException("[$parameterName : $this]Color parameter outside of expected range[$minValue ~ $maxValue]")
            }
            return this.coerceIn(minValue, maxValue)
        }

        @JvmStatic
        fun ofRGB(rgb: Int): Color = Color(argb = rgb, checkRange = false, fixed = false).alpha(255)

        @JvmStatic
        fun ofARGB(argb: Int): Color = Color(argb = argb, checkRange = false, fixed = false)

        @JvmStatic
        fun ofARGB(argb: Long): Color = ofARGB(argb.toInt())

        @JvmStatic
        fun ofHSV(hsvColor: HSVColor): Color = ofARGB(hsvColor.argb)

        @JvmStatic
        fun ofString(color: String): Color = ofARGB(decode(color))

    }

    /**
     * 检查值得范围是否合法
     */
    private var checkRange: Boolean

    /**
     * @param argb [Int] 包含ARGB信息的颜色值
     *
     * @param checkRange [Boolean]
     *
     * &#09;是否严格限制各种值是否合法
     *
     * &#09;如果为 true 则出现非法值会直接抛出异常[IllegalArgumentException]
     *
     * &#09;为 false 则只会将值修复到合法范围内
     *
     * @param fixed [Boolean] 是否为已经修复过的值
     * @constructor
     */
    private constructor(argb: Int, checkRange: Boolean, fixed: Boolean) {
        this.argb = argb
        this.checkRange = checkRange
        if (!fixed) {
            red = red.fixValue(checkRange, "Red")
            green = green.fixValue(checkRange, "Green")
            blue = blue.fixValue(checkRange, "Blue")
            alpha = alpha.fixValue(checkRange, "Alpha")
        }
    }

    /**
     *
     * @param red Int 红色值 Range(0 ~ 255)
     * @param green Int 绿色值 Range(0 ~ 255)
     * @param blue Int 蓝色值 Range(0 ~ 255)
     * @param alpha Int 透明的 Range(0 ~ 255)
     * @param checkRange [Boolean]
     *
     * &#09;是否严格限制各种值是否合法
     *
     * &#09;如果为 true 则出现非法值会直接抛出异常[IllegalArgumentException]
     *
     * &#09;为 false 则只会将值修复到合法范围内
     *
     * @constructor
     */
    constructor(
        red: Int = 255,
        green: Int = 255,
        blue: Int = 255,
        alpha: Int = 255,
        checkRange: Boolean = true
    ) : this(
        ((alpha.fixValue(checkRange, "Alpha") and 0xFF) shl 24) or
                ((red.fixValue(checkRange, "Red") and 0xFF) shl 16) or
                ((green.fixValue(checkRange, "Green") and 0xFF) shl 8) or
                ((blue.fixValue(checkRange, "Blue") and 0xFF)),
        checkRange,
        true
    )

    /**
     *
     * @param red Int 红色值 Range(0.0F ~ 1.0F)
     * @param green Int 绿色值 Range(0.0F ~ 1.0F)
     * @param blue Int 蓝色值 Range(0.0F ~ 1.0F)
     * @param alpha Int 透明的 Range(0.0F ~ 1.0F)
     * @param checkRange [Boolean]
     *
     * &#09;是否严格限制各种值是否合法
     *
     * &#09;如果为 true 则出现非法值会直接抛出异常[IllegalArgumentException]
     *
     * &#09;为 false 则只会将值修复到合法范围内
     *
     * @constructor
     */
    constructor(
        red: Float = 1.0F,
        green: Float = 1.0F,
        blue: Float = 1.0F,
        alpha: Float = 1.0F,
        checkRange: Boolean = false
    ) : this(
        (red.fixValue(checkRange, "Red") * 255).toInt(),
        (green.fixValue(checkRange, "Green") * 255).toInt(),
        (blue.fixValue(checkRange, "Blue") * 255).toInt(),
        (alpha.fixValue(checkRange, "Alpha") * 255).toInt(),
        checkRange
    )

    /**
     * 获取颜色的ARGB信息
     *
     * Blue 0-7 bit
     *
     * Green 8-15 bit
     *
     * Red 16-23 bit
     *
     * Alpha 24-31 bit
     */
    override var argb: Int

    override var rgb: Int
        get() = argb and 0xFFFFFF
        set(value) {
            argb = argb and 0xFF000000.toInt() or value
        }

    override val hexStr: String get() = "#${argb.toUInt().toString(16).fillBefore(8, '0').uppercase()}"

    /**
     * 红色值 Range(0 ~ 255)
     */
    override var red: Int
        get() = argb shr 16 and 0xFF
        set(value) {
            argb = (alpha and 0xFF shl 24) or (value.fixValue(
                checkRange,
                "Red"
            ) and 0xFF shl 16) or (green and 0xFF shl 8) or (blue and 0xFF)
        }

    /**
     * 红色值 Range(0.0F ~ 1.0F)
     */
    override var redF: Float
        get() = red.toFloat() / 255
        set(value) {
            red = (value.fixValue(checkRange, "Red") * 255).toInt()
        }


    /**
     * 获取修改[red]之后的对象
     * @param red [Int] Range(0 ~ 255)
     * @return [Color] 原始对象
     */
    fun red(red: Int): Color = this.apply { this.red = red }

    /**
     * 获取修改[redF]之后的对象
     * @param red [Float] Range(0.0F ~ 1.0F)
     * @return [Color] 原始对象
     */
    fun red(red: Float): Color = this.apply { this.redF = red }

    /**
     * 绿色值 Range(0 ~ 255)
     */
    override var green: Int
        get() = argb shr 8 and 0xFF
        set(value) {
            argb = (alpha and 0xFF shl 24) or (red and 0xFF shl 16) or (value.fixValue(
                checkRange,
                "Green"
            ) and 0xFF shl 8) or (blue and 0xFF)
        }

    /**
     * 绿色值 Range(0.0F ~ 1.0F)
     */
    override var greenF: Float
        get() = green.toFloat() / 255
        set(value) {
            green = (value.fixValue(checkRange, "Green") * 255).toInt()
        }

    /**
     * 获取修改[green]之后的对象
     * @param green [Int] Range(0 ~ 255)
     * @return [Color] 原始对象
     */
    fun green(green: Int): Color = this.apply { this.green = green }

    /**
     * 获取修改[greenF]之后的对象
     * @param green [Float] Range(0.0F ~ 1.0F)
     * @return [Color] 原始对象
     */
    fun green(green: Float): Color = this.apply { this.greenF = green }

    /**
     * 蓝色值 Range(0 ~ 255)
     */
    override var blue: Int
        get() = argb shr 0 and 0xFF
        set(value) {
            argb = (alpha and 0xFF shl 24) or (red and 0xFF shl 16) or (green and 0xFF shl 8) or (value.fixValue(
                checkRange,
                "Blue"
            ) and 0xFF)
        }

    /**
     * 蓝色值 Range(0.0F ~ 1.0F)
     */
    override var blueF: Float
        get() = blue.toFloat() / 255
        set(value) {
            blue = (value.fixValue(checkRange, "Blue") * 255).toInt()
        }

    /**
     * 获取修改[blue]之后的对象
     * @param blue [Int] Range(0 ~ 255)
     * @return [Color] 原始对象
     */
    fun blue(blue: Int): Color = this.apply { this.blue = blue }

    /**
     * 获取修改[blueF]之后的对象
     * @param blue [Float] Range(0.0F ~ 1.0F)
     * @return [Color] 原始对象
     */
    fun blue(blue: Float): Color = this.apply { this.blueF = blue }

    /**
     * 不透明度 Range(0 ~ 255)
     */
    override var alpha: Int
        get() = argb shr 24 and 0xFF
        set(value) {
            argb = (value.fixValue(
                checkRange,
                "Alpha"
            ) and 0xFF shl 24) or (red and 0xFF shl 16) or (green and 0xFF shl 8) or (blue and 0xFF)
        }

    /**
     * 不透明度 Range(0.0F ~ 1.0F)
     */
    override var alphaF: Float
        get() = alpha.toFloat() / 255
        set(value) {
            alpha = (value.fixValue(checkRange, "Alpha") * 255).toInt()
        }

    /**
     * 获取修改[alpha]之后的对象
     * @param alpha [Int] Range(0 ~ 255)
     * @return [Color] 原始对象
     */
    fun alpha(alpha: Int): Color = this.apply { this.alpha = alpha }

    /**
     * 获取修改[alpha]之后的对象
     * @param alpha [Float] Range(0.0F ~ 1.0F)
     * @return [Color] 原始对象
     */
    fun alpha(alpha: Float): Color = this.apply { this.alphaF = alpha }

    /**
     * 获取当前颜色的副本
     * @return [Color] 复制对象
     */
    override fun clone(): Color {
        return ofARGB(argb)
    }

    /**
     * 获取调整不透明度之后的颜色复制对象
     *
     * 不透明度 = 当前不透明度 * opacity
     *
     * @param opacity [Float] Range(0.0F ~ 1.0F)
     * @return [Color] 复制对象
     */
    fun opacity(opacity: Float): Color = this.clone().apply { alphaF *= opacity.fixValue(checkRange, "Opacity") }

    /**
     * 获取调整不透明度之后的颜色复制对象
     *
     * 不透明度 = 当前不透明度 * opacity
     *
     * @param opacity [Float] Range(0 ~ 255)
     * @return [Color] 复制对象
     */
    fun opacity(opacity: Int): Color = this.clone().apply { alpha = (alpha * (opacity.fixValue(checkRange, "Opacity") / 255f)).toInt() }

    override operator fun plus(other: ARGBColor): Color {
        return super.plus(other) as Color
    }

    operator fun plusAssign(other: ARGBColor) {
        red = (red + other.red).coerceIn(redRange)
        green = (green + other.green).coerceIn(greenRange)
        blue = (blue + other.blue).coerceIn(blueRange)
        alpha = (alpha + other.alpha).coerceIn(alphaRange)
    }

    override operator fun minus(other: ARGBColor): Color {
        return super.minus(other) as Color
    }

    operator fun minusAssign(other: ARGBColor) {
        red = (red - other.red).coerceIn(redRange)
        green = (green - other.green).coerceIn(greenRange)
        blue = (blue - other.blue).coerceIn(blueRange)
        alpha = (alpha - other.alpha).coerceIn(alphaRange)
    }

    override operator fun times(other: ARGBColor): Color {
        return super.times(other) as Color
    }

    operator fun timesAssign(other: ARGBColor) {
        redF = (redF * other.redF).coerceIn(redFRange)
        greenF = (greenF * other.greenF).coerceIn(greenFRange)
        blueF = (blueF * other.blueF).coerceIn(blueFRange)
        alphaF = (alphaF * other.alphaF).coerceIn(alphaFRange)
    }

    override operator fun div(other: ARGBColor): Color {
        return super.div(other) as Color
    }

    operator fun divAssign(other: ARGBColor) {
        red = (red / other.red).coerceIn(redRange)
        green = (green / other.green).coerceIn(greenRange)
        blue = (blue / other.blue).coerceIn(blueRange)
        alpha = (alpha / other.alpha).coerceIn(alphaRange)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Color

        return argb == other.argb
    }

    override fun hashCode(): Int {
        return argb
    }

    override fun toString(): String {
        return "Color(argb=$argb, hexStr='$hexStr', red=$red, green=$green, blue=$blue, alpha=$alpha)"
    }


}