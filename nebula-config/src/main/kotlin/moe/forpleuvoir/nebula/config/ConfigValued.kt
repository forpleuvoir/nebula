@file:Suppress("unused")

package moe.forpleuvoir.nebula.config

import kotlin.reflect.KProperty

interface ConfigValued<C> {

    val defaultValue: C

    fun getValue(): C

    fun setValue(value: C)

    fun asString(): String = getValue().toString()

    operator fun getValue(thisRef: Any?, property: KProperty<*>?): C = getValue()

    operator fun setValue(thisRef: Any?, property: KProperty<*>?, value: C) = setValue(value)
}

operator fun <C> ConfigValued<C>.invoke(): C = getValue()
