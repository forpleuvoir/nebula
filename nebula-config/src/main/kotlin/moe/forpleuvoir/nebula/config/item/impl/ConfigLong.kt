package moe.forpleuvoir.nebula.config.item.impl

class ConfigLong(
    key: String,
    defaultValue: Long,
    minValue: Long = Long.MIN_VALUE,
    maxValue: Long = Long.MAX_VALUE
) : ConfigNumber<Long>(key, defaultValue, minValue, maxValue) {

    override val mapping: Number.() -> Long get() = Number::toLong

}