package moe.forpleuvoir.nebula.common.api

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.common.api

 * 文件名 Observable

 * 创建时间 2022/12/5 23:04

 * @author forpleuvoir

 */
interface Observable<T> {

    fun notifyChange(value: T)

    fun observe(callback: (T) -> Unit): Disposable

    fun interface Disposable {
        fun dispose()
    }
}