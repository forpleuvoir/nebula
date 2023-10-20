package moe.forpleuvoir.nebula.config.item

import moe.forpleuvoir.nebula.config.ConfigValue

interface ConfigNumberValue<T> : ConfigValue<T> where T : Number, T : Comparable<T> {

    val minValue: T

    val maxValue: T

    fun T.clamp(): T

}