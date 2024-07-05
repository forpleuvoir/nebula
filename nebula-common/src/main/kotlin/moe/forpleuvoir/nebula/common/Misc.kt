@file:Suppress("unused")

package moe.forpleuvoir.nebula.common

import kotlinx.coroutines.*
import moe.forpleuvoir.nebula.common.util.ClassScanner.getClassesForPackage
import java.util.concurrent.CompletableFuture
import java.util.function.Predicate
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.KClass
import kotlin.reflect.full.functions

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

inline fun <reified E : Enum<*>> Enum.Companion.valueOf(name: String): E? {
    return valueOf(E::class, name)
}

@Suppress("UNCHECKED_CAST")
fun <E : Enum<*>> Enum.Companion.valueOf(enumType: KClass<out E>, name: String): E? {
    return enumType.functions.find { it.name == "valueOf" && it.returnType.classifier == enumType }?.let {
        it.call(name) as E
    }
}

val DefaultCoroutineScope by lazy { CoroutineScope(Dispatchers.Default) }

fun defaultLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return DefaultCoroutineScope.launch(context, start, block)
}

fun <T> defaultAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> {
    return DefaultCoroutineScope.async(context, start, block)
}

fun defaultCancel(message: String, cause: Throwable? = null) {
    DefaultCoroutineScope.cancel(message, cause)
}

fun defaultCancel(cause: CancellationException? = null) {
    DefaultCoroutineScope.cancel(cause)
}

val IOCoroutineScope by lazy { CoroutineScope(Dispatchers.IO) }

fun ioLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return IOCoroutineScope.launch(context, start, block)
}

fun <T> ioAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> {
    return IOCoroutineScope.async(context, start, block)
}

fun ioCancel(message: String, cause: Throwable? = null) {
    IOCoroutineScope.cancel(message, cause)
}

fun ioCancel(cause: CancellationException? = null) {
    IOCoroutineScope.cancel(cause)
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