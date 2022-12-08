package com.forpleuvoir.nebula.serialization

import com.forpleuvoir.nebula.serialization.base.SerializeElement

/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.serialization

 * 文件名 Serializer

 * 创建时间 2022/12/6 0:14

 * @author forpleuvoir

 */
@Suppress("unused")
interface Serializer<T> {
	fun serialization(target: T): SerializeElement
}