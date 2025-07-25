@file:Suppress("UNUSED", "OVERRIDE_DEPRECATION")

package moe.forpleuvoir.nebula.serialization.base

import java.math.BigDecimal
import java.math.BigInteger
import java.util.function.IntFunction

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.serialization.base

 * 文件名 SerializeArray

 * 创建时间 2022/12/8 1:12

 * @author forpleuvoir

 */
class SerializeArray private constructor(private val elements: MutableList<SerializeElement>) : SerializeElement,
    MutableList<SerializeElement> by elements {

    constructor(capacity: Int? = null) : this(if (capacity != null) ArrayList(capacity) else ArrayList())

    constructor(vararg elements: SerializeElement) : this(
        if (elements.isNotEmpty())
            ArrayList<SerializeElement>(elements.size).apply {
                addAll(elements)
            }
        else ArrayList())

    override fun deepCopy(): SerializeArray {
        if (this.isNotEmpty()) {
            val result = SerializeArray(elements.size)
            for (element in this.elements) {
                result.add(element.deepCopy())
            }
            return result
        }
        return SerializeArray()
    }

    override fun copy(): SerializeArray {
        if (this.isNotEmpty()) {
            return SerializeArray(elements)
        }
        return SerializeArray()
    }

    override val asPrimitive: SerializePrimitive
        get() {
            if (this.size == 1) {
                return this.elements[0].asPrimitive
            }
            return super.asPrimitive
        }
    override val asObject: SerializeObject
        get() {
            if (this.size == 1) {
                return this.elements[0].asObject
            }
            return super.asObject
        }

    override val asNull: SerializeNull
        get() {
            if (this.size == 1) {
                return this.elements[0].asNull
            }
            return super.asNull
        }
    override val asString: String
        get() {
            if (this.size == 1) {
                return this.elements[0].asString
            }
            return super.asString
        }
    override val asBoolean: Boolean
        get() {
            if (this.size == 1) {
                return this.elements[0].asBoolean
            }
            return super.asBoolean
        }
    override val asNumber: Number
        get() {
            if (this.size == 1) {
                return this.elements[0].asNumber
            }
            return super.asNumber
        }
    override val asInt: Int
        get() {
            if (this.size == 1) {
                return this.elements[0].asInt
            }
            return super.asInt
        }
    override val asLong: Long
        get() {
            if (this.size == 1) {
                return this.elements[0].asLong
            }
            return super.asLong
        }
    override val asShort: Short
        get() {
            if (this.size == 1) {
                return this.elements[0].asShort
            }
            return super.asShort
        }
    override val asByte: Byte
        get() {
            if (this.size == 1) {
                return this.elements[0].asByte
            }
            return super.asByte
        }
    override val asFloat: Float
        get() {
            if (this.size == 1) {
                return this.elements[0].asFloat
            }
            return super.asFloat
        }
    override val asDouble: Double
        get() {
            if (this.size == 1) {
                return this.elements[0].asDouble
            }
            return super.asDouble
        }
    override val asBigInteger: BigInteger
        get() {
            if (this.size == 1) {
                return this.elements[0].asBigInteger
            }
            return super.asBigInteger
        }
    override val asBigDecimal: BigDecimal
        get() {
            if (this.size == 1) {
                return this.elements[0].asBigDecimal
            }
            return super.asBigDecimal
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


    override fun <T : Any?> toArray(generator: IntFunction<Array<out T?>?>): Array<out T?>? {
        @Suppress("DEPRECATION")
        return super.toArray(generator)
    }

}