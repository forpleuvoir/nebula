package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.common.color.HSVColor
import moe.forpleuvoir.nebula.common.color.RGBColor
import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.container.ConfigContainer
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.extensions.deserialization
import moe.forpleuvoir.nebula.serialization.extensions.serialization
import moe.forpleuvoir.nebula.serialization.extensions.serializationAsObject

/**
 * 修改颜色时应该直接调用[setValue],而不是修改颜色对象
 * @param C : RGBColor
 * @property key String
 * @property defaultValue C
 * @property configValue C
 */
@Suppress("UNCHECKED_CAST")
abstract class ConfigRGBColor<C : RGBColor>(
    override val key: String,
    final override val defaultValue: C
) : ConfigBase<C, ConfigRGBColor<C>>() {


    override var configValue: C = defaultValue.clone() as C

    override fun setValue(value: C) {
        if (configValue isEquals value) return
        configValue = value.clone() as C
        onChange(this)
    }

    override fun getValue(): C {
        return configValue.clone() as C
    }

    override fun serialization(): SerializeElement =
        configValue.serialization()

}

class ConfigColor(key: String, defaultValue: Color) : ConfigRGBColor<Color>(key, defaultValue) {

    override fun serialization(): SerializeElement {
        return configValue.serializationAsObject()
    }

    override fun deserialization(serializeElement: SerializeElement) {
        setValue(Color.deserialization(serializeElement))
    }

}

fun ConfigContainer.color(key: String, defaultValue: Color) = addConfig(ConfigColor(key, defaultValue))

class ConfigHSVColor(key: String, defaultValue: HSVColor) : ConfigRGBColor<HSVColor>(key, defaultValue) {

    override fun serialization(): SerializeElement {
        return configValue.serializationAsObject()
    }

    override fun deserialization(serializeElement: SerializeElement) {
        setValue(HSVColor.deserialization(serializeElement))
    }

}

fun ConfigContainer.hsvColor(key: String, defaultValue: HSVColor) = addConfig(ConfigHSVColor(key, defaultValue))