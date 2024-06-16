package moe.forpleuvoir.nebula.serialization.extensions

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

fun Date.deserialization(serializeElement: SerializeElement) {
    this.time = serializeElement.asLong
}



