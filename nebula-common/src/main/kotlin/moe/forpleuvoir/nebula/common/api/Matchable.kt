package moe.forpleuvoir.nebula.common.api

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.common.api

 * 文件名 Matchable

 * 创建时间 2022/12/5 23:11

 * @author forpleuvoir

 */
fun interface Matchable<T> {

	/**
	 * 匹配
	 * @param target 匹配推对象
	 * @return 是否匹配成功
	 */
	infix fun matched(target: T): Boolean

}