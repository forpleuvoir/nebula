package com.forpleuvoir.nebula.config.item.impl

import com.forpleuvoir.nebula.config.ConfigBase
import com.forpleuvoir.nebula.config.item.ConfigCycleValue
import com.forpleuvoir.nebula.serialization.base.SerializeElement

class ConfigCycle<T>(
    override val key: String,
    override val items: List<T>,
    override val defaultValue: T = items[0],
    private val asString: (T) -> String,
    private val deserializer: (SerializeElement) -> T,
    private val serializer: (T) -> SerializeElement
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
        configValue = deserializer(serializeElement)
    }

    override fun serialization(): SerializeElement {
        return serializer(configValue)
    }

}