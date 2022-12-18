package com.forpleuvoir.nebula.config.item.impl

import com.forpleuvoir.nebula.common.util.clamp
import com.forpleuvoir.nebula.config.ConfigBase
import com.forpleuvoir.nebula.config.item.ConfigNumberValue
import com.forpleuvoir.nebula.serialization.base.SerializeElement
import com.forpleuvoir.nebula.serialization.base.SerializePrimitive

class ConfigDouble(
	override val key: String,
	override val defaultValue: Double,
	override val minValue: Double = Double.MIN_VALUE,
	override val maxValue: Double = Double.MAX_VALUE
) : ConfigBase<Double, ConfigDouble>(), ConfigNumberValue<Double> {

	override var configValue: Double = defaultValue.clamp(minValue, maxValue)

	override fun serialization(): SerializeElement =
		SerializePrimitive(configValue)

	override fun deserialization(serializeElement: SerializeElement) {
		configValue = serializeElement.asDouble.clamp(minValue, maxValue)
	}

}