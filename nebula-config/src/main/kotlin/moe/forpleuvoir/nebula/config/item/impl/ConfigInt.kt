package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.common.util.clamp
import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.item.ConfigNumberValue
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive

class ConfigInt(
	key: String,
	defaultValue: Int,
	minValue: Int = Int.MIN_VALUE,
	maxValue: Int = Int.MAX_VALUE
) : ConfigNumber<Int>(key, defaultValue, minValue, maxValue) {

	override val mapping: Number.() -> Int get() = Number::toInt

}