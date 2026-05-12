package moe.forpleuvoir.nebula.common.util.math

fun Int.lerp(to: Int, fraction: Float): Int {
    check(fraction in 0.0f..1.0f) { "fraction must be between 0.0 and 1.0" }
    return (this + fraction * (to - this)).toInt()
}

fun Long.lerp(to: Long, fraction: Float): Long {
    check(fraction in 0.0f..1.0f) { "fraction must be between 0.0 and 1.0" }
    return (this + fraction * (to - this)).toLong()
}

fun Float.lerp(to: Float, fraction: Float): Float {
    check(fraction in 0.0f..1.0f) { "fraction must be between 0.0 and 1.0" }
    return this + fraction * (to - this)
}

fun Double.lerp(to: Double, fraction: Float): Double {
    check(fraction in 0.0f..1.0f) { "fraction must be between 0.0 and 1.0" }
    return this + fraction * (to - this)
}