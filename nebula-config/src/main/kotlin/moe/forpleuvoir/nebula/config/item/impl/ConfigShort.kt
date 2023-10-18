package moe.forpleuvoir.nebula.config.item.impl

class ConfigShort(
    key: String,
    defaultValue: Short,
    minValue: Short = Short.MIN_VALUE,
    maxValue: Short = Short.MAX_VALUE
) : ConfigNumber<Short>(key, defaultValue, minValue, maxValue) {

    override val mapping: Number.() -> Short get() = Number::toShort

}