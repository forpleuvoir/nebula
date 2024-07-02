package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.container.ConfigContainer
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.extensions.serialization
import java.util.*

class ConfigDate(
    override val key: String,
    override val defaultValue: Date
) : ConfigBase<Date, ConfigDate>() {

    override var configValue: Date = Date(defaultValue.time)

    override fun serialization(): SerializeElement = configValue.serialization()

    override fun deserialization(serializeElement: SerializeElement) {
        setValue(Date(serializeElement.asLong))
    }

}

fun ConfigContainer.date(key: String, defaultValue: Date) = addConfig(ConfigDate(key, defaultValue))