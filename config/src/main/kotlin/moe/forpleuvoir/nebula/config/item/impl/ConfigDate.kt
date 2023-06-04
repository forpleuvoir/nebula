package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import java.util.*

class ConfigDate(
	override val key: String,
	override val defaultValue: Date
) : ConfigBase<Date, ConfigDate>() {

	override var configValue: Date = Date(defaultValue.time)
	override fun serialization(): SerializeElement =
		SerializePrimitive(configValue.time)

	override fun deserialization(serializeElement: SerializeElement) {
		configValue.time = serializeElement.asLong
	}

}