package moe.forpleuvoir.nebula.common.color

internal object HSVHelper {

    private const val MAX_CACHE_SIZE = 10000

    private fun rgb2HSV(red: Int, green: Int, blue: Int): HSV {
        val hsv = FloatArray(3) { 0f }
        java.awt.Color.RGBtoHSB(red, green, blue, hsv)
        return HSV(hsv[0], hsv[1], hsv[2])
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun hsv2RGB(hue: Float, saturation: Float, value: Float): Int = java.awt.Color.HSBtoRGB(hue, saturation, value)

    val cache = object : LinkedHashMap<Int, HSV>(MAX_CACHE_SIZE / 2, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<Int, HSV>): Boolean = size > MAX_CACHE_SIZE
    }

    @Synchronized
    fun getOrPut(color: Int): HSV =
        cache.getOrPut(color) {
            val c = Color.fromRGB(color)
            rgb2HSV(c.red, c.green, c.blue)
        }

    @Synchronized
    fun getOrPut(hue: Float, saturation: Float, value: Float): Int {
        val rgb = hsv2RGB(hue, saturation, value)
        cache.getOrPut(rgb) {
            HSV(hue, saturation, value)
        }
        return rgb
    }

    @Synchronized
    fun getHue(color: Color): Float =
        getOrPut(color.rgb).hue

    @Synchronized
    fun setHue(color: Color, newHue: Float): Color {
        val new = getOrPut(color.rgb).hue(newHue)
        val result = color.rgb(hsv2RGB(newHue, new.saturation, new.value))
        cache[result.rgb] = new
        return result
    }

    @Synchronized
    fun getSaturation(color: Color): Float =
        getOrPut(color.rgb).saturation

    @Synchronized
    fun setSaturation(color: Color, newSaturation: Float): Color {
        val new = getOrPut(color.rgb).saturation(newSaturation)
        val result = color.rgb(hsv2RGB(new.hue, newSaturation, new.value))
        cache[result.rgb] = new
        return result
    }

    @Synchronized
    fun getValue(color: Color): Float =
        getOrPut(color.rgb).value

    @Synchronized
    fun setValue(color: Color, newValue: Float): Color {
        val new = getOrPut(color.rgb).value(newValue)
        val result = color.rgb(hsv2RGB(new.hue, new.saturation, newValue))
        cache[result.rgb] = new
        return result
    }

}

@Suppress("NOTHING_TO_INLINE")
@JvmInline
internal value class HSV(val hsv: Long) {

    constructor(hue: Float, saturation: Float, value: Float) : this(
        ((hue * 65535).toLong() and 0xFFFF) or
                ((saturation * 65535).toLong() and 0xFFFF shl 16) or
                ((value * 65535).toLong() and 0xFFFF shl 32)
    )

    inline val hue: Float get() = (hsv and 0xFFFF) / 65535f

    inline fun hue(hue: Float): HSV = HSV(
        (hue * 65535).toLong() or
                (hsv and 0xFFFFFFFF0000)
    )

    inline val saturation: Float get() = ((hsv shr 16) and 0xFFFF) / 65535f

    inline fun saturation(saturation: Float): HSV = HSV(
        (hsv and 0xFFFF) or
                ((saturation * 65535).toLong() shl 16) or
                (hsv and 0xFFFF00000000)
    )

    inline val value: Float get() = ((hsv shr 32) and 0xFFFF) / 65535f

    inline fun value(value: Float): HSV = HSV(
        ((value * 65535).toLong() shl 32) or
                (hsv and 0xFFFFFFFF)
    )

    override fun toString(): String {
        return "HSV(hsv: $hsv, hue: ${hue}, saturation: ${saturation}, value: ${value})"
    }
}