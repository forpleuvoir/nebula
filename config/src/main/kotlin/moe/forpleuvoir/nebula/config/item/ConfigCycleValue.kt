package moe.forpleuvoir.nebula.config.item

import moe.forpleuvoir.nebula.config.ConfigValue

interface ConfigCycleValue<T> : ConfigValue<T> {

	val items: List<T>

	fun cycles() {
		val index = items.indexOf(getValue())
		val size = items.size
		if (index < size - 1) {
			setValue(items[index + 1])
		} else {
			setValue(items[0])
		}
	}

}