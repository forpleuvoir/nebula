package com.forpleuvoir.nebula.config.item

import com.forpleuvoir.nebula.config.ConfigValue

interface ConfigBooleanValue : ConfigValue<Boolean> {

	fun toggle() {
		setValue(!getValue())
	}

}