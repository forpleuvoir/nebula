package moe.forpleuvoir.nebula.serialization.nebula

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialFormat
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.codec.CharRangeSerializer
import moe.forpleuvoir.nebula.serialization.codec.ColorSerializer
import moe.forpleuvoir.nebula.serialization.codec.DateSerializer
import moe.forpleuvoir.nebula.serialization.codec.DurationSerializer
import moe.forpleuvoir.nebula.serialization.codec.IntRangeSerializer
import moe.forpleuvoir.nebula.serialization.codec.LongRangeSerializer
import moe.forpleuvoir.nebula.serialization.codec.UIntRangeSerializer
import moe.forpleuvoir.nebula.serialization.codec.ULongRangeSerializer
import java.util.Date
import kotlin.ranges.CharRange
import kotlin.ranges.IntRange
import kotlin.ranges.LongRange
import kotlin.ranges.UIntRange
import kotlin.ranges.ULongRange
import kotlin.reflect.KClass
import kotlin.time.Duration

object SerializerRegistry {

    private val serializers = mutableMapOf<KClass<*>, KSerializer<*>>()

    var version: Int = 0
        private set

    fun <T : Any> register(type: KClass<T>, serializer: KSerializer<T>) {
        serializers[type] = serializer
        version++
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(type: KClass<T>): KSerializer<T>? =
        serializers[type] as? KSerializer<T>

    fun all(): Map<KClass<*>, KSerializer<*>> = serializers.toMap()

    fun clear() {
        serializers.clear()
        version++
    }
}

open class NebulaFormat(
    private val customSerializersModule: SerializersModule? = null
) : SerialFormat {

    private var cachedModule: SerializersModule? = null
    private var cachedVersion: Int = -1

    override val serializersModule: SerializersModule
        get() = customSerializersModule ?: run {
            val currentVersion = SerializerRegistry.version
            if (cachedModule == null || currentVersion != cachedVersion) {
                cachedVersion = currentVersion
                cachedModule = @Suppress("UNCHECKED_CAST") SerializersModule {
                    SerializerRegistry.all().forEach { (type, serializer) ->
                        contextual(type as KClass<Any>, serializer as KSerializer<Any>)
                    }
                }
            }
            cachedModule!!
        }

    fun <T> encodeToElement(value: T, serializer: KSerializer<T>): SerializeElement {
        val encoder = NebulaEncoder(serializersModule)
        serializer.serialize(encoder, value)
        return encoder.result ?: error("NebulaFormat: encoding produced null result")
    }

    fun <T> decodeFromElement(element: SerializeElement, serializer: KSerializer<T>): T {
        val decoder = NebulaDecoder(element, serializersModule)
        return serializer.deserialize(decoder)
    }

    companion object : NebulaFormat() {
        init {
            SerializerRegistry.register(Color::class, ColorSerializer)
            SerializerRegistry.register(Duration::class, DurationSerializer)
            SerializerRegistry.register(Date::class, DateSerializer)
            SerializerRegistry.register(IntRange::class, IntRangeSerializer)
            SerializerRegistry.register(LongRange::class, LongRangeSerializer)
            SerializerRegistry.register(UIntRange::class, UIntRangeSerializer)
            SerializerRegistry.register(ULongRange::class, ULongRangeSerializer)
            SerializerRegistry.register(CharRange::class, CharRangeSerializer)
        }
    }
}

inline fun <reified T : Any> T.encode(serializer: KSerializer<T> = nebulaSerializer()): SerializeElement =
    NebulaFormat.encodeToElement(this, serializer)

inline fun <reified T : Any> SerializeElement.decode(serializer: KSerializer<T> = nebulaSerializer()): T =
    NebulaFormat.decodeFromElement(this, serializer)

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> nebulaSerializer(): KSerializer<T> {
    val resolved = SerializerRegistry.resolve(T::class)
    if (resolved != null) return resolved
    return serializer()
}
