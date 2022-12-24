package com.forpleuvoir.nebula.event

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.function.Consumer
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses

open class EventBusImpl : EventBus {

	/**
	 * 所有事件订阅者
	 */
	private val subscribers = ConcurrentHashMap<KClass<out Event>, ConcurrentLinkedQueue<EventPair>>()

	override fun <E : Event> broadcast(event: E) {
		subscribers[event::class]?.forEach { it.action.accept(event) }
		val superClass = event::class.allSuperclasses
		superClass.forEach { eventChannel ->
			subscribers[eventChannel]?.forEach {
				if (it.greedy) it.action.accept(event)
			}
		}
	}

	@Suppress("UNCHECKED_CAST")
	override fun <E : Event> subscribe(channel: KClass<out E>, greedy: Boolean, subscriber: Consumer<E>) {
		if (subscribers.containsKey(channel)) {
			subscribers[channel]?.add(EventPair(subscriber as Consumer<Event>, greedy))
		} else {
			val list = ConcurrentLinkedQueue<EventPair>()
			list.add(EventPair(subscriber as Consumer<Event>, greedy))
			subscribers[channel] = list
		}
	}

	private data class EventPair(val action: Consumer<Event>, val greedy: Boolean)
}