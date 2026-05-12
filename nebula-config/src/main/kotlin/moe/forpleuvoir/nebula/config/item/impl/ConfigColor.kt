package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.common.color.ARGBColor
import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.container.ConfigContainer
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.codec.serialization

/**
 * 修改颜色时应该直接调用[setValue],而不是修改颜色对象
 * @param C : RGBColor
 * @property key String
 * @property defaultValue C
 * @property configValue C
 */
@Suppress("UNCHECKED_CAST")
abstract class ConfigColor(
    override val key: String,
    final override val defaultValue: Color
) : ConfigBase<Color, ConfigColor>() {

    override var configValue: Color = defaultValue

    override fun serialization(): SerializeElement {
        return if (configValue is ARGBColor)
            (configValue as ARGBColor).serialization()
        else
            configValue.serialization()
    }

}


fun ConfigContainer.color(key: String, defaultValue: Color) = addConfig(ConfigColor(key, defaultValue))
