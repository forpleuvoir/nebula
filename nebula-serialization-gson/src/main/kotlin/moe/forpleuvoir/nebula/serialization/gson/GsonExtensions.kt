@file:Suppress("UNUSED")
@file:OptIn(ExperimentalContracts::class)

package moe.forpleuvoir.nebula.serialization.gson

import com.google.gson.*
import moe.forpleuvoir.nebula.serialization.base.*
import moe.forpleuvoir.nebula.serialization.extensions.serializeObject
import moe.forpleuvoir.nebula.serialization.extensions.toMap
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KVisibility.PUBLIC
import kotlin.reflect.full.memberProperties

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.serialization.json

 * 文件名 JsonExtensions

 * 创建时间 2022/12/9 1:04

 * @author forpleuvoir

 */
val gson: Gson by lazy { GsonBuilder().setPrettyPrinting().create() }

fun JsonObject.toSerializeObject(): SerializeObject {
    return serializeObject(this.entrySet()) { key, value ->
        key to value.toSerializeElement()
    }
}

fun SerializeObject.toJsonObject(): JsonObject {
    return JsonObject().apply {
        for ((key, value) in this@toJsonObject) {
            add(key, value.toJsonElement())
        }
    }
}

fun JsonElement.toSerializeElement(): SerializeElement {
    return when {
        isJsonPrimitive -> this.asJsonPrimitive.toSerializePrimitive()
        isJsonArray     -> this.asJsonArray.toArray()
        isJsonObject    -> this.asJsonObject.toSerializeObject()
        else            -> SerializeNull
    }
}

fun SerializeElement.toJsonElement(): JsonElement {
    return when (this) {
        is SerializeArray     -> TODO()
        is SerializeObject    -> TODO()
        is SerializePrimitive -> this.toJsonPrimitive()
        SerializeNull         -> JsonNull.INSTANCE
    }
}

fun JsonPrimitive.toSerializePrimitive(): SerializePrimitive {
    return when {
        isBoolean -> SerializePrimitive(this.asBoolean)
        isNumber  -> SerializePrimitive(this.asNumber)
        isString  -> SerializePrimitive(this.asString)
        else      -> SerializePrimitive(this.toString())
    }
}

fun SerializePrimitive.toJsonPrimitive(): JsonPrimitive {
    return when {
        this.isBoolean -> JsonPrimitive(this.asBoolean)
        this.isNumber  -> JsonPrimitive(this.asNumber)
        this.isString  -> JsonPrimitive(this.asString)
        else           -> JsonPrimitive(this.asString)
    }
}

fun JsonArray.toArray(): SerializeArray {
    val result = SerializeArray(this.size())
    for (element in this) {
        result.add(element.toSerializeElement())
    }
    return result
}

fun SerializeArray.toJsonArray(): JsonArray {
    return JsonArray().apply {
        for (element in this@toJsonArray) {
            add(element.toJsonElement())
        }
    }
}


val String.parseToJsonArray: JsonArray get() = JsonParser.parseString(this).asJsonArray

val String.parseToJsonObject: JsonObject get() = JsonParser.parseString(this).asJsonObject

val String.parseToJsonElement: JsonElement get() = JsonParser.parseString(this)

val JsonElement.string: String get() = this.toString()

fun Any.toJsonObject(): JsonObject {
    return gson.toJsonTree(this).asJsonObject
}

fun JsonObject.getNestedObject(key: String, create: Boolean = false): JsonObject? {
    return if (!this.has(key) || this[key].isJsonObject) {
        if (!create) {
            return null
        }
        val obj = JsonObject()
        this.add(key, obj)
        obj
    } else {
        this[key].asJsonObject
    }
}

/**
 * 将对象转换成json字符串
 *
 * @return json字符串
 */
fun Any.toJsonStr(): String {
    return gson.toJson(this)
}

fun SerializeElement.toJsonString(): String {
    return this.toJsonElement().toString()
}

fun SerializeObject.toJsonString(): String {
    return gson.toJson(this.toMap())
}

fun String.jsonStringToObject(): SerializeObject {
    return parseToJsonObject.toSerializeObject()
}

fun jsonArray(vararg elements: Any): JsonArray {
    return jsonArray(elements.toList())
}

inline fun <T> jsonArray(iterable: Iterable<T>, converter: (T) -> JsonElement): JsonArray {
    return JsonArray().apply {
        for (t in iterable) {
            add(converter(t))
        }
    }
}

fun jsonArray(elements: Iterable<Any>): JsonArray {
    return JsonArray().apply {
        for (element in elements) {
            when (element) {
                is Boolean     -> add(element)
                is Number      -> add(element)
                is String      -> add(element)
                is Char        -> add(element)
                is JsonElement -> add(element)
                else           -> add(element.toJsonObject())
            }
        }
    }
}

interface Json {
    fun serialize(): JsonObject {
        val clazz = this::class
        return JsonObject().apply {
            clazz.memberProperties.forEach {
                if (it.visibility == PUBLIC) {
                    val key = it.name
                    when (val value = it.call(this@Json)) {
                        is Boolean     -> this.addProperty(key, value)
                        is Number      -> this.addProperty(key, value)
                        is String      -> this.addProperty(key, value)
                        is Char        -> this.addProperty(key, value)
                        is JsonElement -> this.add(key, value)
                        else           -> if (value != null) this.add(key, value.toJsonObject()) else this.add(
                            key,
                            JsonNull.INSTANCE
                        )
                    }
                }
            }
        }
    }

}

class JsonObjectScope {

    internal val jsonObject: JsonObject = JsonObject()

    fun scope(scope: JsonObject.() -> Unit) {
        jsonObject.apply(scope)
    }

    infix operator fun String.minus(value: String) {
        jsonObject.addProperty(this, value)
    }

    infix operator fun String.minus(value: Number) {
        jsonObject.addProperty(this, value)
    }

    infix operator fun String.minus(value: Boolean) {
        jsonObject.addProperty(this, value)
    }

    infix operator fun String.minus(value: Char) {
        jsonObject.addProperty(this, value)
    }

    infix operator fun String.minus(value: JsonElement) {
        jsonObject.add(this, value)
    }
}

fun jsonObject(scope: JsonObjectScope.() -> Unit): JsonObject {
    return JsonObjectScope().apply(scope).jsonObject
}

fun jsonObject(map: Map<String, Any>): JsonObject {
    return jsonObject {
        map.forEach { (key, element) ->
            when (element) {
                is Boolean     -> key - element
                is Number      -> key - element
                is String      -> key - element
                is Char        -> key - element
                is JsonElement -> key - element
                else           -> key - element.toJsonObject()
            }
        }
    }
}

fun <T> jsonObject(map: Map<String, T>, converter: (T) -> JsonElement): JsonObject {
    contract {
        callsInPlace(converter, InvocationKind.UNKNOWN)
    }
    return jsonObject {
        for (entry in map) {
            entry.key - converter(entry.value)
        }
    }
}

fun <T> JsonObject.getOr(key: String, or: T, converter: (JsonElement) -> T): T {
    contract {
        callsInPlace(converter, InvocationKind.AT_MOST_ONCE)
    }
    return runCatching { converter(this[key]!!) }.getOrDefault(or)
}


inline fun <reified T> JsonObject.getOr(key: String, or: T): T {
    return runCatching { gson.fromJson(this.get(key), T::class.java) }.getOrDefault(or)
}

fun JsonObject.getOr(key: String, or: Number): Number {
    return runCatching { this.get(key).asNumber }.getOrDefault(or)
}

fun JsonObject.getOr(key: String, or: Boolean): Boolean {
    return runCatching { this.get(key).asBoolean }.getOrDefault(or)
}

fun JsonObject.getOr(key: String, or: String): String {
    return runCatching { this.get(key).asString }.getOrDefault(or)
}

fun JsonObject.getOr(key: String, or: JsonObject): JsonObject {
    return runCatching { this.get(key).asJsonObject }.getOrDefault(or)
}

fun JsonObject.getOr(key: String, or: JsonArray): JsonArray {
    return runCatching { this.get(key).asJsonArray }.getOrDefault(or)
}

