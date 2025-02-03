package moe.forpleuvoir.nebula.event

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION

@Target(FUNCTION)
@Retention(RUNTIME)
annotation class Subscriber(val greedy: Boolean = false, val priority: Int = EventPriority.NORMAL)

object EventPriority {

    const val LOWEST = 0
    const val LOW = 50
    const val NORMAL = 100
    const val HIGH = 150
    const val HIGHEST = 200
    const val MONITOR = 250

}