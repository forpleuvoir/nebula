package moe.forpleuvoir.nebula.config.item.impl

class ConfigByteList(key: String, defaultValue: List<Byte>) : ConfigNumberList<Byte>(key, defaultValue) {
    override val mapping: Number.() -> Byte get() = Number::toByte

}