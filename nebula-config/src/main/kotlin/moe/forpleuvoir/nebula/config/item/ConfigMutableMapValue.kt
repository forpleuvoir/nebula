package moe.forpleuvoir.nebula.config.item

import moe.forpleuvoir.nebula.config.ConfigValue
import kotlin.collections.MutableMap.MutableEntry

interface ConfigMutableMapValue<K, V> : ConfigValue<MutableMap<K, V>>, MutableMap<K, V> {

	override val size: Int
		get() = getValue().size
	override val entries: MutableSet<MutableEntry<K, V>>
		get() = getValue().entries
	override val keys: MutableSet<K>
		get() = getValue().keys
	override val values: MutableCollection<V>
		get() = getValue().values

	override fun containsKey(key: K): Boolean {
		return getValue().containsKey(key)
	}

	override fun containsValue(value: V): Boolean {
		return getValue().containsValue(value)
	}

	override fun get(key: K): V? {
		return getValue()[key]
	}

	override fun isEmpty(): Boolean {
		return getValue().isEmpty()
	}

	override fun clear() {
		getValue().clear()
	}

	override fun put(key: K, value: V): V? {
		return getValue().put(key, value)
	}

	override fun putAll(from: Map<out K, V>) {
		getValue().putAll(from)
	}

	override fun remove(key: K): V? {
		return getValue().remove(key)
	}
}