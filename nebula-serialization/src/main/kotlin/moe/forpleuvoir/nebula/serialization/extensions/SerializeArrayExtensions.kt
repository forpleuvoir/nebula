@file:Suppress("UNUSED")
@file:OptIn(ExperimentalContracts::class)

package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.base.*
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

fun serializeArray(vararg elements: Any?): SerializeArray {
    return serializeArray(elements.toList())
}

inline fun serializeArray(scope: SerializeArray.() -> Unit): SerializeArray {
    contract {
        callsInPlace(scope, InvocationKind.EXACTLY_ONCE)
    }
    return SerializeArray().apply(scope)
}

fun serializeArray(iterator: Iterator<*>): SerializeArray {
    return SerializeArray().apply {
        for (element in iterator) {
            when (element) {
                is Boolean          -> add(element)
                is BigInteger       -> add(element)
                is BigDecimal       -> add(element)
                is Number           -> add(element)
                is String           -> add(element)
                is Char             -> add(element)
                is SerializeElement -> add(element)
                null                -> add(SerializeNull)
                else                -> add(element.toSerializeElement())
            }
        }
    }
}

inline fun <T> serializeArray(iterable: Iterable<T>, converter: (T) -> SerializeElement): SerializeArray {
    contract {
        callsInPlace(converter, InvocationKind.UNKNOWN)
    }
    return SerializeArray().apply {
        for (e in iterable) {
            add(converter(e))
        }
    }
}


fun serializeArray(elements: Iterable<*>): SerializeArray {
    return serializeArray(elements.iterator())
}

fun SerializeArray.toList(): List<Any?> {
    return map { e ->
        when (e) {
            is SerializePrimitive -> e.toObj()
            is SerializeObject    -> e.toMap()
            is SerializeArray     -> e.toList()
            is SerializeNull -> null
        }
    }
}