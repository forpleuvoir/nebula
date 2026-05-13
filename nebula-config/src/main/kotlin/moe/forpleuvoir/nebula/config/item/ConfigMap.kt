@file:Suppress("unused")

package moe.forpleuvoir.nebula.config.item

import kotlinx.serialization.KSerializer
import moe.forpleuvoir.nebula.config.ConfigGroup
import moe.forpleuvoir.nebula.config.ConfigItem
import moe.forpleuvoir.nebula.config.ConfigSerde
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.builder.build
import moe.forpleuvoir.nebula.serialization.codec.Codec
import moe.forpleuvoir.nebula.serialization.extensions.checkType

class ConfigMap<V>(
    name: String,
    defaultValue: Map<String, V>,
    private val serde: ConfigSerde<V>,
) : ConfigItem<Map<String, V>>(name, defaultValue), MutableMap<String, V> {

    private val map: MutableMap<String, V> = LinkedHashMap(defaultValue)

    override fun getValue(): Map<String, V> = map.toMap()

    override fun setValue(value: Map<String, V>) {
        if (map valueNotEquals value) {
            map.clear()
            map.putAll(value)
            notifyChange()
        }
    }

    override fun isDefault(): Boolean = map.toMap() valueEquals defaultValue

    override fun restDefault() {
        if (isDefault()) return
        map.clear()
        map.putAll(defaultValue)
        notifyChange()
    }

    override fun serialization(): SerializeElement = SerializeObject.build {
        map.forEach { (k, v) -> obj[k] = serde.encode(v) }
    }

    override fun deserialization(serializeElement: SerializeElement) {
        serializeElement.checkType<SerializeObject, Unit> { obj ->
            val m = LinkedHashMap<String, V>()
            obj.forEach { (k, v) ->
                serde.decode(v).onSuccess { m[k] = it }
            }
            setValue(m)
        }
    }

    override val size: Int get() = map.size
    override val entries: MutableSet<MutableMap.MutableEntry<String, V>> get() = map.entries
    override val keys: MutableSet<String> get() = map.keys
    override val values: MutableCollection<V> get() = map.values
    override fun isEmpty(): Boolean = map.isEmpty()
    override fun containsKey(key: String): Boolean = map.containsKey(key)
    override fun containsValue(value: V): Boolean = map.containsValue(value)
    override fun get(key: String): V? = map[key]
    override fun put(key: String, value: V): V? = map.put(key, value).also { notifyChange() }
    override fun putAll(from: Map<out String, V>) {
        map.putAll(from); notifyChange()
    }

    override fun clear() {
        map.clear(); notifyChange()
    }

    override fun remove(key: String): V? = map.remove(key).also { if (it != null) notifyChange() }
}

context(group: ConfigGroup)
fun <V> configMap(name: String, defaultValue: Map<String, V>, serde: ConfigSerde<V>) =
    group.addConfig(ConfigMap(name, defaultValue, serde))


context(group: ConfigGroup)
fun <V> configMap(name: String, defaultValue: Map<String, V>, codec: Codec<V>) =
    configMap(name, defaultValue, ConfigSerde.of(codec))

context(group: ConfigGroup)
fun <V> configMap(name: String, defaultValue: Map<String, V>, serializer: KSerializer<V>) =
    configMap(name, defaultValue, ConfigSerde.of(serializer))
