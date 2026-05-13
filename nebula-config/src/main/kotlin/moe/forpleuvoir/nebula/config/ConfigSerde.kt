package moe.forpleuvoir.nebula.config

import kotlinx.serialization.KSerializer
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.codec.Codec
import moe.forpleuvoir.nebula.serialization.nebula.NebulaFormat

sealed interface ConfigSerde<C> {

    fun encode(value: C): SerializeElement

    fun decode(element: SerializeElement): Result<C>

    data class ViaCodec<C>(private val codec: Codec<C>) : ConfigSerde<C> {
        override fun encode(value: C): SerializeElement = codec.serialization(value)
        override fun decode(element: SerializeElement): Result<C> = codec.deserialization(element)
    }

    data class ViaKSerializer<C>(private val serializer: KSerializer<C>) : ConfigSerde<C> {
        override fun encode(value: C): SerializeElement =
            NebulaFormat.encodeToElement(value, serializer)

        override fun decode(element: SerializeElement): Result<C> =
            runCatching { NebulaFormat.decodeFromElement(element, serializer) }
    }

    @Suppress("NOTHING_TO_INLINE")
    companion object {
        inline fun <T> of(serializer: KSerializer<T>): ConfigSerde<T> = ViaKSerializer(serializer)

        inline fun <T> of(codec: Codec<T>): ConfigSerde<T> = ViaCodec(codec)
    }
}
