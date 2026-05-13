@file:Suppress("unused")

package moe.forpleuvoir.nebula.config.item

import moe.forpleuvoir.nebula.config.Config
import moe.forpleuvoir.nebula.config.ConfigGroup
import moe.forpleuvoir.nebula.config.ConfigSerde
import moe.forpleuvoir.nebula.config.config
import moe.forpleuvoir.nebula.serialization.codec.Codec

context(group: ConfigGroup)
fun string(name: String, defaultValue: String) = config(name, defaultValue, Codec.string(defaultValue))

context(group: ConfigGroup)
fun string(name: String, defaultValue: Char) = config(name, defaultValue, Codec.char(defaultValue))

class ConfigBoolean(
    name: String,
    defaultValue: Boolean,
) : Config<Boolean>(name, defaultValue, ConfigSerde.of(Codec.boolean(defaultValue))) {
    fun toggle(): ConfigBoolean {
        setValue(!getValue())
        return this
    }
}

context(group: ConfigGroup)
fun boolean(name: String, defaultValue: Boolean): ConfigBoolean = group.addConfig(ConfigBoolean(name, defaultValue))


@Suppress("NOTHING_TO_INLINE")
context(group: ConfigGroup)
private inline fun <T> number(name: String, defaultValue: T, minValue: T, maxValue: T, serde: ConfigSerde<T>): ConfigRange<T> where T : Comparable<T> =
    configRange(name, defaultValue, minValue, maxValue, serde)

context(group: ConfigGroup)
fun byte(name: String, defaultValue: Byte, minValue: Byte = Byte.MIN_VALUE, maxValue: Byte = Byte.MAX_VALUE) =
    number(name, defaultValue, minValue, maxValue, ConfigSerde.of(Codec.byte(defaultValue, minValue..maxValue)))

context(group: ConfigGroup)
fun short(name: String, defaultValue: Short, minValue: Short = Short.MIN_VALUE, maxValue: Short = Short.MAX_VALUE) =
    number(name, defaultValue, minValue, maxValue, ConfigSerde.of(Codec.short(defaultValue, minValue..maxValue)))

context(group: ConfigGroup)
fun int(name: String, defaultValue: Int, minValue: Int = Int.MIN_VALUE, maxValue: Int = Int.MAX_VALUE) =
    number(name, defaultValue, minValue, maxValue, ConfigSerde.of(Codec.int(defaultValue, minValue..maxValue)))

context(group: ConfigGroup)
fun long(name: String, defaultValue: Long, minValue: Long = Long.MIN_VALUE, maxValue: Long = Long.MAX_VALUE) =
    number(name, defaultValue, minValue, maxValue, ConfigSerde.of(Codec.long(defaultValue, minValue..maxValue)))

context(group: ConfigGroup)
fun float(name: String, defaultValue: Float, minValue: Float = Float.MIN_VALUE, maxValue: Float = Float.MAX_VALUE) =
    number(name, defaultValue, minValue, maxValue, ConfigSerde.of(Codec.float(defaultValue, minValue..maxValue)))

context(group: ConfigGroup)
fun double(name: String, defaultValue: Double, minValue: Double = Double.MIN_VALUE, maxValue: Double = Double.MAX_VALUE) =
    number(name, defaultValue, minValue, maxValue, ConfigSerde.of(Codec.double(defaultValue, minValue..maxValue)))