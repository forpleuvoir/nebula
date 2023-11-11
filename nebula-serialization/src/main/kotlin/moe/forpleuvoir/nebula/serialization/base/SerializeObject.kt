@file:Suppress("UNUSED")

package moe.forpleuvoir.nebula.serialization.base

import java.math.BigDecimal
import java.math.BigInteger

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.serialization.base

 * 文件名 SerializeObject

 * 创建时间 2022/12/8 1:11

 * @author forpleuvoir

 */
class SerializeObject
internal constructor(private val members: LinkedHashMap<String, SerializeElement>) : SerializeElement, MutableMap<String, SerializeElement> by members {

    constructor() : this(members = LinkedHashMap())

    override val deepCopy: SerializeObject
        get() {
            val result = SerializeObject()
            for (entry in entries) {
                result[entry.key] = entry.value.deepCopy
            }
            return result
        }

    operator fun set(key: String, value: String): String? {
        return this.put(key, SerializePrimitive(value))?.asString
    }

    operator fun set(key: String, value: Char): Char? {
        return this.put(key, SerializePrimitive(value))?.asString?.get(0)
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

    fun containsKey(vararg keys: String): Boolean {
        if (keys.isEmpty()) return false
        for (key in keys) {
            if (!this.members.containsKey(key)) return false
        }
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SerializeObject

        return this.members == other.members
    }

    override fun hashCode(): Int {
        return members.hashCode()
    }

    override fun toString(): String {
        return members.toString()
    }


}