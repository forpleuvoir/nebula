package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.common.util.NotifiableArrayList
import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.item.ConfigMutableListValue
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.extensions.serializeArray

class ConfigIntList(key: String, defaultValue: List<Int>) : ConfigNumberList<Int>(key, defaultValue) {
    override val mapping: Number.() -> Int get() = Number::toInt

}