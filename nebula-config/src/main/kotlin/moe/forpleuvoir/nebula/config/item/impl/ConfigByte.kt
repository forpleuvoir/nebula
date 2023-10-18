package moe.forpleuvoir.nebula.config.item.impl

class ConfigByte(
    key: String,
    defaultValue: Byte,
    minValue: Byte = Byte.MIN_VALUE,
    maxValue: Byte = Byte.MAX_VALUE
) : ConfigNumber<Byte>(key, defaultValue, minValue, maxValue) {

    override val mapping: Number.() -> Byte get() = Number::toByte

}