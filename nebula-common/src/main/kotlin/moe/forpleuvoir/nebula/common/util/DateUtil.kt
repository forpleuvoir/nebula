package moe.forpleuvoir.nebula.common.util

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun LocalDateTime.toDate(): Date {
	return Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
}

fun Date.toLocalDateTime(): LocalDateTime {
	return this.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
}

fun Date.format(format: String): String {
	return SimpleDateFormat(format).format(this)
}

val Number.second: Long get() = (this.toDouble() * 1000).toLong()

val Number.minute: Long get() = this.second * 60

val Number.hour: Long get() = this.minute * 60

val Number.day: Long get() = this.hour * 24

val Number.week: Long get() = this.day * 7

operator fun Date.plus(time: Long): Date {
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