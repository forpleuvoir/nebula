package moe.forpleuvoir.nebula.config

import kotlinx.serialization.KSerializer
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.codec.Codec
import moe.forpleuvoir.nebula.serialization.nebula.NebulaFormat
import moe.forpleuvoir.nebula.serialization.nebula.toCodec
import moe.forpleuvoir.nebula.serialization.nebula.toKSerializer

sealed interface ConfigSerde<C> {

    fun encode(value: C): SerializeElement

    fun decode(element: SerializeElement): Result<C>

    val asCodec: Codec<C>

    val asKSerializer: KSerializer<C>

    data class ViaCodec<C : Any>(private val codec: Codec<C>) : ConfigSerde<C> {
        override fun encode(value: C): SerializeElement = codec.serialization(value)
        override fun decode(element: SerializeElement): Result<C> = codec.deserialization(element)
        override val asCodec: Codec<C> get() = codec
        override val asKSerializer: KSerializer<C> by lazy { codec.toKSerializer() }
    }

    data class ViaKSerializer<C : Any>(private val serializer: KSerializer<C>) : ConfigSerde<C> {
        override fun encode(value: C): SerializeElement = NebulaFormat.encodeToElement(value, serializer)
        override fun decode(element: SerializeElement): Result<C> = runCatching { NebulaFormat.decodeFromElement(element, serializer) }
        override val asCodec: Codec<C> by lazy { serializer.toCodec() }
        override val asKSerializer: KSerializer<C> get() = serializer
    }

    @Suppress("NOTHING_TO_INLINE")
    companion object {
        inline fun <T : Any> of(serializer: KSerializer<T>): ConfigSerde<T> = ViaKSerializer(serializer)

        inline fun <T : Any> of(codec: Codec<T>): ConfigSerde<T> = ViaCodec(codec)
    }
}
