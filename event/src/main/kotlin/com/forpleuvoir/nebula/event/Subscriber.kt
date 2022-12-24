package com.forpleuvoir.nebula.event

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION

@Target(FUNCTION)
@Retention(RUNTIME)
annotation class Subscriber(val greedy: Boolean = false)