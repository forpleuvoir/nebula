package moe.forpleuvoir.nebula.config.item

import moe.forpleuvoir.nebula.config.ConfigValue

interface ConfigNumberValue<T : Number> : ConfigValue<T> {

	val minValue: T

	val maxValue: T

}