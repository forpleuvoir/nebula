package moe.forpleuvoir.nebula.config.item.impl

class ConfigLongList(key: String, defaultValue: List<Long>) : ConfigNumberList<Long>(key, defaultValue) {
    override val mapping: Number.() -> Long get() = Number::toLong

}