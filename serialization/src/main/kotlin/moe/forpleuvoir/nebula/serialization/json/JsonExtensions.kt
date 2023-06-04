@file:Suppress("UNUSED")

package moe.forpleuvoir.nebula.serialization.json

import com.google.gson.*
import moe.forpleuvoir.nebula.serialization.base.*
import moe.forpleuvoir.nebula.serialization.extensions.toMap
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

internal fun JsonObject.toObject(): SerializeObject {
    val result = SerializeObject()
    for (entry in this.entrySet()) {
        result[entry.key] = entry.value.toElement()
    }
    return result
}

internal fun JsonElement.toElement(): SerializeElement {
    return when {
        isJsonPrimitive -> this.asJsonPrimitive.toPrimitive()
        isJsonArray     -> this.asJsonArray.toArray()
        isJsonObject    -> this.asJsonObject.toObject()
        else            -> SerializeNull
    }
}

internal fun JsonPrimitive.toPrimitive(): SerializePrimitive {
    return when {
        isBoolean -> SerializePrimitive(this.asBoolean)
        isNumber  -> SerializePrimitive(this.asNumber)
        isString  -> SerializePrimitive(this.asString)
        else      -> SerializePrimitive(this.toString())
    }
}

internal fun JsonArray.toArray(): SerializeArray {
    val result = SerializeArray(this.size())
    for (element in this) {
        result.add(element.toElement())
    }
    return result
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

fun SerializeObject.toJsonString(): String {
    return gson.toJson(this.toMap())
}

fun String.jsonStringToObject(): SerializeObject {
    return parseToJsonObject.toObject()
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
    val jsonObjectScope = JsonObjectScope()
    jsonObjectScope.scope()
    return jsonObjectScope.jsonObject
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
    return jsonObject {
        for (entry in map) {
            entry.key - converter(entry.value)
        }
    }
}

fun <T> JsonObject.getOr(key: String, or: T, converter: (JsonElement) -> T): T {
    return try {
        converter(this[key]!!)
    } catch (_: Exception) {
        or
    }
}


inline fun <reified T> JsonObject.getOr(key: String, or: T): T {
    return try {
        gson.fromJson(this.get(key), T::class.java)
    } catch (_: Exception) {
        or
    }
}

fun JsonObject.getOr(key: String, or: Number): Number {
    return try {
        this.get(key).asNumber
    } catch (_: Exception) {
        or
    }
}

fun JsonObject.getOr(key: String, or: Boolean): Boolean {
    return try {
        this.get(key).asBoolean
    } catch (_: Exception) {
        or
    }
}

fun JsonObject.getOr(key: String, or: String): String {
    return try {
        this.get(key).asString
    } catch (_: Exception) {
        or
    }
}

fun JsonObject.getOr(key: String, or: JsonObject): JsonObject {
    return try {
        this.get(key).asJsonObject
    } catch (_: Exception) {
        or
    }
}

fun JsonObject.getOr(key: String, or: JsonArray): JsonArray {
    return try {
        this.get(key).asJsonArray
    } catch (_: Exception) {
        or
    }
}

