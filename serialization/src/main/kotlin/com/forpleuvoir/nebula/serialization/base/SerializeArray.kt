@file:Suppress("UNUSED")
package com.forpleuvoir.nebula.serialization.base

import java.math.BigDecimal
import java.math.BigInteger

/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.serialization.base

 * 文件名 SerializeArray

 * 创建时间 2022/12/8 1:12

 * @author forpleuvoir

 */
class SerializeArray private constructor(internal val elements: MutableList<SerializeElement>) : SerializeElement,
	MutableCollection<SerializeElement> {

	constructor(capacity: Int? = null) : this(if (capacity != null) ArrayList(capacity) else ArrayList())

	constructor(vararg elements: SerializeElement) : this(
		if (elements.isNotEmpty())
			ArrayList<SerializeElement>(elements.size).apply {
				addAll(elements)
			}
		else ArrayList())

	override val size: Int
		get() = this.elements.size
	override val deepCopy: SerializeArray
		get() {
			if (this.isNotEmpty()) {
				val result = SerializeArray(elements.size)
				for (element in this.elements) {
					result.add(element.deepCopy)
				}
				return result
			}
			return SerializeArray()
		}

	override val asPrimitive: SerializePrimitive
		get() {
			if (this.size == 1) {
				return this[0].asPrimitive
			}
			throw IllegalAccessException()
		}
	override val asObject: SerializeObject
		get() {
			if (this.size == 1) {
				return this[0].asObject
			}
			throw IllegalAccessException()
		}
	override val asArray: SerializeArray
		get() {
			if (this.size == 1) {
				return this[0].asArray
			}
			throw IllegalAccessException()
		}
	override val asNull: SerializeNull
		get() {
			if (this.size == 1) {
				return this[0].asNull
			}
			throw IllegalAccessException()
		}
	override val asString: String
		get() {
			if (this.size == 1) {
				return this[0].asString
			}
			throw IllegalAccessException()
		}
	override val asBoolean: Boolean
		get() {
			if (this.size == 1) {
				return this[0].asBoolean
			}
			throw IllegalAccessException()
		}
	override val asNumber: Number
		get() {
			if (this.size == 1) {
				return this[0].asNumber
			}
			throw IllegalAccessException()
		}
	override val asInt: Int
		get() {
			if (this.size == 1) {
				return this[0].asInt
			}
			throw IllegalAccessException()
		}
	override val asLong: Long
		get() {
			if (this.size == 1) {
				return this[0].asLong
			}
			throw IllegalAccessException()
		}
	override val asShort: Short
		get() {
			if (this.size == 1) {
				return this[0].asShort
			}
			throw IllegalAccessException()
		}
	override val asByte: Byte
		get() {
			if (this.size == 1) {
				return this[0].asByte
			}
			throw IllegalAccessException()
		}
	override val asFloat: Float
		get() {
			if (this.size == 1) {
				return this[0].asFloat
			}
			throw IllegalAccessException()
		}
	override val asDouble: Double
		get() {
			if (this.size == 1) {
				return this[0].asDouble
			}
			throw IllegalAccessException()
		}
	override val asBigInteger: BigInteger
		get() {
			if (this.size == 1) {
				return this[0].asBigInteger
			}
			throw IllegalAccessException()
		}
	override val asBigDecimal: BigDecimal
		get() {
			if (this.size == 1) {
				return this[0].asBigDecimal
			}
			throw IllegalAccessException()
		}

	operator fun get(index: Int): SerializeElement {
		return this.elements[index]
	}

	fun add(string: String): Boolean {
		return this.elements.add(SerializePrimitive(string))
	}

	fun add(char: Char): Boolean {
		return this.elements.add(SerializePrimitive(char.toString()))
	}

	fun add(boolean: Boolean): Boolean {
		return this.elements.add(SerializePrimitive(boolean))
	}

	fun add(number: Number): Boolean {
		return this.elements.add(SerializePrimitive(number))
	}

	fun add(bigInteger: BigInteger): Boolean {
		return this.elements.add(SerializePrimitive(bigInteger))
	}

	fun add(bigDecimal: BigDecimal): Boolean {
		return this.elements.add(SerializePrimitive(bigDecimal))
	}

	override fun add(element: SerializeElement): Boolean {
		return this.elements.add(element)
	}

	override fun addAll(elements: Collection<SerializeElement>): Boolean {
		return this.elements.addAll(elements)
	}

	fun addAll(array: SerializeArray): Boolean {
		return this.elements.addAll(array.elements)
	}

	operator fun set(index: Int, element: SerializeElement): SerializeElement {
		return this.elements.set(index, element)
	}

	fun remove(index: Int): SerializeElement {
		return this.elements.removeAt(index)
	}

	override fun remove(element: SerializeElement): Boolean {
		return this.elements.remove(element)
	}

	override fun clear() {
		this.elements.clear()
	}

	override fun isEmpty(): Boolean {
		return this.elements.isEmpty()
	}

	override fun containsAll(elements: Collection<SerializeElement>): Boolean {
		return this.elements.containsAll(elements)
	}

	override fun contains(element: SerializeElement): Boolean {
		return this.elements.contains(element)
	}

	override fun iterator(): MutableIterator<SerializeElement> = elements.iterator()
	override fun retainAll(elements: Collection<SerializeElement>): Boolean {
		return this.elements.retainAll(elements)
	}

	override fun removeAll(elements: Collection<SerializeElement>): Boolean {
		return this.elements.removeAll(elements)
	}



	override fun hashCode(): Int {
		return elements.hashCode()
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as SerializeArray

		if (elements != other.elements) return false

		return true
	}

	override fun toString(): String {
		return elements.toString()
	}
}