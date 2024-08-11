package moe.forpleuvoir.nebula.common.util.math

fun lerp(from: Float, to: Float, fraction: Float): Float {
    check(fraction in 0.0f..1.0f) { "fraction must be between 0.0 and 1.0" }
    return from + fraction * (to - from)
}