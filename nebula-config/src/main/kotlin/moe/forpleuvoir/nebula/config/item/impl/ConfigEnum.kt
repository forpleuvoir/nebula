package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.container.ConfigContainer
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import moe.forpleuvoir.nebula.serialization.extensions.deserialization

open class ConfigEnum<E : Enum<E>>(
    override val key: String,
    final override val defaultValue: E
) : ConfigBase<E, ConfigEnum<E>>() {

    override var configValue: E = defaultValue

    override fun serialization(): SerializeElement {
        return SerializePrimitive(configValue.name)
    }

    override fun deserialization(serializeElement: SerializeElement) {
        setValue(Enum.deserialization(configValue::class,serializeElement))
    }

}

fun <E : Enum<E>> ConfigContainer.enum(key: String, defaultValue: E) = addConfig(ConfigEnum(key, defaultValue))