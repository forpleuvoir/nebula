package moe.forpleuvoir.nebula.serialization.base.internal

import java.math.BigDecimal

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.serialization.base.internal

 * 文件名 LazilyParsedNumber

 * 创建时间 2022/12/8 2:11

 * @author forpleuvoir

 */
internal class LazilyParsedNumber(private val value: String) : Number() {

	fun type(): Class<*> {
		return value::class.java
	}

	override fun toByte(): Byte {
		return value.toByte()
	}

	override fun toChar(): Char {
		return value.toCharArray()[0]
	}

	override fun toDouble(): Double {
		return value.toDouble()
	}

	override fun toFloat(): Float {
		return value.toFloat()
	}

	override fun toInt(): Int {
		return try {
			value.toInt()
		} catch (e: NumberFormatException) {
			try {
				value.toLong().toInt()
			} catch (e: NumberFormatException) {
				BigDecimal(value).toInt()
			}
		}
	}

	override fun toLong(): Long {
		return try {
			value.toLong()
		} catch (e: NumberFormatException) {
			BigDecimal(value).toLong()
		}
	}

	override fun toShort(): Short {
		return value.toShort()
	}

	override fun toString(): String {
		return value
	}
}