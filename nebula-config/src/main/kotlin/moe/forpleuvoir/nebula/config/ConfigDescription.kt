package moe.forpleuvoir.nebula.config

import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive

class ConfigDescription(val config: ConfigSerializable, private val description: String, keyMap: (String) -> String = { "@$it" }) : ConfigSerializable {

    override val key: String = keyMap(config.key).let { if (it == config.key) throw IllegalArgumentException("keyMap must change the key") else it }

    override fun init() = Unit

    override fun serialization(): SerializeElement {
        return SerializePrimitive(description)
    }

    // 不需要反序列化
    override fun deserialization(serializeElement: SerializeElement) = Unit
}