package com.forpleuvoir.nebula.common.serialize

/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.common.serialize

 * 文件名 Serializer

 * 创建时间 2022/12/6 0:14

 * @author forpleuvoir

 */
interface Serializer<T, S> {
	fun serialization(target: T): S
}