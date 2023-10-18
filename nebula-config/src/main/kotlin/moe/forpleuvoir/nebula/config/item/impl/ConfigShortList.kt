package moe.forpleuvoir.nebula.config.item.impl

class ConfigShortList(key: String, defaultValue: List<Short>) : ConfigNumberList<Short>(key, defaultValue) {
    override val mapping: Number.() -> Short get() = Number::toShort

}