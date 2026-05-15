package moe.forpleuvoir.nebula.common.color

internal object HSVHelper {

    private const val MAX_CACHE_SIZE = 10000

    private fun rgb2HSV(red: Int, green: Int, blue: Int): HSV {
        val r = red / 255f
        val g = green / 255f
        val b = blue / 255f
        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)
        val delta = max - min

        val hue = when {
            delta == 0f -> 0f
            max == r -> ((g - b) / delta) % 6f
            max == g -> ((b - r) / delta) + 2f
            else     -> ((r - g) / delta) + 4f
        }.let { (it / 6f).let { h -> if (h < 0f) h + 1f else h } }

        val saturation = if (max == 0f) 0f else delta / max
        return HSV(hue, saturation, max)
    }

    fun hsv2RGB(hue: Float, saturation: Float, value: Float): Int {
        if (saturation == 0f) {
            val v = (value * 255f + 0.5f).toInt()
            return (0xFF shl 24) or (v shl 16) or (v shl 8) or v
        }
        val h6 = hue * 6f
        val i = h6.toInt()
        val f = h6 - i
        val p = value * (1f - saturation)
        val q = value * (1f - saturation * f)
        val t = value * (1f - saturation * (1f - f))
        val (r, g, b) = when (i % 6) {
            0 -> Triple(value, t, p)
            1 -> Triple(q, value, p)
            2 -> Triple(p, value, t)
            3 -> Triple(p, q, value)
            4 -> Triple(t, p, value)
            5 -> Triple(value, p, q)
            else -> Triple(0f, 0f, 0f)
        }
        val ri = (r * 255f + 0.5f).toInt()
        val gi = (g * 255f + 0.5f).toInt()
        val bi = (b * 255f + 0.5f).toInt()
        return (0xFF shl 24) or (ri shl 16) or (gi shl 8) or bi
    }

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
        val rgb = hsv2RGB(hue, saturation, value) and 0x00FFFFFF
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
        val result = color.rgb((hsv2RGB(newHue, new.saturation, new.value) and 0x00FFFFFF))
        cache[result.rgb] = new
        return result
    }

    @Synchronized
    fun getSaturation(color: Color): Float =
        getOrPut(color.rgb).saturation

    @Synchronized
    fun setSaturation(color: Color, newSaturation: Float): Color {
        val new = getOrPut(color.rgb).saturation(newSaturation)
        val result = color.rgb((hsv2RGB(new.hue, newSaturation, new.value) and 0x00FFFFFF))
        cache[result.rgb] = new
        return result
    }

    @Synchronized
    fun getValue(color: Color): Float =
        getOrPut(color.rgb).value

    @Synchronized
    fun setValue(color: Color, newValue: Float): Color {
        val new = getOrPut(color.rgb).value(newValue)
        val result = color.rgb((hsv2RGB(new.hue, new.saturation, newValue) and 0x00FFFFFF))
        cache[result.rgb] = new
        return result
    }

}

@Suppress("NOTHING_TO_INLINE")
@JvmInline
internal value class HSV(val hsv: Long) {

    constructor(hue: Float, saturation: Float, value: Float) : this(
        ((hue * 65535).toLong() and 0xFFFF) or
                (((saturation * 65535).toLong() and 0xFFFF) shl 16) or
                (((value * 65535).toLong() and 0xFFFF) shl 32)
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