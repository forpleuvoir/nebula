@file:Suppress("UNUSED")
@file:OptIn(ExperimentalContracts::class)

package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.base.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

fun Map<*, *>.toSerializeObject(): SerializeObject {
    return serializeObject(this.mapKeys { it.key.toString() })
}

fun SerializeObject.putAny(key: String, value: Any?) {
    this[key] = value.toSerializeElement()
}

fun SerializeObject.toJavaMap(): Map<String, Any?> {
    return LinkedHashMap<String, Any?>().apply {
        for (entry in this@toJavaMap) {
            when (val value = entry.value) {
                is SerializePrimitive -> this[entry.key] = value.toJavaPrimitive()
                is SerializeArray     -> this[entry.key] = value.toJavaList()
                is SerializeObject    -> this[entry.key] = value.toJavaMap()
                is SerializeNull      -> this[entry.key] = null
            }
        }
    }
}

class SerializeObjectBuilder {

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
        obj.putAny(key, value)
    }

    infix fun String.to(value: Any?) {
        obj.putAny(this, value)
    }

    operator fun String.invoke(scope: SerializeObjectBuilder.() -> Unit) {
        obj[this] = SerializeObjectBuilder().apply(scope).obj
    }

}

fun serializeObject(scope: SerializeObjectBuilder.() -> Unit): SerializeObject {
    return SerializeObjectBuilder().apply(scope).obj
}

fun serializeObject(map: Map<String, Any?>): SerializeObject {
    return serializeObject(map.entries)
}

@JvmName("serializeObjectByPair")
fun <T> serializeObject(pairs: Iterable<Pair<T, *>>, converter: (T) -> String): SerializeObject {
    return serializeObject {
        for ((k, v) in pairs) {
            this[converter(k)] = v
        }
    }
}

@JvmName("serializeObjectByEntry")
fun <T> serializeObject(pairs: Iterable<Map.Entry<T, *>>, converter: (T) -> String): SerializeObject {
    return serializeObject {
        for ((k, v) in pairs) {
            this[converter(k)] = v
        }
    }
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

fun <T> SerializeObject.getOr(key: String, or: T, mapping: (SerializeElement) -> T): T {
    return runCatching { mapping(this[key]!!) }.getOrDefault(or)
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

