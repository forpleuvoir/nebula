package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.common.util.NotifiableLinkedHashMap
import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.item.ConfigMutableMapValue
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.extensions.serializeObject

class ConfigStringMap(
    override val key: String,
    defaultValue: Map<String, String>
) : ConfigBase<MutableMap<String, String>, ConfigStringMap>(), ConfigMutableMapValue<String, String> {

    override val defaultValue: MutableMap<String, String> = LinkedHashMap(defaultValue)

    override var configValue: MutableMap<String, String> = map(this.defaultValue)

    private fun map(map: Map<String, String>): NotifiableLinkedHashMap<String, String> {
        return NotifiableLinkedHashMap(map).apply {
            subscribe {
                this@ConfigStringMap.onChange(this@ConfigStringMap)
            }
        }
    }

    override fun restDefault() {
        if (isDefault()) return
        configValue = map(defaultValue)
        onChange(this)
    }

    override fun serialization(): SerializeElement =
        serializeObject(configValue)

    override fun deserialization(serializeElement: SerializeElement) {
        serializeElement.asObject.apply {
            setValue(this@ConfigStringMap.map(this.mapValues {
                it.value.asString
            }))
        }
    }
}