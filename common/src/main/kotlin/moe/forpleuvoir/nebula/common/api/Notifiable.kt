package moe.forpleuvoir.nebula.common.api

import java.util.function.Consumer

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.common.api

 * 文件名 Notifiable

 * 创建时间 2022/12/5 23:04

 * @author forpleuvoir

 */
interface Notifiable<T> {

	fun onChange(value: T)

	fun subscribe(callback: Consumer<T>)

}