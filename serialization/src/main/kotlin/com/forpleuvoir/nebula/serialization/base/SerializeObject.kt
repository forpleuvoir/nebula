package com.forpleuvoir.nebula.serialization.base

import com.google.gson.internal.LinkedTreeMap
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.collections.MutableMap.MutableEntry

/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.serialization.base

 * 文件名 SerializeObject

 * 创建时间 2022/12/8 1:11

 * @author forpleuvoir

 */
class SerializeObject : SerializeElement, MutableMap<String, SerializeElement> {

	private val members: LinkedTreeMap<String, SerializeElement> = LinkedTreeMap()

	override val deepCopy: SerializeElement
		get() {
			val result = SerializeObject()
			for (entry in entries) {
				result[entry.key] = entry.value.deepCopy
			}
			return result
		}
	override val entries: MutableSet<MutableEntry<String, SerializeElement>>
		get() = members.entries
	override val keys: MutableSet<String>
		get() = members.keys
	override val size: Int
		get() = members.size
	override val values: MutableCollection<SerializeElement>
		get() = members.values

	operator fun set(key: String, value: String): String? {
		return this.put(key, SerializePrimitive(value))?.asString
	}

	operator fun set(key: String, value: Boolean): Boolean? {
		return this.put(key, SerializePrimitive(value))?.asBoolean
	}

	operator fun set(key: String, value: Number): Number? {
		return this.put(key, SerializePrimitive(value))?.asNumber
	}

	operator fun set(key: String, value: BigInteger): BigInteger? {
		return this.put(key, SerializePrimitive(value))?.asBigInteger
	}

	operator fun set(key: String, value: BigDecimal): BigDecimal? {
		return this.put(key, SerializePrimitive(value))?.asBigDecimal
	}

	fun getAsPrimitive(key: String): SerializePrimitive? {
		return this[key]?.asPrimitive
	}

	fun getAsArray(key: String): SerializeArray? {
		return this[key]?.asArray
	}

	fun getAsObject(key: String): SerializeObject? {
		return this[key]?.asObject
	}

	override fun clear() {
		this.members.clear()
	}

	override fun isEmpty(): Boolean {
		return this.members.isEmpty()
	}

	override fun remove(key: String): SerializeElement? {
		return this.members.remove(key)
	}

	override fun putAll(from: Map<out String, SerializeElement>) {
		this.members.putAll(from)
	}

	override fun put(key: String, value: SerializeElement): SerializeElement? {
		return this.members.put(key, value)
	}

	override fun get(key: String): SerializeElement? {
		return this.members[key]
	}

	override fun containsValue(value: SerializeElement): Boolean {
		return this.members.containsValue(value)
	}

	override fun containsKey(key: String): Boolean {
		return this.members.containsKey(key)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as SerializeObject

		if (members != other.members) return false

		return true
	}

	override fun hashCode(): Int {
		return members.hashCode()
	}


}