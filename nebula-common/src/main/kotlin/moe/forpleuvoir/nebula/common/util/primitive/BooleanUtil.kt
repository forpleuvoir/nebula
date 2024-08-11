package moe.forpleuvoir.nebula.common.util.primitive

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


@OptIn(ExperimentalContracts::class)
inline fun Boolean?.ifc(block: () -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (this == true) block.invoke()
}

@OptIn(ExperimentalContracts::class)
inline fun Boolean?.notc(block: () -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (this != null) {
        if (!this) block() else Unit
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Boolean?.pick(v1: T, v2: T): T = if (this == true) v1 else v2

inline fun <R> Boolean?.pick(block: () -> R, block2: () -> R): R = if (this == true) block() else block2()