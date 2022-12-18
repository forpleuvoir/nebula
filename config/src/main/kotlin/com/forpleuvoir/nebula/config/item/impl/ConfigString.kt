package com.forpleuvoir.nebula.config.item.impl

import com.forpleuvoir.nebula.config.ConfigBase
import com.forpleuvoir.nebula.config.ConfigValue
import com.forpleuvoir.nebula.serialization.base.SerializeElement
import com.forpleuvoir.nebula.serialization.base.SerializePrimitive

class ConfigString(
	override val key: String,
	override val defaultValue: String
) : ConfigBase<String, ConfigString>(), ConfigValue<String> {
	override var configValue: String = defaultValue

	override fun serialization(): SerializeElement =
		SerializePrimitive(configValue)

	override fun deserialization(serializeElement: SerializeElement) {
		configValue = serializeElement.asString
	}

}