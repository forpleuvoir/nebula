package com.forpleuvoir.nebula.config.item.impl

import com.forpleuvoir.nebula.common.color.Color
import com.forpleuvoir.nebula.config.ConfigBase
import com.forpleuvoir.nebula.serialization.base.SerializeElement
import com.forpleuvoir.nebula.serialization.extensions.deserialization
import com.forpleuvoir.nebula.serialization.extensions.serialization

class ConfigColor(
	override val key: String,
	override val defaultValue: Color
) : ConfigBase<Color, ConfigColor>() {

	override var configValue: Color = defaultValue.copy()

	override fun setValue(value: Color) {
		if (configValue == value) return
		configValue = value.copy()
		onChange(this)
	}

	override fun getValue(): Color {
		return configValue.copy()
	}

	override fun serialization(): SerializeElement =
		configValue.serialization()

	override fun deserialization(serializeElement: SerializeElement) {
		configValue.deserialization(serializeElement)
	}

}