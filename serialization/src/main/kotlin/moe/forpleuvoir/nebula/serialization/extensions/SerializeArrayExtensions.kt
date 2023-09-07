@file:Suppress("UNUSED")

package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.base.*

fun serializeArray(vararg elements: Any?): SerializeArray {
	return serializeArray(elements.toList())
}

fun serializeArray(iterator: Iterator<*>): SerializeArray {
	return SerializeArray().apply {
		for (element in iterator) {
			when (element) {
				is Boolean          -> add(element)
				is Number           -> add(element)
				is String           -> add(element)
				is Char             -> add(element)
				is SerializeElement -> add(element)
				null                -> add(SerializeNull)
				else                -> add(element.toSerializeObject())
			}
		}
	}
}

inline fun <T> serializeArray(iterable: Iterable<T>, converter: (T) -> SerializeArray): SerializeArray {
	return SerializeArray().apply {
		for (t in iterable) {
			add(converter(t))
		}
	}
}


fun serializeArray(elements: Iterable<*>): SerializeArray {
	return serializeArray(elements.iterator())
}

fun SerializeArray.toList(): List<Any> {
	return map { e ->
		when (e) {
			is SerializePrimitive -> e.toObj()
			is SerializeObject    -> e.toMap()
			is SerializeArray     -> e.toList()
            is SerializeNull      -> SerializeNull.toString()
            else                  -> e.toString()
		}
	}
}