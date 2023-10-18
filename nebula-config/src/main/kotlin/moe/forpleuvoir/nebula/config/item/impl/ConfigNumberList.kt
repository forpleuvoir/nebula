package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.common.util.NotifiableArrayList
import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.item.ConfigMutableListValue
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.extensions.serializeArray

abstract class ConfigNumberList<T>(
    override val key: String,
    defaultValue: List<T>
) : ConfigBase<MutableList<T>, ConfigNumberList<T>>(), ConfigMutableListValue<T> where T : Number, T : Comparable<T> {

    final override val defaultValue: MutableList<T> = ArrayList(defaultValue)

    override var configValue: MutableList<T> = list(this.defaultValue)

    protected abstract val mapping: Number.() -> T
    override fun restDefault() {
        if (isDefault()) return
        configValue = list(defaultValue)
        onChange(this)
    }

    private fun list(list: List<T>): NotifiableArrayList<T> {
        return NotifiableArrayList(list).apply {
            subscribe {
                this@ConfigNumberList.onChange(this@ConfigNumberList)
            }
        }
    }

    override fun serialization(): SerializeElement =
        serializeArray(configValue)

    override fun deserialization(serializeElement: SerializeElement) {
        val list = serializeElement.asArray.map { it.asNumber.mapping() }
        configValue = list(list)
    }

}