package com.forpleuvoir.nebula.config.item.impl

import com.forpleuvoir.nebula.common.util.clamp
import com.forpleuvoir.nebula.config.ConfigBase
import com.forpleuvoir.nebula.config.item.ConfigNumberValue
import com.forpleuvoir.nebula.serialization.base.SerializeElement
import com.forpleuvoir.nebula.serialization.base.SerializePrimitive

class ConfigInt(
	override val key: String,
	override val defaultValue: Int,
	override val minValue: Int = Int.MIN_VALUE,
	override val maxValue: Int = Int.MAX_VALUE
) : ConfigBase<Int, ConfigInt>(), ConfigNumberValue<Int> {

	override var configValue: Int = defaultValue.clamp(minValue, maxValue)

	override fun setValue(value: Int) {
		super.setValue(value.clamp(minValue, maxValue))
	}

	override fun serialization(): SerializeElement =
		SerializePrimitive(configValue)

	override fun deserialization(serializeElement: SerializeElement) {
		configValue = serializeElement.asInt.clamp(minValue, maxValue)
	}

}