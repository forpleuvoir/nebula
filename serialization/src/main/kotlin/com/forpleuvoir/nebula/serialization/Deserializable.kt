package com.forpleuvoir.nebula.serialization

import com.forpleuvoir.nebula.serialization.base.SerializeElement

/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.serialization

 * 文件名 Deserializable

 * 创建时间 2022/12/6 0:21

 * @author forpleuvoir

 */
@Suppress("unused")
interface Deserializable {

	fun deserialization(serializeElement: SerializeElement)

}