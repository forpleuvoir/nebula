package moe.forpleuvoir.nebula.common.util.primitive

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
@JvmName("sumOfFloat")
inline fun <T> Iterable<T>.sumOf(selector: (T) -> Float): Float {
    contract {
        callsInPlace(selector, InvocationKind.UNKNOWN)
    }
    var sum = 0f
    for (element in this) {
        sum += selector(element)
    }
    return sum
}