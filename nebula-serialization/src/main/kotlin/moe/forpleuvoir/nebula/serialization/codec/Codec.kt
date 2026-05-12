@file:Suppress("unused", "NOTHING_TO_INLINE")

package moe.forpleuvoir.nebula.serialization.codec

import moe.forpleuvoir.nebula.serialization.Deserializer
import moe.forpleuvoir.nebula.serialization.Serializer
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeNull
import java.math.BigDecimal
import java.math.BigInteger

interface Codec<T> : Serializer<T>, Deserializer<T> {

    companion object {

        //region PrimitiveCodec
        //region Byte
        @JvmStatic
        inline val byte: Codec<Byte> get() = PrimitiveCodec.byte

        @JvmStatic
        inline fun byte(default: Byte): Codec<Byte> = PrimitiveCodec.byte(default)

        @JvmStatic
        inline fun byte(range: ClosedRange<Byte>): Codec<Byte> = PrimitiveCodec.byte(range)

        @JvmStatic
        inline fun byte(default: Byte, range: ClosedRange<Byte>): Codec<Byte> = PrimitiveCodec.byte(default, range)
        //endregion

        //region Short
        @JvmStatic
        inline val short: Codec<Short> get() = PrimitiveCodec.short

        @JvmStatic
        inline fun short(default: Short): Codec<Short> = PrimitiveCodec.short(default)

        @JvmStatic
        inline fun short(range: ClosedRange<Short>): Codec<Short> = PrimitiveCodec.short(range)

        @JvmStatic
        inline fun short(default: Short, range: ClosedRange<Short>): Codec<Short> = PrimitiveCodec.short(default, range)
        //endregion

        //region Int
        @JvmStatic
        inline val int: Codec<Int> get() = PrimitiveCodec.int

        @JvmStatic
        inline fun int(default: Int): Codec<Int> = PrimitiveCodec.int(default)

        @JvmStatic
        inline fun int(range: IntRange): Codec<Int> = PrimitiveCodec.int(range)

        @JvmStatic
        inline fun int(default: Int, range: IntRange): Codec<Int> = PrimitiveCodec.int(default, range)
        //endregion

        //region Long
        @JvmStatic
        inline val long: Codec<Long> get() = PrimitiveCodec.long

        @JvmStatic
        inline fun long(default: Long): Codec<Long> = PrimitiveCodec.long(default)

        @JvmStatic
        inline fun long(range: LongRange): Codec<Long> = PrimitiveCodec.long(range)

        @JvmStatic
        inline fun long(default: Long, range: LongRange): Codec<Long> = PrimitiveCodec.long(default, range)
        //endregion

        //region Float
        @JvmStatic
        inline val float: Codec<Float> get() = PrimitiveCodec.float

        @JvmStatic
        inline fun float(default: Float): Codec<Float> = PrimitiveCodec.float(default)

        @JvmStatic
        inline fun float(range: ClosedRange<Float>): Codec<Float> = PrimitiveCodec.float(range)

        @JvmStatic
        inline fun float(default: Float, range: ClosedRange<Float>): Codec<Float> = PrimitiveCodec.float(default, range)
        //endregion

        //region Double
        @JvmStatic
        inline val double: Codec<Double> get() = PrimitiveCodec.double

        @JvmStatic
        inline fun double(default: Double): Codec<Double> = PrimitiveCodec.double(default)

        @JvmStatic
        inline fun double(range: ClosedRange<Double>): Codec<Double> = PrimitiveCodec.double(range)

        @JvmStatic
        inline fun double(default: Double, range: ClosedRange<Double>): Codec<Double> = PrimitiveCodec.double(default, range)
        //endregion

        //region Boolean
        @JvmStatic
        inline val boolean: Codec<Boolean> get() = PrimitiveCodec.boolean

        @JvmStatic
        inline fun boolean(default: Boolean): Codec<Boolean> = PrimitiveCodec.boolean(default)
        //endregion

        //region Char
        @JvmStatic
        inline val char: Codec<Char> get() = PrimitiveCodec.char

        @JvmStatic
        inline fun char(default: Char): Codec<Char> = PrimitiveCodec.char(default)
        //endregion

        //region String
        @JvmStatic
        inline val string: Codec<String> get() = PrimitiveCodec.string

        @JvmStatic
        inline fun string(default: String): Codec<String> = PrimitiveCodec.string(default)
        //endregion

        //region BigInteger
        @JvmStatic
        inline val bigInteger: Codec<BigInteger> get() = PrimitiveCodec.bigInteger

        @JvmStatic
        inline fun bigInteger(default: BigInteger): Codec<BigInteger> = PrimitiveCodec.bigInteger(default)

        @JvmStatic
        inline fun bigInteger(range: ClosedRange<BigInteger>): Codec<BigInteger> = PrimitiveCodec.bigInteger(range)

        @JvmStatic
        inline fun bigInteger(default: BigInteger, range: ClosedRange<BigInteger>): Codec<BigInteger> = PrimitiveCodec.bigInteger(default, range)
        //endregion

        //region BigDecimal
        @JvmStatic
        inline val bigDecimal: Codec<BigDecimal> get() = PrimitiveCodec.bigDecimal

        @JvmStatic
        inline fun bigDecimal(default: BigDecimal): Codec<BigDecimal> = PrimitiveCodec.bigDecimal(default)

        @JvmStatic
        inline fun bigDecimal(range: ClosedRange<BigDecimal>): Codec<BigDecimal> = PrimitiveCodec.bigDecimal(range)

        @JvmStatic
        inline fun bigDecimal(default: BigDecimal, range: ClosedRange<BigDecimal>): Codec<BigDecimal> = PrimitiveCodec.bigDecimal(default, range)
        //endregion
        //endregion

        fun <T> create(): CodecBuilder<T> = CodecBuilder()
    }
}

fun <T> Codec<T>.nullable(): Codec<T?> = object : Codec<T?> {
    override fun serialization(target: T?) = when (target) {
        null -> SerializeNull
        else -> this@nullable.serialization(target)
    }

    override fun deserialization(element: SerializeElement): Result<T?> = when (element) {
        is SerializeNull -> Result.success(null)
        else             -> this@nullable.deserialization(element).map { it as T? }
    }
}

context(codec: Codec<T>)
inline val <T> T.serialization: SerializeElement get() = codec.serialization(this)

inline fun <T> T.serialization(codec: Codec<T>): SerializeElement = context(codec) { serialization }

context(codec: Codec<T>)
inline val <T> SerializeElement.deserialization: Result<T> get() = codec.deserialization(this)

inline fun <T> SerializeElement.deserialization(codec: Codec<T>): Result<T> = context(codec) { deserialization }