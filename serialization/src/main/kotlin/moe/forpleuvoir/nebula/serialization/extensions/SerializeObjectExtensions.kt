@file:Suppress("UNUSED")

package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.Serializable
import moe.forpleuvoir.nebula.serialization.base.*
import java.lang.reflect.Array
import java.lang.reflect.Modifier
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility.INTERNAL
import kotlin.reflect.KVisibility.PUBLIC
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.serialization.extensions

 * 文件名 SerializeObjectExtensions

 * 创建时间 2022/12/8 23:47

 * @author forpleuvoir

 */

@Suppress("UNCHECKED_CAST")
fun Any.toSerializeObject(): SerializeObject {
    if (this is Serializable) {
        return this.serialization().asObject
    }
    val obj = SerializeObject()
    this::class.apply {
        memberProperties
            .filter {
                it.visibility != INTERNAL
                && it.findAnnotation<Transient>() == null
                && it.javaField?.let { field -> !Modifier.isTransient(field.modifiers) } ?: true
            }
            .forEach { property ->
                property as KProperty1<Any, Any?>
                property.isAccessible = true
                //不能是委托属性
                if (property.getDelegate(this@toSerializeObject) == null) {
                    val value = property.getter.call(this@toSerializeObject)
                    val name = property.name
                    when (value) {
                        null               -> obj[name] = SerializeNull
                        is String          -> obj[name] = value
                        is Char            -> obj[name] = value
                        is Boolean         -> obj[name] = value
                        is BigInteger      -> obj[name] = value
                        is BigDecimal      -> obj[name] = value
                        is Number          -> obj[name] = value
                        is kotlin.Array<*> -> obj[name] = serializeArray(value.iterator())
                        is Collection<*>   -> obj[name] = serializeArray(value)
                        else               -> obj[name] = value.toSerializeObject()
                    }
                }
            }

    }
    return obj
}

fun SerializeObject.toMap(): Map<String, Any?> {
    return buildMap {
        for (entry in this@toMap) {
            when (val value = entry.value) {
                is SerializePrimitive -> this[entry.key] = value.toObj()
                is SerializeArray     -> this[entry.key] = value.toList()
                is SerializeObject    -> this[entry.key] = value.toMap()
                is SerializeNull      -> this[entry.key] = null
            }
        }
    }
}

interface SObj {
    fun serialize(): SerializeObject {
        val clazz = this::class
        return SerializeObject().apply {
            clazz.memberProperties.forEach {
                if (it.visibility == PUBLIC) {
                    val key = it.name
                    when (val value = it.call(this@SObj)) {
                        is Boolean          -> this[key] = value
                        is Number           -> this[key] = value
                        is String           -> this[key] = value
                        is Char             -> this[key] = value
                        is SerializeElement -> this[key] = value
                        else                -> if (value != null) this[key] = value.toSerializeObject() else this[key] = SerializeNull
                    }
                }
            }
        }
    }

}

class SerializeObjectScope {

    internal val obj: SerializeObject = SerializeObject()

    infix operator fun String.minus(value: String) {
        obj[this] = value
    }

    infix operator fun String.minus(value: Number) {
        obj[this] = value
    }

    infix operator fun String.minus(value: Boolean) {
        obj[this] = value
    }

    infix operator fun String.minus(value: Char) {
        obj[this] = value
    }

    infix operator fun String.minus(value: SerializeElement) {
        obj[this] = value
    }
}

fun serializeObject(scope: SerializeObjectScope.() -> Unit): SerializeObject {
    val objectScope = SerializeObjectScope()
    objectScope.scope()
    return objectScope.obj
}


fun serializeObject(map: Map<String, Any>): SerializeObject {
    return serializeObject(map.entries)
}

@Suppress("UNCHECKED_CAST")
fun serializeObject(entries: Iterable<Map.Entry<String, Any>>): SerializeObject {
    return serializeObject {
        for (entry in entries) {
            when (val element = entry.value) {
                is Boolean          -> entry.key - element
                is Number           -> entry.key - element
                is String           -> entry.key - element
                is Char             -> entry.key - element
                is SerializeElement -> entry.key - element
                is Map<*, *>        -> entry.key - serializeObject(element.mapKeys { it.key.toString() }.mapValues { it.value!! })
                is Iterable<*>      -> entry.key - serializeArray(element as List<Any>)
                else                -> entry.key - element.toSerializeObject()
            }
        }
    }
}

fun <T> serializeObject(map: Map<String, T>, converter: (T) -> SerializeElement): SerializeObject {
    return serializeObject {
        for (entry in map) {
            entry.key - converter(entry.value)
        }
    }
}

fun <T> SerializeObject.getOr(key: String, or: T, converter: (SerializeElement) -> T): T {
    return try {
        converter(this[key]!!)
    } catch (_: Exception) {
        or
    }
}

fun SerializeObject.getOr(key: String, or: Number): Number {
    return try {
        this[key]!!.asNumber
    } catch (_: Exception) {
        or
    }
}

fun SerializeObject.getOr(key: String, or: Boolean): Boolean {
    return try {
        this[key]!!.asBoolean
    } catch (_: Exception) {
        or
    }
}

fun SerializeObject.getOr(key: String, or: String): String {
    return try {
        this[key]!!.asString
    } catch (_: Exception) {
        or
    }
}

fun SerializeObject.getOr(key: String, or: Char): Char {
    return try {
        this[key]!!.asString[0]
    } catch (_: Exception) {
        or
    }
}

fun SerializeObject.getOr(key: String, or: SerializeObject): SerializeObject {
    return try {
        this[key]!!.asObject
    } catch (_: Exception) {
        or
    }
}

fun SerializeObject.getOr(key: String, or: SerializeArray): SerializeArray {
    return try {
        this[key]!!.asArray
    } catch (_: Exception) {
        or
    }
}

