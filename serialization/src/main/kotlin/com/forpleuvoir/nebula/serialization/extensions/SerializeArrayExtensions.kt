@file:Suppress("UNUSED")

package com.forpleuvoir.nebula.serialization.extensions

import com.forpleuvoir.nebula.serialization.base.*


/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.serialization.extensions

 * 文件名 SerializeArrayExtensions

 * 创建时间 2022/12/8 22:57

 * @author forpleuvoir

 */

fun serializeArray(vararg elements: Any): SerializeArray {
	return serializeArray(elements.toList())
}

inline fun <T> serializeArray(iterable: Iterable<T>, converter: (T) -> SerializeArray): SerializeArray {
	return SerializeArray().apply {
		for (t in iterable) {
			add(converter(t))
		}
	}
}


fun serializeArray(elements: Iterable<Any>): SerializeArray {
	return SerializeArray().apply {
		for (element in elements) {
			when (element) {
				is Boolean          -> add(element)
				is Number           -> add(element)
				is String           -> add(element)
				is Char             -> add(element)
				is SerializeElement -> add(element)

				else                -> add(element.toSerializeObject())
			}
		}
	}
}

fun SerializeArray.toList(): List<Any> {
	return map { e ->
		when (e) {
			is SerializePrimitive -> e.toObj()
			is SerializeObject    -> e.toMap()
			is SerializeArray     -> e.toList()
			is SerializeNull      -> "null"
			else                  -> e.toString()
		}
	}
}