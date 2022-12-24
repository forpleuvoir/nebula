@file:Suppress("unused")

package com.forpleuvoir.nebula.common

import com.forpleuvoir.nebula.common.util.ClassScanner.getClassesForPackage
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.function.Predicate
import kotlin.reflect.KClass

inline fun Boolean?.ifc(action: () -> Unit) {
	if (this == true) {
		action.invoke()
	}
}

inline fun Boolean?.notc(action: () -> Unit) = if (this != null) {
	if (!this) action() else Unit
} else Unit

fun <T> Boolean?.ternary(v1: T, v2: T): T = if (this == true) v1 else v2

inline fun times(timeConsuming: (Long) -> Unit = { println("耗时 : ${it / 1000000.0}ms") }, action: () -> Unit) {
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
			if (predicate.test(clazz.kotlin)) {
				add(clazz.kotlin)
			}
		}
	}
}

