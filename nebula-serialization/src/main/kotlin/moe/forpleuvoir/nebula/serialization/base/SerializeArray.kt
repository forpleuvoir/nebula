@file:Suppress("UNUSED", "OVERRIDE_DEPRECATION")

package moe.forpleuvoir.nebula.serialization.base

import moe.forpleuvoir.nebula.serialization.Serializable
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

    companion object {

        operator fun of(vararg elements: Serializable): SerializeArray {
            return SerializeArray(elements.map { it.serialization() }.toMutableList())
        }

    }

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

    override val asPrimitive: SerializePrimitive? get() = if (size == 1) elements[0].asPrimitive else null
    override val asObject: SerializeObject? get() = if (size == 1) elements[0].asObject else null
    override val asNull: SerializeNull? get() = if (size == 1) elements[0].asNull else null
    override val asChar: Char? get() = if (size == 1) elements[0].asChar else null
    override val asString: String? get() = if (size == 1) elements[0].asString else null
    override val asBoolean: Boolean? get() = if (size == 1) elements[0].asBoolean else null
    override val asNumber: Number? get() = if (size == 1) elements[0].asNumber else null
    override val asInt: Int? get() = if (size == 1) elements[0].asInt else null
    override val asLong: Long? get() = if (size == 1) elements[0].asLong else null
    override val asShort: Short? get() = if (size == 1) elements[0].asShort else null
    override val asByte: Byte? get() = if (size == 1) elements[0].asByte else null
    override val asFloat: Float? get() = if (size == 1) elements[0].asFloat else null
    override val asDouble: Double? get() = if (size == 1) elements[0].asDouble else null
    override val asBigInteger: BigInteger? get() = if (size == 1) elements[0].asBigInteger else null
    override val asBigDecimal: BigDecimal? get() = if (size == 1) elements[0].asBigDecimal else null

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

    fun add(value: Serializable): Boolean {
        return this.add(value.serialization())
    }

    fun addAll(array: SerializeArray): Boolean {
        return this.addAll(array.elements)
    }

    fun addAll(elements: Iterable<Serializable>): Boolean {
        return this.addAll(elements.map { it.serialization() })
    }

    override fun hashCode(): Int {
        return elements.hashCode()
    }

    override fun toString(): String {
        return elements.joinToString(", ", "[", "]")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SerializeArray

        return elements == other.elements
    }


    override fun <T> toArray(generator: IntFunction<Array<out T?>?>): Array<out T?>? {
        @Suppress("DEPRECATION")
        return super.toArray(generator)
    }

}