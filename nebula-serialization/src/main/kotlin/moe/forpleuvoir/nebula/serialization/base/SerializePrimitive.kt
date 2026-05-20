@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate")

package moe.forpleuvoir.nebula.serialization.base

import moe.forpleuvoir.nebula.serialization.base.internal.LazilyParsedNumber
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.serialization.base

 * 文件名 SerializePrimitive

 * 创建时间 2022/12/8 1:07

 * @author forpleuvoir

 */
class SerializePrimitive private constructor(val value: Any) : SerializeElement {

    constructor(boolean: Boolean) : this(boolean as Any)

    constructor(string: String) : this(string as Any)

    constructor(number: Number) : this(number as Any)

    constructor(bigInteger: BigInteger) : this(bigInteger as Any)

    constructor(bigDecimal: BigDecimal) : this(bigDecimal as Any)

    constructor(char: Char) : this(char as Any)

    override fun deepCopy(): SerializePrimitive = SerializePrimitive(this.value)

    override fun copy(): SerializeElement = SerializePrimitive(this.value)

    val isString: Boolean get() = value is String

    val isBoolean: Boolean get() = value is Boolean

    val isNumber: Boolean get() = value is Number

    val isInt: Boolean get() = value is Int

    val isLong: Boolean get() = value is Long

    val isShort: Boolean get() = value is Short

    val isByte: Boolean get() = value is Byte

    val isFloat: Boolean get() = value is Float

    val isDouble: Boolean get() = value is Double

    val isBigInteger: Boolean get() = value is BigInteger

    val isBigDecimal: Boolean get() = value is BigDecimal

    val isChar: Boolean get() = value is Char

    val valueType: KClass<*> get() = value::class

    override val asChar: Char?
        get() = when (value) {
            is Char   -> value
            is String -> value.toCharArray().firstOrNull()
            else      -> value.toString().toCharArray().firstOrNull()
        }

    override val asString: String?
        get() = when (value) {
            is String -> value
            else      -> value.toString()
        }

    override val asBoolean: Boolean?
        get() = when (value) {
            is Boolean -> value
            else       -> value.toString().toBooleanStrictOrNull()
        }

    override val asNumber: Number?
        get() = when (value) {
            is Number -> value
            is String -> LazilyParsedNumber(value)
            else      -> null
        }

    override val asInt: Int?
        get() = when (value) {
            is Number -> value.toInt()
            else      -> value.toString().toIntOrNull()
        }

    override val asLong: Long?
        get() = when (value) {
            is Number -> value.toLong()
            else      -> value.toString().toLongOrNull()
        }

    override val asShort: Short?
        get() = when (value) {
            is Number -> value.toShort()
            else      -> value.toString().toShortOrNull()
        }

    override val asByte: Byte?
        get() = when (value) {
            is Number -> value.toByte()
            else      -> value.toString().toByteOrNull()
        }

    override val asFloat: Float?
        get() = when (value) {
            is Number -> value.toFloat()
            else      -> value.toString().toFloatOrNull()
        }

    override val asDouble: Double?
        get() = when (value) {
            is Number -> value.toDouble()
            else      -> value.toString().toDoubleOrNull()
        }

    override val asBigInteger: BigInteger?
        get() = when (value) {
            is BigInteger -> value
            else          -> runCatching { BigInteger(value.toString()) }.getOrNull()
        }

    override val asBigDecimal: BigDecimal?
        get() = when (value) {
            is BigDecimal -> value
            else          -> runCatching { BigDecimal(value.toString()) }.getOrNull()
        }

    override fun toString(): String = when (value) {
        is String -> "\"${escape(value)}\""
        is Char   -> "\'${escape(value.toString())}\'"
        else      -> value.toString()
    }

    private fun escape(s: String): String {
        val sb = StringBuilder()
        s.forEach { c ->
            when (c) {
                '"'      -> sb.append("\\\"")
                '\\'     -> sb.append("\\\\")
                '\b'     -> sb.append("\\b")
                '\u000C' -> sb.append("\\f")
                '\n'     -> sb.append("\\n")
                '\r'     -> sb.append("\\r")
                '\t'     -> sb.append("\\t")
                else     -> {
                    if (c.isISOControl()) {
                        // 处理控制字符，转为 \u00XX 格式
                        sb.append("\\u%04x".format(c.code))
                    } else {
                        sb.append(c)
                    }
                }
            }
        }
        return sb.toString()
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SerializePrimitive

        return value == other.value
    }

}