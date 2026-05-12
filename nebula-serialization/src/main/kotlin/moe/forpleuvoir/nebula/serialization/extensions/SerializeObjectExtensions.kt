@file:Suppress("UNUSED","NOTHING_TO_INLINE")

package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.base.SerializeNull
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import java.math.BigDecimal
import java.math.BigInteger

// region — strict type check: only returns value if the stored type matches exactly

inline fun SerializeObject.getString(key: String): String? =
    this[key]?.asPrimitive?.takeIf { it.isString }?.asString

inline fun SerializeObject.getBoolean(key: String): Boolean? =
    this[key]?.asPrimitive?.takeIf { it.isBoolean }?.asBoolean

inline fun SerializeObject.getByte(key: String): Byte? =
    this[key]?.asPrimitive?.takeIf { it.isByte }?.asByte

inline fun SerializeObject.getShort(key: String): Short? =
    this[key]?.asPrimitive?.takeIf { it.isShort }?.asShort

inline fun SerializeObject.getInt(key: String): Int? =
    this[key]?.asPrimitive?.takeIf { it.isInt }?.asInt

inline fun SerializeObject.getLong(key: String): Long? =
    this[key]?.asPrimitive?.takeIf { it.isLong }?.asLong

inline fun SerializeObject.getFloat(key: String): Float? =
    this[key]?.asPrimitive?.takeIf { it.isFloat }?.asFloat

inline fun SerializeObject.getDouble(key: String): Double? =
    this[key]?.asPrimitive?.takeIf { it.isDouble }?.asDouble

inline fun SerializeObject.getChar(key: String): Char? =
    this[key]?.asPrimitive?.takeIf { it.isChar }?.asChar

inline fun SerializeObject.getBigInteger(key: String): BigInteger? =
    this[key]?.asPrimitive?.takeIf { it.isBigInteger }?.asBigInteger

inline fun SerializeObject.getBigDecimal(key: String): BigDecimal? =
    this[key]?.asPrimitive?.takeIf { it.isBigDecimal }?.asBigDecimal

// endregion

// region — lenient conversion: converts the value to the target type if possible

inline fun SerializeObject.getAsString(key: String): String? =
    this[key]?.asString

inline fun SerializeObject.getAsBoolean(key: String): Boolean? =
    this[key]?.asBoolean

inline fun SerializeObject.getAsByte(key: String): Byte? =
    this[key]?.asByte

inline fun SerializeObject.getAsShort(key: String): Short? =
    this[key]?.asShort

inline fun SerializeObject.getAsInt(key: String): Int? =
    this[key]?.asInt

inline fun SerializeObject.getAsLong(key: String): Long? =
    this[key]?.asLong

inline fun SerializeObject.getAsFloat(key: String): Float? =
    this[key]?.asFloat

inline fun SerializeObject.getAsDouble(key: String): Double? =
    this[key]?.asDouble

inline fun SerializeObject.getAsChar(key: String): Char? =
    this[key]?.asChar

inline fun SerializeObject.getAsBigInteger(key: String): BigInteger? =
    this[key]?.asBigInteger

inline fun SerializeObject.getAsBigDecimal(key: String): BigDecimal? =
    this[key]?.asBigDecimal

inline fun SerializeObject.getAsNull(key: String): SerializeNull? =
    this[key]?.asNull

// endregion

// region — getOrElse: value or default

inline fun SerializeObject.getOrElse(key: String, default: String): String =
    this[key]?.asString ?: default

inline fun SerializeObject.getOrElse(key: String, default: Boolean): Boolean =
    this[key]?.asBoolean ?: default

inline fun SerializeObject.getOrElse(key: String, default: Byte): Byte =
    this[key]?.asByte ?: default

inline fun SerializeObject.getOrElse(key: String, default: Short): Short =
    this[key]?.asShort ?: default

inline fun SerializeObject.getOrElse(key: String, default: Int): Int =
    this[key]?.asInt ?: default

inline fun SerializeObject.getOrElse(key: String, default: Long): Long =
    this[key]?.asLong ?: default

inline fun SerializeObject.getOrElse(key: String, default: Float): Float =
    this[key]?.asFloat ?: default

inline fun SerializeObject.getOrElse(key: String, default: Double): Double =
    this[key]?.asDouble ?: default

inline fun SerializeObject.getOrElse(key: String, default: Char): Char =
    this[key]?.asChar ?: default

inline fun SerializeObject.getOrElse(key: String, default: BigInteger): BigInteger =
    this[key]?.asBigInteger ?: default

inline fun SerializeObject.getOrElse(key: String, default: BigDecimal): BigDecimal =
    this[key]?.asBigDecimal ?: default

// endregion


