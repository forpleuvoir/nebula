@file:Suppress("UNUSED")

package com.forpleuvoir.nebula.serialization.extensions

import com.forpleuvoir.nebula.serialization.Serializable
import com.forpleuvoir.nebula.serialization.base.*
import com.forpleuvoir.nebula.serialization.json.toJsonObject
import com.forpleuvoir.nebula.serialization.json.toObject
import kotlin.reflect.KVisibility.PUBLIC
import kotlin.reflect.full.memberProperties

/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.serialization.extensions

 * 文件名 SerializeObjectExtensions

 * 创建时间 2022/12/8 23:47

 * @author forpleuvoir

 */

fun Any.toSerializeObject(): SerializeObject {
	//todo后期修改为反射转换 不依赖gson
	if (this is Serializable) {
		return this.serialization().asObject
	}
	return this.toJsonObject().toObject()
}

fun SerializeObject.toMap(): Map<String, Any> {
	return buildMap {
		for (entry in this@toMap) {
			when (val value = entry.value) {
				is SerializePrimitive -> this[entry.key] = value.toObj()
				is SerializeArray     -> this[entry.key] = value.toList()
				is SerializeObject    -> this[entry.key] = value.toMap()
				is SerializeNull      -> this[entry.key] = SerializeNull.toString()
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

