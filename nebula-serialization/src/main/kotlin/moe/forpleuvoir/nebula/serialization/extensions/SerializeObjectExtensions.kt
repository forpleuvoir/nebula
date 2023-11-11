@file:Suppress("UNUSED")
@file:OptIn(ExperimentalContracts::class)

package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.Serializable
import moe.forpleuvoir.nebula.serialization.base.*
import java.lang.reflect.Modifier
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility.INTERNAL
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
        memberProperties.filter {
            it.visibility != INTERNAL && it.findAnnotation<Transient>() == null && it.javaField?.let { field -> !Modifier.isTransient(field.modifiers) } ?: true
        }.forEach { property ->
            property as KProperty1<Any, Any?>
            property.isAccessible = true
            //不能是委托属性
            if (property.getDelegate(this@toSerializeObject) == null) {
                val value = property.getter.call(this@toSerializeObject)
                val name = property.name
                obj.putAny(name, value)
            }
        }

    }
    return obj
}

fun Map<*, *>.toSerializeObject(): SerializeObject {
    return serializeObject(this.mapKeys { it.toString() })

}

fun SerializeObject.putAny(key: String, value: Any?) {
    this[key] = value.toSerializeElement()
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
        return this.toSerializeObject()
    }

}

class SerializeObjectScope {

    internal val obj: SerializeObject = SerializeObject()


    fun scope(scope: SerializeObject.() -> Unit) {
        obj.apply(scope)
    }

    fun put(entry: Map.Entry<String, Any?>) {
        obj.putAny(entry.key, entry.value)
    }

    fun put(entry: Pair<String, Any?>) {
        obj.putAny(entry.first, entry.second)
    }

    infix operator fun String.minus(value: Any?) {
        obj.putAny(this, value)
    }

    operator fun set(key: String, value: Any?) {
        obj["a"] = SerializeObject()
        obj.putAny(key, value)
    }

    infix fun String.to(value: Any?) {
        obj.putAny(this, value)
    }

}

fun serializeObject(scope: SerializeObjectScope.() -> Unit): SerializeObject {
    return SerializeObjectScope().apply(scope).obj
}

fun serializeObject(map: Map<String, Any?>): SerializeObject {
    return serializeObject(map.entries)
}

fun serializeObject(entries: Iterable<Map.Entry<String, *>>): SerializeObject {
    return serializeObject {
        for (entry in entries) {
            put(entry)
        }
    }
}

fun <T> serializeObject(map: Map<String, T>, converter: (T) -> SerializeElement): SerializeObject {
    contract {
        callsInPlace(converter, InvocationKind.UNKNOWN)
    }
    return serializeObject {
        for (entry in map) {
            entry.key - converter(entry.value)
        }
    }
}

fun <T> serializeObject(entries: Set<Map.Entry<String, T>>, entryConverter: (String, T) -> Pair<String, SerializeElement>): SerializeObject {
    contract {
        callsInPlace(entryConverter, InvocationKind.UNKNOWN)
    }
    return serializeObject {
        for (entry in entries) {
            put(entryConverter(entry.key, entry.value))
        }
    }
}


fun <T> SerializeObject.getOr(key: String, or: T, converter: (SerializeElement) -> T): T {
    return runCatching { converter(this[key]!!) }.getOrDefault(or)
}

fun SerializeObject.getOr(key: String, or: Number): Number {
    return runCatching { this[key]!!.asNumber }.getOrDefault(or)
}

fun SerializeObject.getOr(key: String, or: Boolean): Boolean {
    return runCatching { this[key]!!.asBoolean }.getOrDefault(or)
}

fun SerializeObject.getOr(key: String, or: String): String {
    return runCatching { this[key]!!.asString }.getOrDefault(or)
}

fun SerializeObject.getOr(key: String, or: Char): Char {
    return runCatching { this[key]!!.asString[0] }.getOrDefault(or)
}

fun SerializeObject.getOr(key: String, or: SerializeObject): SerializeObject {
    return runCatching { this[key]!!.asObject }.getOrDefault(or)
}

fun SerializeObject.getOr(key: String, or: SerializeArray): SerializeArray {
    return runCatching { this[key]!!.asArray }.getOrDefault(or)
}

