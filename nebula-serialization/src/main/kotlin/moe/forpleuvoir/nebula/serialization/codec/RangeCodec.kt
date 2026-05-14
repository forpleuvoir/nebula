package moe.forpleuvoir.nebula.serialization.codec

import kotlinx.serialization.KSerializer
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import moe.forpleuvoir.nebula.serialization.nebula.toKSerializer
import kotlin.ranges.ClosedRange

context(codec: Codec<T>)
val <T : Comparable<T>> rangeCodec: Codec<ClosedRange<T>>
    get() = Codec.create<ClosedRange<T>>()
        .field<T>("start").getter(ClosedRange<T>::start).codec
        .field<T>("end").getter(ClosedRange<T>::endInclusive).codec
        .build { start, end -> start..end }

@Suppress("NOTHING_TO_INLINE")
inline fun <T : Comparable<T>> rangeCodec(codec: Codec<T>): Codec<ClosedRange<T>> = context(codec) { rangeCodec }

private inline fun <R, T> numberRangeCodec(crossinline conversion: (String) -> T, crossinline rangeConversion: (ClosedRange<T>) -> R): Codec<R>
        where R : ClosedRange<T>, T : Comparable<T> = object : Codec<R> {
    override fun serialization(target: R): SerializeElement =
        SerializePrimitive("${target.start}..${target.endInclusive}")

    override fun deserialization(element: SerializeElement): Result<R> = runCatching {
        element.asString!!.split("..").let {
            rangeConversion(conversion(it[0])..conversion(it[1]))
        }
    }

}

private val intRangeCodec: Codec<IntRange> = numberRangeCodec({ it.toInt() }, { it.start..it.endInclusive })

val IntRange.Companion.CODEC get() = intRangeCodec

inline val Codec.Companion.intRange get() = IntRange.CODEC

object IntRangeSerializer : KSerializer<IntRange> by IntRange.CODEC.toKSerializer()

private val uIntRangeCodec = numberRangeCodec({ it.toUInt() }, { it.start..it.endInclusive })

val UIntRange.Companion.CODEC get() = uIntRangeCodec

inline val Codec.Companion.uIntRange get() = UIntRange.CODEC

object UIntRangeSerializer : KSerializer<UIntRange> by UIntRange.CODEC.toKSerializer()

private val longRangeCodec = numberRangeCodec({ it.toLong() }, { it.start..it.endInclusive })

val LongRange.Companion.CODEC get() = longRangeCodec

inline val Codec.Companion.longRange get() = LongRange.CODEC

object LongRangeSerializer : KSerializer<LongRange> by LongRange.CODEC.toKSerializer()

private val uLongRangeCodec = numberRangeCodec({ it.toULong() }, { it.start..it.endInclusive })

val ULongRange.Companion.CODEC get() = uLongRangeCodec

inline val Codec.Companion.uLongRange get() = ULongRange.CODEC

object ULongRangeSerializer : KSerializer<ULongRange> by ULongRange.CODEC.toKSerializer()

private val charRangeCodec = numberRangeCodec({ it.single() }, { it.start..it.endInclusive })

val CharRange.Companion.CODEC get() = charRangeCodec

inline val Codec.Companion.charRange get() = CharRange.CODEC

object CharRangeSerializer : KSerializer<CharRange> by CharRange.CODEC.toKSerializer()

private val floatRangeCodec = numberRangeCodec({ it.toFloat() }, { it.start..it.endInclusive })

val Codec.Companion.floatRange get() = floatRangeCodec

private val doubleRangeCodec = numberRangeCodec({ it.toDouble() }, { it.start..it.endInclusive })

val Codec.Companion.doubleRange get() = doubleRangeCodec