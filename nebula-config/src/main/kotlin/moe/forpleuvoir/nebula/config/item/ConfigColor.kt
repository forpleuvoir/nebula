package moe.forpleuvoir.nebula.config.item

import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.config.ConfigGroup
import moe.forpleuvoir.nebula.config.config
import moe.forpleuvoir.nebula.serialization.codec.Codec
import moe.forpleuvoir.nebula.serialization.codec.color

context(group: ConfigGroup)
fun color(name: String, defaultValue: Color) =
    config(name, defaultValue, Codec.color)