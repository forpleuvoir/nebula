package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.common.util.Time
import moe.forpleuvoir.nebula.serialization.Deserializer
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import java.util.*
import kotlin.time.DurationUnit

fun Time.serialization(): SerializeElement {
    return serializeObject {
        "value" - value
        "unit" - unit.name
    }
}

object TimeDeserializer : Deserializer<Time> {
    override fun deserialization(serializeElement: SerializeElement): Time {
        serializeElement as SerializeObject
        val time = serializeElement["value"]!!.asDouble
        val unit = DurationUnit.valueOf(serializeElement["unit"]!!.asString)
        return Time(time, unit)
    }

}

fun Date.serialization(): SerializeElement {
    return SerializePrimitive(this.time)
}

fun Date.deserialization(serializeElement: SerializeElement) {
    this.time = serializeElement.asLong
}



