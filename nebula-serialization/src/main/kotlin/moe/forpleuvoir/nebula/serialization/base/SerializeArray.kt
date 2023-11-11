@file:Suppress("UNUSED")

package moe.forpleuvoir.nebula.serialization.base

import java.math.BigDecimal
import java.math.BigInteger

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.serialization.base

 * 文件名 SerializeArray

 * 创建时间 2022/12/8 1:12

 * @author forpleuvoir

 */
class SerializeArray private constructor(private val elements: MutableList<SerializeElement>) : SerializeElement,
    MutableCollection<SerializeElement> by elements {

    constructor(capacity: Int? = null) : this(if (capacity != null) ArrayList(capacity) else ArrayList())

    constructor(vararg elements: SerializeElement) : this(
        if (elements.isNotEmpty())
            ArrayList<SerializeElement>(elements.size).apply {
                addAll(elements)
            }
        else ArrayList())

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
                return this.elements[0].asPrimitive
            }
            throw IllegalAccessException()
        }
    override val asObject: SerializeObject
        get() {
            if (this.size == 1) {
                return this.elements[0].asObject
            }
            throw IllegalAccessException()
        }

    override val asNull: SerializeNull
        get() {
            if (this.size == 1) {
                return this.elements[0].asNull
            }
            throw IllegalAccessException()
        }
    override val asString: String
        get() {
            if (this.size == 1) {
                return this.elements[0].asString
            }
            throw IllegalAccessException()
        }
    override val asBoolean: Boolean
        get() {
            if (this.size == 1) {
                return this.elements[0].asBoolean
            }
            throw IllegalAccessException()
        }
    override val asNumber: Number
        get() {
            if (this.size == 1) {
                return this.elements[0].asNumber
            }
            throw IllegalAccessException()
        }
    override val asInt: Int
        get() {
            if (this.size == 1) {
                return this.elements[0].asInt
            }
            throw IllegalAccessException()
        }
    override val asLong: Long
        get() {
            if (this.size == 1) {
                return this.elements[0].asLong
            }
            throw IllegalAccessException()
        }
    override val asShort: Short
        get() {
            if (this.size == 1) {
                return this.elements[0].asShort
            }
            throw IllegalAccessException()
        }
    override val asByte: Byte
        get() {
            if (this.size == 1) {
                return this.elements[0].asByte
            }
            throw IllegalAccessException()
        }
    override val asFloat: Float
        get() {
            if (this.size == 1) {
                return this.elements[0].asFloat
            }
            throw IllegalAccessException()
        }
    override val asDouble: Double
        get() {
            if (this.size == 1) {
                return this.elements[0].asDouble
            }
            throw IllegalAccessException()
        }
    override val asBigInteger: BigInteger
        get() {
            if (this.size == 1) {
                return this.elements[0].asBigInteger
            }
            throw IllegalAccessException()
        }
    override val asBigDecimal: BigDecimal
        get() {
            if (this.size == 1) {
                return this.elements[0].asBigDecimal
            }
            throw IllegalAccessException()
        }

    operator fun get(index: Int): SerializeElement {
        return this.elements[index]
    }

    fun add(string: String): Boolean {
        return this.add(SerializePrimitive(string))
    }

    fun add(char: Char): Boolean {
        return this.add(SerializePrimitive(char.toString()))
    }

    fun add(boolean: Boolean): Boolean {
        return this.add(SerializePrimitive(boolean))
    }

    fun add(number: Number): Boolean {
        return this.add(SerializePrimitive(number))
    }

    fun add(bigInteger: BigInteger): Boolean {
        return this.add(SerializePrimitive(bigInteger))
    }

    fun add(bigDecimal: BigDecimal): Boolean {
        return this.add(SerializePrimitive(bigDecimal))
    }

    fun addAll(array: SerializeArray): Boolean {
        return this.addAll(array.elements)
    }

    override fun remove(element: SerializeElement): Boolean {
        return this.remove(element)
    }

    override fun hashCode(): Int {
        return elements.hashCode()
    }

    override fun toString(): String {
        return elements.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SerializeArray

        return elements == other.elements
    }


}