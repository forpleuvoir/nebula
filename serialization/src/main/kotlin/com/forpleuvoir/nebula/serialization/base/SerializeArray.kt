package com.forpleuvoir.nebula.serialization.base

/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.serialization.base

 * 文件名 SerializeArray

 * 创建时间 2022/12/8 1:12

 * @author forpleuvoir

 */
class SerializeArray private constructor(private val elements: MutableList<SerializeElement>) : SerializeElement, Iterable<SerializeElement> {
	constructor(capacity: Int? = null) : this(if (capacity != null) ArrayList(capacity) else ArrayList())

	override fun iterator(): Iterator<SerializeElement> = elements.iterator()

	override val deepCopy: SerializeElement
		get() = TODO("Not yet implemented")
}