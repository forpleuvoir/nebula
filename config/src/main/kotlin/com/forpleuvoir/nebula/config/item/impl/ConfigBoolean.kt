package com.forpleuvoir.nebula.config.item.impl

import com.forpleuvoir.nebula.config.ConfigBase
import com.forpleuvoir.nebula.config.item.ConfigBooleanValue
import com.forpleuvoir.nebula.serialization.base.SerializeElement
import com.forpleuvoir.nebula.serialization.base.SerializePrimitive

class ConfigBoolean(
	override val key: String,
	override val defaultValue: Boolean
) : ConfigBase<Boolean, ConfigBoolean>(), ConfigBooleanValue {

	override var configValue: Boolean = defaultValue

	override fun serialization(): SerializeElement =
		SerializePrimitive(configValue)

	override fun deserialization(serializeElement: SerializeElement) {
		configValue = serializeElement.asBoolean
	}

}