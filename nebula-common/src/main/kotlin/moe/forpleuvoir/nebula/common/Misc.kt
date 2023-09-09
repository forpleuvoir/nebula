@file:Suppress("unused")

package moe.forpleuvoir.nebula.common

import moe.forpleuvoir.nebula.common.util.ClassScanner.getClassesForPackage
import java.util.concurrent.CompletableFuture
import java.util.function.Predicate
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass

@OptIn(ExperimentalContracts::class)
inline fun Boolean?.ifc(action: () -> Unit) {
	contract {
		callsInPlace(action, InvocationKind.AT_MOST_ONCE)
	}
	if (this == true) action.invoke()
}

@OptIn(ExperimentalContracts::class)
inline fun Boolean?.notc(action: () -> Unit) {
	contract {
		callsInPlace(action, InvocationKind.AT_MOST_ONCE)
	}
	if (this != null) {
		if (!this) action() else Unit
	}
}

fun <T> Boolean?.ternary(v1: T, v2: T): T = if (this == true) v1 else v2

@OptIn(ExperimentalContracts::class)
inline fun times(timeConsuming: (Long) -> Unit = { println("耗时 : ${it / 1000000.0}ms") }, action: () -> Unit) {
	contract {
		callsInPlace(action, InvocationKind.EXACTLY_ONCE)
	}
	val timeStart = System.nanoTime()
	action.invoke()
	timeConsuming(System.nanoTime() - timeStart)
}

fun runAsync(runnable: Runnable): CompletableFuture<Void> {
	return CompletableFuture.runAsync(runnable)
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