package moe.forpleuvoir.nebula.config.item.impl
class ConfigFloat(
    key: String,
    defaultValue: Float,
    minValue: Float = Float.MIN_VALUE,
    maxValue: Float = Float.MAX_VALUE
) : ConfigNumber<Float>(key, defaultValue, minValue, maxValue) {

    override val mapping: Number.() -> Float get() = Number::toFloat

}