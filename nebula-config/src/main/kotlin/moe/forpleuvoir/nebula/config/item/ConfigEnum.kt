@file:Suppress("unused")

package moe.forpleuvoir.nebula.config.item

import moe.forpleuvoir.nebula.config.ConfigGroup
import moe.forpleuvoir.nebula.config.config
import moe.forpleuvoir.nebula.serialization.codec.Codec
import moe.forpleuvoir.nebula.serialization.codec.enum

context(group: ConfigGroup)
inline fun <reified T : Enum<T>> configEnum(name: String, default: T) =
    config(name, default, Codec.enum())