package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive

abstract class ConfigNumberList<T>(
    key: String,
    defaultValue: List<T>,
    deserializer: (SerializeElement) -> T
) : ConfigList<T>(key, defaultValue, { SerializePrimitive(it) }, deserializer) where T : Number, T : Comparable<T>

class ConfigByteList(key: String, defaultValue: List<Byte>) : ConfigNumberList<Byte>(key, defaultValue, { it.asByte })

class ConfigShortList(key: String, defaultValue: List<Short>) : ConfigNumberList<Short>(key, defaultValue, { it.asShort })

class ConfigIntList(key: String, defaultValue: List<Int>) : ConfigNumberList<Int>(key, defaultValue, { it.asInt })

class ConfigLongList(key: String, defaultValue: List<Long>) : ConfigNumberList<Long>(key, defaultValue, { it.asLong })

class ConfigFloatList(key: String, defaultValue: List<Float>) : ConfigNumberList<Float>(key, defaultValue, { it.asFloat })

class ConfigDoubleList(key: String, defaultValue: List<Double>) : ConfigNumberList<Double>(key, defaultValue, { it.asDouble })