package com.forpleuvoir.nebula.serialization

import com.forpleuvoir.nebula.serialization.base.SerializeElement

/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.serialization

 * 文件名 Deserializer

 * 创建时间 2022/12/6 0:19

 * @author forpleuvoir

 */
@Suppress("unused")
interface Deserializer<T> {

	fun deserialization(serializeElement: SerializeElement): T

}