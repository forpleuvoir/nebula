package moe.forpleuvoir.nebula.event

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.function.Consumer
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses

open class EventBusImpl : EventBus {

    /**
     * 所有事件订阅者
     */
    private val subscribers = ConcurrentHashMap<KClass<out Event>, ConcurrentLinkedQueue<EventSubscriber>>()

    override fun <E : Event> broadcast(event: E) {
        //
        subscribers[event::class]?.asSequence()
            ?.sortedByDescending {
                it.priority
            }?.forEach {
                it.action.accept(event)
            }

        event::class.allSuperclasses.forEach { eventChannel ->
            subscribers[eventChannel]?.asSequence()
                ?.filter {
                    it.greedy
                }?.sortedByDescending {
                    it.priority
                }?.forEach { subscriber ->
                    subscriber.action.accept(event)
                }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <E : Event> subscribe(channel: KClass<out E>, greedy: Boolean, priority: Int, subscriber: Consumer<E>) {
        if (subscribers.containsKey(channel)) {
            subscribers[channel]!!.add(EventSubscriber(subscriber as Consumer<Event>, greedy, priority))
        } else {
            subscribers[channel] = ConcurrentLinkedQueue<EventSubscriber>()
                .apply {
                    add(EventSubscriber(subscriber as Consumer<Event>, greedy, priority))
                }
        }
    }

    private data class EventSubscriber(val action: Consumer<Event>, val greedy: Boolean, val priority: Int)
}