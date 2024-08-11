package moe.forpleuvoir.nebula.common.util

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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