package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive

class ConfigString(
    override val key: String,
    override val defaultValue: String
) : ConfigBase<String, ConfigString>() {
    override var configValue: String = defaultValue

    override fun serialization(): SerializeElement =
        SerializePrimitive(configValue)

    override fun deserialization(serializeElement: SerializeElement) {
        setValue(serializeElement.asString)
    }

}