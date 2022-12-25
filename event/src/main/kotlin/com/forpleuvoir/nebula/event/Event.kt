@file:Suppress("UNUSED")

package com.forpleuvoir.nebula.event

import kotlin.reflect.KClass

interface Event {

	/**
	 * 事件是否可以取消
	 */
	val cancellable: Boolean get() = false

	/**
	 * 事件是否已被取消
	 */
	val canceled: Boolean get() = false

	/**
	 * 如果当前事件可以被取消则取消当前事件
	 */
	fun cancel() {
		if (!cancellable) throw EventException("this event could not be cancel")
	}

}

val KClass<out Event>.eventName: String get() = this.qualifiedName ?: this.java.name

val KClass<out Event>.eventSimpleName: String get() = this.simpleName ?: this.java.simpleName