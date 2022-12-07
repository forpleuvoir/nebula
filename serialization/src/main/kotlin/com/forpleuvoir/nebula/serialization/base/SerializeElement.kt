package com.forpleuvoir.nebula.serialization.base

import java.math.BigDecimal
import java.math.BigInteger

/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.serialization.base

 * 文件名 SerializeElement

 * 创建时间 2022/12/8 0:43

 * @author forpleuvoir

 */
interface SerializeElement {

	val deepCopy: SerializeElement

	/**
	 * 是否为原始类型
	 * @return Boolean
	 */
	val isPrimitive: Boolean get() = this is SerializePrimitive

	/**
	 * 是否为对象
	 * @return Boolean
	 */
	val isObject: Boolean get() = this is SerializeObject

	/**
	 * 是否为数组
	 * @return Boolean
	 */
	val isArray: Boolean get() = this is SerializeArray

	/**
	 * 是否为空
	 * @return Boolean
	 */
	val isNull: Boolean get() = this is SerializeNull

	val asPrimitive: SerializePrimitive
		get() {
			if (this.isPrimitive) {
				return this as SerializePrimitive
			}
			throw IllegalStateException("Not a serialization Primitive")
		}

	val asObject: SerializeObject
		get() {
			if (this.isObject) {
				return this as SerializeObject
			}
			throw IllegalStateException("Not a serialization Object")
		}

	val asArray: SerializeArray
		get() {
			if (this.isArray) {
				return this as SerializeArray
			}
			throw IllegalStateException("Not a serialization Array")
		}

	val asNull: SerializeNull
		get() {
			if (this.isNull) {
				return this as SerializeNull
			}
			throw IllegalStateException("Not a serialization Null")
		}

	val asString: String
		get() {
			throw UnsupportedOperationException(this::class.simpleName)
		}

	val asBoolean: Boolean
		get() {
			throw UnsupportedOperationException(this::class.simpleName)
		}

	val asNumber: Number
		get() {
			throw UnsupportedOperationException(this::class.simpleName)
		}

	val asInt: Int
		get() {
			throw UnsupportedOperationException(this::class.simpleName)
		}

	val asLong: Long
		get() {
			throw UnsupportedOperationException(this::class.simpleName)
		}

	val asShort: Short
		get() {
			throw UnsupportedOperationException(this::class.simpleName)
		}

	val asByte: Byte
		get() {
			throw UnsupportedOperationException(this::class.simpleName)
		}

	val asFloat: Float
		get() {
			throw UnsupportedOperationException(this::class.simpleName)
		}

	val asDouble: Double
		get() {
			throw UnsupportedOperationException(this::class.simpleName)
		}

	val asBigInteger: BigInteger
		get() {
			throw UnsupportedOperationException(this::class.simpleName)
		}

	val asBigDecimal: BigDecimal
		get() {
			throw UnsupportedOperationException(this::class.simpleName)
		}


}
