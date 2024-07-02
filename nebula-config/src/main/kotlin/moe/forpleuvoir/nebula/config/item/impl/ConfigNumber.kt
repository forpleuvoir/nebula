package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.container.ConfigContainer
import moe.forpleuvoir.nebula.config.item.ConfigNumberValue
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive

open class ConfigNumber<T>(
    override val key: String,
    final override val defaultValue: T,
    final override val minValue: T,
    final override val maxValue: T,
    protected val deserializer: (SerializeElement) -> T
) : ConfigBase<T, ConfigNumber<T>>(), ConfigNumberValue<T> where T : Number, T : Comparable<T> {

    override var configValue: T = defaultValue.clamp()

    final override fun T.clamp(): T {
        return if (this > maxValue) maxValue
        else if (this < minValue) minValue
        else this
    }

    override fun setValue(value: T) {
        super.setValue(value.clamp())
    }

    override fun serialization(): SerializeElement =
        SerializePrimitive(configValue)

    override fun deserialization(serializeElement: SerializeElement) {
        setValue(deserializer(serializeElement).clamp())
    }

}

fun <T> ConfigContainer.number(
    key: String,
    defaultValue: T,
    minValue: T,
    maxValue: T,
    deserializer: (SerializeElement) -> T
) where T : Number, T : Comparable<T> = addConfig(ConfigNumber(key, defaultValue, minValue, maxValue, deserializer))


class ConfigByte(
    key: String,
    defaultValue: Byte,
    minValue: Byte = Byte.MIN_VALUE,
    maxValue: Byte = Byte.MAX_VALUE
) : ConfigNumber<Byte>(key, defaultValue, minValue, maxValue, { it.asByte })

fun ConfigContainer.byte(
    key: String,
    defaultValue: Byte,
    minValue: Byte = Byte.MIN_VALUE,
    maxValue: Byte = Byte.MAX_VALUE
) = addConfig(ConfigByte(key, defaultValue, minValue, maxValue))

class ConfigShort(
    key: String,
    defaultValue: Short,
    minValue: Short = Short.MIN_VALUE,
    maxValue: Short = Short.MAX_VALUE
) : ConfigNumber<Short>(key, defaultValue, minValue, maxValue, { it.asShort })

fun ConfigContainer.short(
    key: String,
    defaultValue: Short,
    minValue: Short = Short.MIN_VALUE,
    maxValue: Short = Short.MAX_VALUE
) = addConfig(ConfigShort(key, defaultValue, minValue, maxValue))

class ConfigInt(
    key: String,
    defaultValue: Int,
    minValue: Int = Int.MIN_VALUE,
    maxValue: Int = Int.MAX_VALUE
) : ConfigNumber<Int>(key, defaultValue, minValue, maxValue, { it.asInt })

fun ConfigContainer.int(
    key: String,
    defaultValue: Int,
    minValue: Int = Int.MIN_VALUE,
    maxValue: Int = Int.MAX_VALUE
) = addConfig(ConfigInt(key, defaultValue, minValue, maxValue))

class ConfigLong(
    key: String,
    defaultValue: Long,
    minValue: Long = Long.MIN_VALUE,
    maxValue: Long = Long.MAX_VALUE
) : ConfigNumber<Long>(key, defaultValue, minValue, maxValue, { it.asLong })

fun ConfigContainer.long(
    key: String,
    defaultValue: Long,
    minValue: Long = Long.MIN_VALUE,
    maxValue: Long = Long.MAX_VALUE
) = addConfig(ConfigLong(key, defaultValue, minValue, maxValue))

class ConfigFloat(
    key: String,
    defaultValue: Float,
    minValue: Float = Float.MIN_VALUE,
    maxValue: Float = Float.MAX_VALUE
) : ConfigNumber<Float>(key, defaultValue, minValue, maxValue, { it.asFloat })

fun ConfigContainer.float(
    key: String,
    defaultValue: Float,
    minValue: Float = Float.MIN_VALUE,
    maxValue: Float = Float.MAX_VALUE
) = addConfig(ConfigFloat(key, defaultValue, minValue, maxValue))

class ConfigDouble(
    key: String,
    defaultValue: Double,
    minValue: Double = Double.MIN_VALUE,
    maxValue: Double = Double.MAX_VALUE
) : ConfigNumber<Double>(key, defaultValue, minValue, maxValue, { it.asDouble })

fun ConfigContainer.double(
    key: String,
    defaultValue: Double,
    minValue: Double = Double.MIN_VALUE,
    maxValue: Double = Double.MAX_VALUE
) = addConfig(ConfigDouble(key, defaultValue, minValue, maxValue))