package moe.forpleuvoir.nebula.serialization.codec

import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import java.math.BigDecimal
import java.math.BigInteger

@Suppress("UNUSED")
object PrimitiveCodec {

    private fun <T : Any> create(f: (SerializeElement) -> T): Codec<T> = object : Codec<T> {
        override fun serialization(target: T): SerializeElement = when (target) {
            is Number  -> SerializePrimitive(target)
            is Boolean -> SerializePrimitive(target)
            is String  -> SerializePrimitive(target)
            is Char    -> SerializePrimitive(target)
            else       -> throw IllegalArgumentException("Unsupported type: ${target::class.simpleName}")
        }

        override fun deserialization(element: SerializeElement): Result<T> =
            runCatching { f(element) }
    }

    private fun <T : Any> create(default: T, f: (SerializeElement) -> T): Codec<T> = create {
        runCatching { f(it) }.getOrDefault(default)
    }

    private fun <T> create(range: ClosedRange<T>, f: (SerializeElement) -> T): Codec<T> where  T : Comparable<T> {
        require(range.start <= range.endInclusive) { "max(${range.start}) must be greater than min(${range.endInclusive})" }
        return create {
            f(it).coerceIn(range)
        }
    }

    private fun <T> create(default: T, range: ClosedRange<T>, f: (SerializeElement) -> T): Codec<T> where T : Number, T : Comparable<T> {
        require(range.start <= range.endInclusive) { "max(${range.start}) must be greater than min(${range.endInclusive})" }
        return create {
            val value = runCatching { f(it) }.getOrDefault(default)
            value.coerceIn(range)
        }
    }

    //region Byte
    val byte = create { it.asInt!!.toByte() }

    fun byte(default: Byte) = create(default) { it.asInt!!.toByte() }

    fun byte(range: ClosedRange<Byte>) = create(range) { it.asInt!!.toByte() }

    fun byte(default: Byte, range: ClosedRange<Byte>) = create(default, range) { it.asInt!!.toByte() }
    //endregion

    //region Int
    val int = create { it.asInt!! }

    fun int(default: Int) = create(default) { it.asInt!! }

    fun int(range: IntRange) = create(range) { it.asInt!! }

    fun int(default: Int, range: IntRange) = create(default, range) { it.asInt!! }
    //endregion

    //region Short
    val short = create { it.asShort!! }

    fun short(default: Short) = create(default) { it.asShort!! }

    fun short(range: ClosedRange<Short>) = create(range) { it.asShort!! }

    fun short(default: Short, range: ClosedRange<Short>) = create(default, range) { it.asShort!! }
    //endregion

    //region Long
    val long = create { it.asLong!! }

    fun long(default: Long) = create(default) { it.asLong!! }

    fun long(range: LongRange) = create(range) { it.asLong!! }

    fun long(default: Long, range: LongRange) = create(default, range) { it.asLong!! }
    //endregion

    //region Float
    val float = create { it.asFloat!! }

    fun float(default: Float) = create(default) { it.asFloat!! }

    fun float(range: ClosedRange<Float>) = create(range) { it.asFloat!! }

    fun float(default: Float, range: ClosedRange<Float>) = create(default, range) { it.asFloat!! }
    //endregion

    //region Double
    val double = create { it.asDouble!! }

    fun double(default: Double) = create(default) { it.asDouble!! }

    fun double(range: ClosedRange<Double>) = create(range) { it.asDouble!! }

    fun double(default: Double, range: ClosedRange<Double>) = create(default, range) { it.asDouble!! }
    //endregion

    //region Boolean
    val boolean = create { it.asBoolean!! }

    fun boolean(default: Boolean) = create(default) { it.asBoolean!! }
    //endregion

    //region Char
    val char = create { it.asString!!.first() }

    fun char(default: Char) = create(default) { it.asString!!.first() }
    //endregion

    //region String
    val string = create { it.asString!! }

    fun string(default: String) = create(default) { it.asString!! }
    //endregion

    //region BigInteger
    val bigInteger = create { it.asBigInteger!! }

    fun bigInteger(default: BigInteger) = create(default) { it.asBigInteger!! }

    fun bigInteger(range: ClosedRange<BigInteger>) = create(range) { it.asBigInteger!! }

    fun bigInteger(default: BigInteger, range: ClosedRange<BigInteger>) = create(default, range) { it.asBigInteger!! }
    //endregion

    //region BigDecimal
    val bigDecimal = create { it.asBigDecimal!! }

    fun bigDecimal(default: BigDecimal) = create(default) { it.asBigDecimal!! }

    fun bigDecimal(range: ClosedRange<BigDecimal>) = create(range) { it.asBigDecimal!! }

    fun bigDecimal(default: BigDecimal, range: ClosedRange<BigDecimal>) = create(default, range) { it.asBigDecimal!! }
    //endregion
}
