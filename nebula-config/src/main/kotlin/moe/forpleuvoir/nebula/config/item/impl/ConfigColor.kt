package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.extensions.deserialization
import moe.forpleuvoir.nebula.serialization.extensions.serialization

class ConfigColor(
    override val key: String,
    override val defaultValue: Color
) : ConfigBase<Color, ConfigColor>() {

    override var configValue: Color = defaultValue.clone()

    override fun setValue(value: Color) {
        if (configValue isEquals value) return
        configValue = value.clone()
        onChange(this)
    }

    override fun getValue(): Color {
        return configValue.clone()
    }

    override fun serialization(): SerializeElement =
        configValue.serialization()

    override fun deserialization(serializeElement: SerializeElement) {
        setValue(Color(0xFFFFFFFFu).apply { deserialization(serializeElement) })
    }

}