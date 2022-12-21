package com.forpleuvoir.nebula.common.util

import com.forpleuvoir.nebula.common.api.Notifiable
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Function

class NotifiableLinkedHashMap<K, V : Any>() : LinkedHashMap<K, V>(), Notifiable<NotifiableLinkedHashMap<K, V>> {

	constructor(callback: Consumer<NotifiableLinkedHashMap<K, V>>) : this() {
		subscribers.add(callback)
	}

	constructor(map: Map<K, V>) : this() {
		super.putAll(map)
	}

	private val subscribers: MutableList<Consumer<NotifiableLinkedHashMap<K, V>>> = ArrayList()

	override fun onChange(value: NotifiableLinkedHashMap<K, V>) {
		for (consumer in subscribers) {
			consumer.accept(value)
		}
	}

	override fun subscribe(callback: Consumer<NotifiableLinkedHashMap<K, V>>) {
		subscribers.add(callback)
	}

	override fun clear() {
		super.clear()
		onChange(this)
	}

	override fun put(key: K, value: V): V? {
		val v = super.put(key, value)
		if (v == null)
			onChange(this)
		return v
	}

	override fun putAll(from: Map<out K, V>) {
		super.putAll(from)
		onChange(this)
	}

	override fun remove(key: K): V? {
		val v = super.remove(key)
		if (v != null)
			onChange(this)
		return v
	}

	override fun remove(key: K, value: V): Boolean {
		return if (super.remove(key, value)) {
			onChange(this)
			true
		} else false

	}

	override fun replaceAll(function: BiFunction<in K, in V, out V>) {
		super.replaceAll(function)
		onChange(this)
	}

	override fun putIfAbsent(key: K, value: V): V? {
		val v = super.putIfAbsent(key, value)
		if (v == null) onChange(this)
		return v
	}

	override fun replace(key: K, oldValue: V, newValue: V): Boolean {
		return if (super.replace(key, oldValue, newValue)) {
			onChange(this)
			true
		} else false
	}

	override fun replace(key: K, value: V): V? {
		val v = super.replace(key, value)
		if (v != null) onChange(this)
		return v
	}

	override fun computeIfAbsent(key: K, mappingFunction: Function<in K, out V>): V {
		super.computeIfAbsent(key, mappingFunction).let {
			onChange(this)
			return it
		}
	}


	override fun computeIfPresent(key: K, remappingFunction: BiFunction<in K, in V, out V?>): V? {
		super.computeIfPresent(key, remappingFunction).let {
			onChange(this)
			return it
		}
	}

	override fun compute(key: K, remappingFunction: BiFunction<in K, in V?, out V?>): V? {
		super.compute(key, remappingFunction).let {
			onChange(this)
			return it
		}
	}


	override fun merge(key: K, value: V, remappingFunction: BiFunction<in V, in V, out V?>): V? {
		super.merge(key, value, remappingFunction).let {
			onChange(this)
			return it
		}
	}
}