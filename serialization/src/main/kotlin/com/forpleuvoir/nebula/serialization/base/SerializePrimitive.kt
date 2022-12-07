package com.forpleuvoir.nebula.serialization.base

import com.forpleuvoir.nebula.serialization.base.internal.LazilyParsedNumber
import java.math.BigDecimal
import java.math.BigInteger

/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.serialization.base

 * 文件名 SerializePrimitive

 * 创建时间 2022/12/8 1:07

 * @author forpleuvoir

 */
class SerializePrimitive private constructor(private val value: Any) : SerializeElement {

	constructor(boolean: Boolean) : this(boolean as Any)

	constructor(string: String) : this(string as Any)

	constructor(number: Number) : this(number as Any)

	constructor(bigInteger: BigInteger) : this(bigInteger as Any)

	constructor(bigDecimal: BigDecimal) : this(bigDecimal as Any)

	override val deepCopy: SerializePrimitive get() = this

	val isString: Boolean get() = value is String

	val isBoolean: Boolean get() = value is Boolean

	val isNumber: Boolean get() = value is Number

	val isBigInteger: Boolean get() = value is BigInteger

	val isBigDecimal: Boolean get() = value is BigDecimal

	override val asString: String
		get() {
			return if (this.isNumber) {
				this.asNumber.toString()
			} else {
				if (this.isBoolean) (value as Boolean).toString() else (value as String)
			}
		}

	override val asBoolean: Boolean
		get() {
			return if (this.isBoolean) (value as Boolean) else asString.toBoolean()
		}

	override val asNumber: Number
		get() {
			return if (this.isString) LazilyParsedNumber(asString) else value as Number
		}

	override val asInt: Int
		get() {
			return if (this.isNumber) asNumber.toInt() else asString.toInt()
		}
	override val asLong: Long
		get() {
			return if (this.isNumber) asNumber.toLong() else asString.toLong()
		}
	override val asShort: Short
		get() {
			return if (this.isNumber) asNumber.toShort() else asString.toShort()
		}
	override val asByte: Byte
		get() {
			return if (this.isNumber) asNumber.toByte() else asString.toByte()
		}
	override val asFloat: Float
		get() {
			return if (this.isNumber) asNumber.toFloat() else asString.toFloat()
		}
	override val asDouble: Double
		get() {
			return if (this.isNumber) asNumber.toDouble() else asString.toDouble()
		}

	override val asBigInteger: BigInteger
		get() {
			return if (this.isBigDecimal) value as BigInteger else BigInteger(asString)
		}

	override val asBigDecimal: BigDecimal
		get() {
			return if (this.isBigDecimal) value as BigDecimal else BigDecimal(asString)
		}
}