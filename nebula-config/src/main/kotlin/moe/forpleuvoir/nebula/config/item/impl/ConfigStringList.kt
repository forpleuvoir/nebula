package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.config.container.ConfigContainer
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive

class ConfigStringList(
    key: String,
    defaultValue: List<String>
) : ConfigList<String>(key, defaultValue, { SerializePrimitive(it) }, { it.asString })

fun ConfigContainer.stringList(key: String, defaultValue: List<String>) = addConfig(ConfigStringList(key, defaultValue))