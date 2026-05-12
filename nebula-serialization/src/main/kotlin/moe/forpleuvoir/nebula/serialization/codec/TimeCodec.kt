package moe.forpleuvoir.nebula.serialization.codec

import kotlinx.serialization.KSerializer
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import moe.forpleuvoir.nebula.serialization.extensions.checkType
import moe.forpleuvoir.nebula.serialization.nebula.toKSerializer
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.parse

val Duration.Companion.CODEC: Codec<Duration> by lazy {
    object : Codec<Duration> {
        override fun serialization(target: Duration): SerializeElement = SerializePrimitive(target.toString())

        override fun deserialization(element: SerializeElement): Result<Duration> =
            element.checkType<SerializePrimitive, Duration> { parse(it.asString!!) }

    }
}

inline val Codec.Companion.duration: Codec<Duration> get() = Duration.CODEC

object DurationSerializer : KSerializer<Duration> by Duration.CODEC.toKSerializer()

val DateCodec: Codec<Date> by lazy {
    object : Codec<Date> {
        override fun serialization(target: Date): SerializeElement = SerializePrimitive(target.time)

        override fun deserialization(element: SerializeElement): Result<Date> =
            element.checkType<SerializePrimitive, Date> { Date(it.asLong!!) }
    }
}

inline val Codec.Companion.date: Codec<Date> get() = DateCodec

object DateSerializer : KSerializer<Date> by DateCodec.toKSerializer()
