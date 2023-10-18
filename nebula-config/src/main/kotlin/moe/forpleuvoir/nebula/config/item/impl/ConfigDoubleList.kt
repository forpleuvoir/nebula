package moe.forpleuvoir.nebula.config.item.impl

class ConfigDoubleList(key: String, defaultValue: List<Double>) : ConfigNumberList<Double>(key, defaultValue) {
    override val mapping: Number.() -> Double get() = Number::toDouble

}