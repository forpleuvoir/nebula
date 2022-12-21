package com.forpleuvoir.nebula.config.item.impl

import com.forpleuvoir.nebula.config.ConfigBase
import com.forpleuvoir.nebula.config.item.ConfigCycleValue
import com.forpleuvoir.nebula.serialization.base.SerializeElement
import com.forpleuvoir.nebula.serialization.base.SerializePrimitive

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
		configValue = checkValue(serializeElement.asString)
	}


}