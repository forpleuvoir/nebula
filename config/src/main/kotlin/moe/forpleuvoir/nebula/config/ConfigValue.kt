package moe.forpleuvoir.nebula.config

import kotlin.reflect.KProperty

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.config

 * 文件名 ConfigValue

 * 创建时间 2022/12/5 22:48

 * @author forpleuvoir

 */
interface ConfigValue<T> {

    val defaultValue: T

    fun getValue(): T

    fun setValue(value: T)

    fun asString(): String {
        return getValue().toString()
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>?): T

    operator fun setValue(thisRef: Any?, property: KProperty<*>?, value: T)
}