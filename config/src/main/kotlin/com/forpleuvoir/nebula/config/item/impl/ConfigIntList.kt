package com.forpleuvoir.nebula.config.item.impl

import com.forpleuvoir.nebula.common.util.NotifiableArrayList
import com.forpleuvoir.nebula.config.ConfigBase
import com.forpleuvoir.nebula.config.item.ConfigMutableListValue
import com.forpleuvoir.nebula.serialization.base.SerializeElement
import com.forpleuvoir.nebula.serialization.extensions.serializeArray

class ConfigIntList(
    override val key: String,
    defaultValue: List<Int>
) : ConfigBase<MutableList<Int>, ConfigIntList>(), ConfigMutableListValue<Int> {

    override val defaultValue: MutableList<Int> = ArrayList(defaultValue)

    override var configValue: MutableList<Int> = list(this.defaultValue)

    override fun restDefault() {
        if (isDefault()) return
        configValue = list(defaultValue)
        onChange(this)
    }

    private fun list(list: List<Int>): NotifiableArrayList<Int> {
        return NotifiableArrayList(list).apply {
            subscribe {
                this@ConfigIntList.onChange(this@ConfigIntList)
            }
        }
    }

    override fun serialization(): SerializeElement =
        serializeArray(configValue)

    override fun deserialization(serializeElement: SerializeElement) {
        val list = serializeElement.asArray.map { it.asInt }
        configValue = list(list)
    }
}