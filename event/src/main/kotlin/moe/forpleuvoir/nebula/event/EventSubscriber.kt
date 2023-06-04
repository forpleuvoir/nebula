package moe.forpleuvoir.nebula.event

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS

@Target(CLASS)
@Retention(RUNTIME)
annotation class EventSubscriber(val eventBus: String = "default")