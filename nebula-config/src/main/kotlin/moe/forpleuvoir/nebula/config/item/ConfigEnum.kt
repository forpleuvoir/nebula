@file:Suppress("unused")

package moe.forpleuvoir.nebula.config.item

import moe.forpleuvoir.nebula.config.ConfigGroup
import moe.forpleuvoir.nebula.config.ConfigItem
import moe.forpleuvoir.nebula.config.ConfigSerde
import moe.forpleuvoir.nebula.serialization.codec.Codec
import moe.forpleuvoir.nebula.serialization.codec.enum

class ConfigEnum<E : Enum<E>>(
    name: String,
    defaultValue: E,
    serde: ConfigSerde<E>
) : ConfigItem<E>(name, defaultValue, serde)

context(group: ConfigGroup)
inline fun <reified T : Enum<T>> configEnum(name: String, default: T) =
    group.addConfig(ConfigEnum(name, default, ConfigSerde.of(Codec.enum())))