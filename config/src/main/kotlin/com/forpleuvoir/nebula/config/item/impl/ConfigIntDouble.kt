package com.forpleuvoir.nebula.config.item.impl

import com.forpleuvoir.nebula.common.util.NotifiableArrayList
import com.forpleuvoir.nebula.config.ConfigBase
import com.forpleuvoir.nebula.config.item.ConfigMutableListValue
import com.forpleuvoir.nebula.serialization.base.SerializeElement
import com.forpleuvoir.nebula.serialization.extensions.serializeArray

class ConfigIntDouble(
    override val key: String,
    defaultValue: List<Double>
) : ConfigBase<MutableList<Double>, ConfigIntDouble>(), ConfigMutableListValue<Double> {

    override val defaultValue: MutableList<Double> = ArrayList(defaultValue)

    override var configValue: MutableList<Double> = list(this.defaultValue)

    override fun restDefault() {
        if (isDefault()) return
        configValue = list(defaultValue)
        onChange(this)
    }

    private fun list(list: List<Double>): NotifiableArrayList<Double> {
        return NotifiableArrayList(list).apply {
            subscribe {
                this@ConfigIntDouble.onChange(this@ConfigIntDouble)
            }
        }
    }

    override fun serialization(): SerializeElement =
        serializeArray(configValue)

    override fun deserialization(serializeElement: SerializeElement) {
        val list = serializeElement.asArray.map { it.asDouble }
        configValue = list(list)
    }
}