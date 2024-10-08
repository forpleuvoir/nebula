package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.common.util.collection.NotifiableArrayList
import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.container.ConfigContainer
import moe.forpleuvoir.nebula.config.item.ConfigMutableListValue
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.extensions.serializeArray

open class ConfigList<T>(
    override val key: String,
    defaultValue: List<T>,
    protected open val serializer: (T) -> SerializeElement,
    protected open val deserializer: (SerializeElement) -> T
) : ConfigBase<MutableList<T>, ConfigList<T>>(), ConfigMutableListValue<T> {

    final override val defaultValue: MutableList<T> = ArrayList(defaultValue)

    override var configValue: MutableList<T> = notifiableList(this.defaultValue)

    override fun restDefault() {
        if (isDefault()) return
        configValue = notifiableList(defaultValue)
        onChange(this)
    }

    private fun notifiableList(list: List<T>): NotifiableArrayList<T> {
        return NotifiableArrayList(list).apply {
            subscribe {
                this@ConfigList.onChange(this@ConfigList)
            }
        }
    }

    override fun serialization(): SerializeElement =
        serializeArray(configValue, serializer)

    override fun deserialization(serializeElement: SerializeElement) {
        setValue(notifiableList(serializeElement.asArray.map { deserializer(it) }))
    }

}

fun <T> ConfigContainer.list(
    key: String,
    defaultValue: List<T>,
    serializer: (T) -> SerializeElement,
    deserializer: (SerializeElement) -> T
) = addConfig(ConfigList(key, defaultValue, serializer, deserializer))