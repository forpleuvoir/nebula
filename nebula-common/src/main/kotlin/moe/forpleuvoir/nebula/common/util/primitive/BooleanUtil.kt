package moe.forpleuvoir.nebula.common.util.primitive

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline infix fun Boolean?.onTrue(block: () -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (this == true) block()
}

@OptIn(ExperimentalContracts::class)
inline infix fun Boolean?.onFalse(block: () -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (this != true) block()
}

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Boolean?.either(onTrue: T, onFalse: T): T = if (this == true) onTrue else onFalse

inline fun <R> Boolean?.either(onTrue: () -> R, onFalse: () -> R): R = if (this == true) onTrue() else onFalse()