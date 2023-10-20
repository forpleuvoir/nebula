package moe.forpleuvoir.nebula.common.util

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

fun LocalDateTime.toDate(): Date {
    return Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
}

fun Date.toLocalDateTime(): LocalDateTime {
    return this.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
}

fun Date.format(format: String): String {
    return SimpleDateFormat(format).format(this)
}

operator fun Date.plus(time: Long): Date {
    5.minutes
    return Date(this.time + time)
}

operator fun Date.plus(time: Date): Date {
    return Date(this.time + time.time)
}

operator fun Date.plusAssign(time: Long) {
    this.time += time
}

operator fun Date.plusAssign(time: Date) {
    this.time += time.time
}

operator fun Date.minus(time: Long): Date {
    return Date(this.time - time)
}

operator fun Date.minus(time: Date): Date {
    return Date(this.time - time.time)
}

operator fun Date.minusAssign(time: Long) {
    this.time -= time
}

operator fun Date.minusAssign(time: Date) {
    this.time -= time.time
}

fun Timer.schedule(delay: Duration, period: Duration, task: () -> Unit) {
    this.schedule(object : TimerTask() {
        override fun run() {
            task()
        }
    }, delay.inWholeMilliseconds, period.inWholeMilliseconds)
}