package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.container.ConfigContainer
import moe.forpleuvoir.nebula.config.item.ConfigCycleValue
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive

class ConfigCycleString(
    override val key: String,
    override val items: List<String>,
    override val defaultValue: String = items[0]
) : ConfigBase<String, ConfigCycleString>(), ConfigCycleValue<String> {

    init {
        checkValue(defaultValue)
    }

    private fun checkValue(value: String): String {
        if (!items.contains(value)) {
            throw IllegalArgumentException("[value:$value] must come from within [items:$items]")
        }
        return value
    }

    override var configValue: String = defaultValue

    override fun serialization(): SerializeElement =
        SerializePrimitive(configValue)

    override fun deserialization(serializeElement: SerializeElement) {
        setValue(checkValue(serializeElement.asString))
    }

}

fun ConfigContainer.cycleString(
    key: String,
    items: List<String>,
    defaultValue: String = items[0],
) = addConfig(ConfigCycleString(key, items, defaultValue))