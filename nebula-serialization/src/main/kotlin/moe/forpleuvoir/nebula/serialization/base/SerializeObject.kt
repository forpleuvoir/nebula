@file:Suppress("UNUSED")

package moe.forpleuvoir.nebula.serialization.base

import moe.forpleuvoir.nebula.common.color.ARGBColor
import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.common.color.HSVColor
import moe.forpleuvoir.nebula.common.color.RGBColor
import moe.forpleuvoir.nebula.common.util.SerializableDuration
import moe.forpleuvoir.nebula.serialization.extensions.serialization
import moe.forpleuvoir.nebula.serialization.extensions.serializationAsObject
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass

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

    companion object {

        internal val serializerCache: MutableMap<KClass<out Any>, (Any) -> SerializeObject> = mutableMapOf()

        init {
            register<Color>(Color::serializationAsObject)
            register<HSVColor>(HSVColor::serializationAsObject)
            register<RGBColor>(RGBColor::serializationAsObject)
            register<ARGBColor>(ARGBColor::serializationAsObject)
            register<SerializableDuration>(SerializableDuration::serialization)
        }

        @Suppress("UNCHECKED_CAST")
        private inline fun <reified T : Any> register(noinline func: (T) -> SerializeObject) {
            serializerCache[T::class] = func as (Any) -> SerializeObject
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : Any> registerSerializer(type: KClass<T>, func: (T) -> SerializeObject) {
            serializerCache[type] = func as (Any) -> SerializeObject
        }

        inline fun <reified T : Any> registerSerializer(noinline func: (T) -> SerializeObject) {
            registerSerializer(T::class, func)
        }

    }

    override fun deepCopy(): SerializeObject {
        if (this.isEmpty()) return SerializeObject()
        return SerializeObject().apply {
            for (entry in this@SerializeObject) {
                this[entry.key] = entry.value.deepCopy()
            }
        }
    }

    override fun copy(): SerializeElement {
        return SerializeObject(this.members)
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