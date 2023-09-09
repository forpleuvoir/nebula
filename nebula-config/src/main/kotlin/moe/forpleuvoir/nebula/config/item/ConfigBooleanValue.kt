package moe.forpleuvoir.nebula.config.item

import moe.forpleuvoir.nebula.config.ConfigValue

interface ConfigBooleanValue : ConfigValue<Boolean> {

	fun toggle() {
		setValue(!getValue())
	}

}