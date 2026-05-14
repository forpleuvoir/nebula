package moe.forpleuvoir.nebula.serialization.codec

import moe.forpleuvoir.nebula.serialization.DeserializationException
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import moe.forpleuvoir.nebula.serialization.extensions.checkType

fun <T : Enum<T>> enumCodec(values: Array<out T>) = object : Codec<T> {
    override fun serialization(target: T): SerializeElement = SerializePrimitive(target.name)
    override fun deserialization(element: SerializeElement): Result<T> = element.checkType<SerializePrimitive, T> { primitive ->
        values.firstOrNull { it.name == primitive.asString } ?: throw DeserializationException(
            "Unknown enum constant \"${primitive.asString}\", expected one of ${values.map { it.name }}"
        )
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun <T : Enum<T>> Codec.Companion.enum(values: Array<out T>): Codec<T> = enumCodec(values)

inline fun <reified T : Enum<T>> Codec.Companion.enum(): Codec<T> = enumCodec(enumValues())