package moe.forpleuvoir.nebula.serialization.codec

import moe.forpleuvoir.nebula.common.util.checkType
import moe.forpleuvoir.nebula.serialization.DeserializationException
import moe.forpleuvoir.nebula.serialization.base.SerializeArray
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.builder.build

inline fun <reified T> Codec.Companion.array(elementCodec: Codec<T>): Codec<Array<T>> = object : Codec<Array<T>> {
    override fun serialization(target: Array<T>): SerializeElement = SerializeArray.build(target.asIterable(), elementCodec)

    override fun deserialization(data: SerializeElement): Result<Array<T>> = DeserializationException.runCatching {
        data.checkType<SerializeArray, Array<T>> {
            it.mapIndexed { index, element ->
                elementCodec.deserialization(element).onFailure { e ->
                    throw DeserializationException("Failed to decode element at index $index", e)
                }.getOrThrow()
            }.toTypedArray()
        }
    }

}

fun <T> Codec.Companion.list(elementCodec: Codec<T>): Codec<List<T>> = object : Codec<List<T>> {
    override fun serialization(target: List<T>): SerializeElement = SerializeArray.build(target, elementCodec)

    override fun deserialization(data: SerializeElement): Result<List<T>> = DeserializationException.runCatching {
        data.checkType<SerializeArray, List<T>> {
            it.mapIndexed { index, element ->
                elementCodec.deserialization(element).onFailure { e ->
                    throw DeserializationException("Failed to decode element at index $index", e)
                }.getOrThrow()
            }
        }
    }
}

fun <T> Codec.Companion.map(elementCodec: Codec<T>): Codec<Map<String, T>> = object : Codec<Map<String, T>> {
    override fun serialization(target: Map<String, T>): SerializeElement = SerializeObject.build {
        context(elementCodec) {
            target.forEach { (key, value) -> set(key, value) }
        }
    }

    override fun deserialization(data: SerializeElement): Result<Map<String, T>> = DeserializationException.runCatching {
        data.checkType<SerializeObject, Map<String, T>> {
            it.mapValues { (k, v) ->
                elementCodec.deserialization(v).onFailure { e ->
                    throw DeserializationException("Failed to decode entry at key $k", e)
                }.getOrThrow()
            }
        }
    }

}