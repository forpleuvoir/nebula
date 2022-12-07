package com.forpleuvoir.nebula.serialization.base.internal

/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.serialization.base.internal

 * 文件名 LazilyParsedNumber

 * 创建时间 2022/12/8 2:11

 * @author forpleuvoir

 */
internal class LazilyParsedNumber(private val value: String) : Number() {
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
		return value.toInt()
	}

	override fun toLong(): Long {
		return value.toLong()
	}

	override fun toShort(): Short {
		return value.toShort()
	}
}