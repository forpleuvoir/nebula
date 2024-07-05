package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.common.util.NotifiableLinkedHashMap
import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.container.ConfigContainer
import moe.forpleuvoir.nebula.config.item.ConfigMutableMapValue
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import moe.forpleuvoir.nebula.serialization.extensions.checkType
import moe.forpleuvoir.nebula.serialization.extensions.serializeObject

open class ConfigStringKeyMap<V : Any>(
    override val key: String,
    defaultValue: Map<String, V>,
    protected open val valueSerializer: (V) -> SerializeElement,
    protected open val valueDeserializer: (SerializeElement) -> V
) : ConfigBase<MutableMap<String, V>, ConfigStringKeyMap<V>>(), ConfigMutableMapValue<String, V> {

    final override val defaultValue: MutableMap<String, V> = LinkedHashMap(defaultValue)

    override var configValue: MutableMap<String, V> = map(this.defaultValue)

    private fun map(map: Map<String, V>): NotifiableLinkedHashMap<String, V> {
        return NotifiableLinkedHashMap(map).apply {
            subscribe {
                this@ConfigStringKeyMap.onChange(this@ConfigStringKeyMap)
            }
        }
    }

    override fun restDefault() {
        if (isDefault()) return
        configValue = map(defaultValue)
        onChange(this)
    }

    override fun serialization(): SerializeElement =
        serializeObject {
            getValue().forEach { (k, v) ->
                k to valueSerializer(v)
            }
        }

    override fun deserialization(serializeElement: SerializeElement) {
        setValue(serializeElement.checkType {
            check<SerializeObject> {
                this@ConfigStringKeyMap.map(it.mapValues { (_, value) ->
                    valueDeserializer(value)
                })
            }
        }.getOrThrow())
    }
}

fun <V : Any> ConfigContainer.stringKeyMap(
    key: String,
    defaultValue: Map<String, V>,
    valueSerializer: (V) -> SerializeElement,
    valueDeserializer: (SerializeElement) -> V
) = addConfig(ConfigStringKeyMap(key, defaultValue, valueSerializer, valueDeserializer))

class ConfigStringMap(
    key: String,
    defaultValue: Map<String, String>
) : ConfigStringKeyMap<String>(key, defaultValue, { SerializePrimitive(it) }, { it.asString })

fun ConfigContainer.stringMap(key: String, defaultValue: Map<String, String>) = addConfig(ConfigStringMap(key, defaultValue))

class ConfigStringByteMap(
    key: String,
    defaultValue: Map<String, Byte>
) : ConfigStringKeyMap<Byte>(key, defaultValue, { SerializePrimitive(it) }, { it.asByte })

fun ConfigContainer.stringByteMap(key: String, defaultValue: Map<String, Byte>) = addConfig(ConfigStringByteMap(key, defaultValue))

class ConfigStringShortMap(
    key: String,
    defaultValue: Map<String, Short>
) : ConfigStringKeyMap<Short>(key, defaultValue, { SerializePrimitive(it) }, { it.asShort })

fun ConfigContainer.stringShortMap(key: String, defaultValue: Map<String, Short>) = addConfig(ConfigStringShortMap(key, defaultValue))

class ConfigStringIntMap(
    key: String,
    defaultValue: Map<String, Int>
) : ConfigStringKeyMap<Int>(key, defaultValue, { SerializePrimitive(it) }, { it.asInt })

fun ConfigContainer.stringIntMap(key: String, defaultValue: Map<String, Int>) = addConfig(ConfigStringIntMap(key, defaultValue))

class ConfigStringLongMap(
    key: String,
    defaultValue: Map<String, Long>
) : ConfigStringKeyMap<Long>(key, defaultValue, { SerializePrimitive(it) }, { it.asLong })

fun ConfigContainer.stringLongMap(key: String, defaultValue: Map<String, Long>) = addConfig(ConfigStringLongMap(key, defaultValue))

class ConfigStringFloatMap(
    key: String,
    defaultValue: Map<String, Float>
) : ConfigStringKeyMap<Float>(key, defaultValue, { SerializePrimitive(it) }, { it.asFloat })

fun ConfigContainer.stringFloatMap(key: String, defaultValue: Map<String, Float>) = addConfig(ConfigStringFloatMap(key, defaultValue))

class ConfigStringDoubleMap(
    key: String,
    defaultValue: Map<String, Double>
) : ConfigStringKeyMap<Double>(key, defaultValue, { SerializePrimitive(it) }, { it.asDouble })

fun ConfigContainer.stringDoubleMap(key: String, defaultValue: Map<String, Double>) = addConfig(ConfigStringDoubleMap(key, defaultValue))
