package com.forpleuvoir.nebula.serialization

import com.forpleuvoir.nebula.serialization.base.SerializeElement

/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.serialization

 * 文件名 Serializable

 * 创建时间 2022/12/6 0:00

 * @author forpleuvoir

 */
@Suppress("unused")
interface Serializable {

	fun serialization(): SerializeElement

}