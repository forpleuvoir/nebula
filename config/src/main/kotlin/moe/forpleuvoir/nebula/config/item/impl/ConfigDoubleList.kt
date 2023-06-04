package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.common.util.NotifiableArrayList
import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.item.ConfigMutableListValue
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.extensions.serializeArray

class ConfigDoubleList(
    override val key: String,
    defaultValue: List<Double>
) : ConfigBase<MutableList<Double>, ConfigDoubleList>(), ConfigMutableListValue<Double> {

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
                this@ConfigDoubleList.onChange(this@ConfigDoubleList)
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