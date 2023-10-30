package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive

open class ConfigEnum<E : Enum<E>>(
    override val key: String,
    final override val defaultValue: E
) : ConfigBase<E, ConfigEnum<E>>() {

    override var configValue: E = defaultValue

    override fun serialization(): SerializeElement {
        return SerializePrimitive(configValue.name)
    }

    override fun deserialization(serializeElement: SerializeElement) {
        return serializeElement.asString.let { java.lang.Enum.valueOf(configValue.javaClass, it) }
    }

}