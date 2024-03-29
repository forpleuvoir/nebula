@file:Suppress("unused")

package moe.forpleuvoir.nebula.common

import kotlinx.coroutines.*
import moe.forpleuvoir.nebula.common.util.ClassScanner.getClassesForPackage
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.function.Predicate
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.KClass

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

fun <T> Boolean?.pick(v1: T, v2: T): T = if (this == true) v1 else v2

fun <R> Boolean?.pick(block: () -> R, block2: () -> R): R = if (this == true) block() else block2()

fun runAsync(runnable: Runnable): CompletableFuture<Void> {
    return CompletableFuture.runAsync(runnable)
}

val NebulaDispatcher by lazy { Executors.newFixedThreadPool(4).asCoroutineDispatcher() }

val CommonCoroutineScope by lazy { CoroutineScope(NebulaDispatcher) }

fun nebulaLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return CommonCoroutineScope.launch(context, start, block)
}

fun scanPackage(packageName: String, predicate: Predicate<KClass<*>>): Set<KClass<*>> {
    return buildSet {
        for (clazz in getClassesForPackage(packageName)) {
            clazz.declaredClasses.forEach {
                if (predicate.test(it.kotlin)) {
                    add(it.kotlin)
                }
            }
            if (predicate.test(clazz.kotlin)) {
                add(clazz.kotlin)
            }
        }
    }
}

@OptIn(ExperimentalContracts::class)
@JvmName("sumOfFloat")
inline fun <T> Iterable<T>.sumOf(selector: (T) -> Float): Float {
    contract {
        callsInPlace(selector, InvocationKind.UNKNOWN)
    }
    var sum: Float = 0.toFloat()
    for (element in this) {
        sum += selector(element)
    }
    return sum
}