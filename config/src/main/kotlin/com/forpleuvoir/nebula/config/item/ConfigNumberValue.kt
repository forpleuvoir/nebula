package com.forpleuvoir.nebula.config.item

import com.forpleuvoir.nebula.config.ConfigValue

interface ConfigNumberValue<T : Number> : ConfigValue<T> {

	val minValue: T

	val maxValue: T

}