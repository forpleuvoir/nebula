package moe.forpleuvoir.nebula.config.item.impl

class ConfigFloatList(key: String, defaultValue: List<Float>) : ConfigNumberList<Float>(key, defaultValue) {
    override val mapping: Number.() -> Float get() = Number::toFloat

}