@file:Suppress("UNUSED")

package moe.forpleuvoir.nebula.event

import java.util.function.Consumer
import kotlin.reflect.KClass


interface EventBus {

	companion object {

		@JvmStatic
		val DEFAULT_EVENT_BUS = EventBusImpl()

		private val eventBusContainer: HashMap<String, EventBus> = HashMap()

		init {
			eventBusContainer["default"] = DEFAULT_EVENT_BUS
		}

		fun <E : Event> broadcast(event: E) = DEFAULT_EVENT_BUS.broadcast(event)

		fun <E : Event> subscribe(channel: KClass<out E>, greedy: Boolean = false, subscriber: Consumer<E>) =
			DEFAULT_EVENT_BUS.subscribe(channel, greedy, subscriber)

		inline fun <reified E : Event> subscribe(greedy: Boolean = false, subscriber: Consumer<E>) {
			subscribe(E::class, greedy, subscriber)
		}

		@JvmStatic
		fun registerEventBus(name: String, eventBus: EventBus) {
			if (eventBusContainer.containsKey(name)) {
				throw Exception("this EventBus was existed")
			}
			eventBusContainer[name] = eventBus
		}

		@JvmStatic
		operator fun get(name: String): EventBus? = eventBusContainer[name]
	}

	fun <E : Event> broadcast(event: E)
	fun <E : Event> subscribe(channel: KClass<out E>, greedy: Boolean = false, subscriber: Consumer<E>)

}

inline fun <reified E : Event> EventBus.subscribe(greedy: Boolean = false, subscriber: Consumer<E>) {
	subscribe(E::class, greedy, subscriber)
}