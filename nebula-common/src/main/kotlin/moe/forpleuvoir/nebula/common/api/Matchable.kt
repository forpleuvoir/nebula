package moe.forpleuvoir.nebula.common.api

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.common.api

 * 文件名 Matchable

 * 创建时间 2022/12/5 23:11

 * @author forpleuvoir

 */
interface Matchable {

	/**
	 * 匹配
	 * @param regex 正则表达式
	 * @return 是否匹配成功
	 */
	infix fun matched(regex: Regex): Boolean

}