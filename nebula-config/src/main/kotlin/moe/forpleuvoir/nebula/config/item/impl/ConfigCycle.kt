package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.container.ConfigContainer
import moe.forpleuvoir.nebula.config.item.ConfigCycleValue
import moe.forpleuvoir.nebula.serialization.base.SerializeElement

class ConfigCycle<T>(
    override val key: String,
    override val items: List<T>,
    override val defaultValue: T = items[0],
    private val asString: (T) -> String,
    private val serializer: (T) -> SerializeElement,
    private val deserializer: (SerializeElement) -> T,
) : ConfigBase<T, ConfigCycle<T>>(), ConfigCycleValue<T> {

    init {
        checkValue(defaultValue)
    }

    override var configValue: T = defaultValue

    override fun asString(): String {
        return asString(configValue)
    }

    private fun checkValue(value: T): T {
        if (!items.contains(value)) {
            throw IllegalArgumentException("[value:$value] must come from within [items:$items]")
        }
        return value
    }

    override fun deserialization(serializeElement: SerializeElement) {
        setValue(deserializer(serializeElement))
    }

    override fun serialization(): SerializeElement {
        return serializer(configValue)
    }

}

fun <T> ConfigContainer.cycle(
    key: String,
    items: List<T>,
    defaultValue: T = items[0],
    asString: (T) -> String,
    serializer: (T) -> SerializeElement,
    deserializer: (SerializeElement) -> T,
) = addConfig(ConfigCycle(key, items, defaultValue, asString, serializer, deserializer))