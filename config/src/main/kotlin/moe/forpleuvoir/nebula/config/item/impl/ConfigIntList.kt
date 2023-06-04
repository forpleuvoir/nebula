package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.common.util.NotifiableArrayList
import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.item.ConfigMutableListValue
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.extensions.serializeArray

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