package moe.forpleuvoir.nebula.common.api

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.common.api

 * 文件名 Resettable

 * 创建时间 2022/12/5 23:10

 * @author forpleuvoir

 */
interface Resettable {

	fun isDefault(): Boolean

	fun restDefault()

}