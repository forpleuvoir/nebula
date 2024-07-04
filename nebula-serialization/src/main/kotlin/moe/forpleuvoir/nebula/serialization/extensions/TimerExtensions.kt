package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.Deserializer
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import java.util.*
import kotlin.time.Duration

fun Duration.serialization(): SerializePrimitive {
    return SerializePrimitive(this.toString())
}

fun Duration.Companion.deserialization(serializeElement: SerializeElement): Duration {
    return serializeElement.checkType<SerializePrimitive, Duration> {
        parse(it.asString)
    }.getOrThrow()
}

fun Date.serialization(): SerializeElement {
    return SerializePrimitive(this.time)
}

object DateDeserializer : Deserializer<Date> {
    override fun deserialization(serializeElement: SerializeElement): Date {
        return Date(serializeElement.checkType<SerializePrimitive, Long> { it.asLong }.getOrThrow())
    }
}

fun Date.deserialization(serializeElement: SerializeElement) {
    this.time = serializeElement.checkType<SerializePrimitive, Long> { it.asLong }.getOrThrow()
}



