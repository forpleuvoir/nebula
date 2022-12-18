package com.forpleuvoir.nebula.serialization.base

/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.serialization.base

 * 文件名 SerializeNull

 * 创建时间 2022/12/8 1:12

 * @author forpleuvoir

 */
object SerializeNull : SerializeElement {

	override val deepCopy: SerializeNull get() = this

	override fun equals(other: Any?): Boolean {
		return other == this
	}

	override fun hashCode(): Int {
		return SerializeNull::class.hashCode()
	}

	override fun toString(): String {
		return "SerializeNull"
	}
}