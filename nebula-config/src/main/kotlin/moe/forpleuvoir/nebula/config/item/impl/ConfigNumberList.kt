package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.config.container.ConfigContainer
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive

open class ConfigNumberList<T>(
    key: String,
    defaultValue: List<T>,
    deserializer: (SerializeElement) -> T
) : ConfigList<T>(key, defaultValue, { SerializePrimitive(it) }, deserializer) where T : Number, T : Comparable<T>

fun <T> ConfigContainer.numberList(
    key: String,
    defaultValue: List<T>,
    deserializer: (SerializeElement) -> T
) where T : Number, T : Comparable<T> = addConfig(ConfigNumberList(key, defaultValue, deserializer))

class ConfigByteList(key: String, defaultValue: List<Byte>) : ConfigNumberList<Byte>(key, defaultValue, { it.asByte })

fun ConfigContainer.byteList(key: String, defaultValue: List<Byte>) = addConfig(ConfigByteList(key, defaultValue))

class ConfigShortList(key: String, defaultValue: List<Short>) : ConfigNumberList<Short>(key, defaultValue, { it.asShort })

fun ConfigContainer.shortList(key: String, defaultValue: List<Short>) = addConfig(ConfigShortList(key, defaultValue))

class ConfigIntList(key: String, defaultValue: List<Int>) : ConfigNumberList<Int>(key, defaultValue, { it.asInt })

fun ConfigContainer.intList(key: String, defaultValue: List<Int>) = addConfig(ConfigIntList(key, defaultValue))

class ConfigLongList(key: String, defaultValue: List<Long>) : ConfigNumberList<Long>(key, defaultValue, { it.asLong })

fun ConfigContainer.longList(key: String, defaultValue: List<Long>) = addConfig(ConfigLongList(key, defaultValue))

class ConfigFloatList(key: String, defaultValue: List<Float>) : ConfigNumberList<Float>(key, defaultValue, { it.asFloat })

fun ConfigContainer.floatList(key: String, defaultValue: List<Float>) = addConfig(ConfigFloatList(key, defaultValue))

class ConfigDoubleList(key: String, defaultValue: List<Double>) : ConfigNumberList<Double>(key, defaultValue, { it.asDouble })

fun ConfigContainer.doubleList(key: String, defaultValue: List<Double>) = addConfig(ConfigDoubleList(key, defaultValue))