package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.common.util.requireType
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import java.math.BigDecimal
import java.math.BigInteger

fun SerializePrimitive.requireDouble(prefix: String? = null): Double = this.value.requireType<Double>(prefix)

fun SerializePrimitive.requireFloat(prefix: String? = null): Float = this.value.requireType<Float>(prefix)

fun SerializePrimitive.requireLong(prefix: String? = null): Long = this.value.requireType<Long>(prefix)

fun SerializePrimitive.requireInt(prefix: String? = null): Int = this.value.requireType<Int>(prefix)

fun SerializePrimitive.requireString(prefix: String? = null): String = this.value.requireType<String>(prefix)

fun SerializePrimitive.requireChar(prefix: String? = null): Char = this.value.requireType<Char>(prefix)

fun SerializePrimitive.requireBoolean(prefix: String? = null): Boolean = this.value.requireType<Boolean>(prefix)

fun SerializePrimitive.requireBigInt(prefix: String? = null): BigInteger = this.value.requireType<BigInteger>(prefix)

fun SerializePrimitive.requireBigDecimal(prefix: String? = null): BigDecimal = this.value.requireType<BigDecimal>(prefix)