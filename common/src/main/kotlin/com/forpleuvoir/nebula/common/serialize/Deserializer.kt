package com.forpleuvoir.nebula.common.serialize

/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.common.serialize

 * 文件名 Deserializer

 * 创建时间 2022/12/6 0:19

 * @author forpleuvoir

 */
interface Deserializer<T, S> {

	fun deserialization(serializeObject: S): T

}