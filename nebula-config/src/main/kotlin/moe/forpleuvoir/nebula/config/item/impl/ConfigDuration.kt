package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.extensions.deserialization
import moe.forpleuvoir.nebula.serialization.extensions.serialization
import kotlin.time.Duration

class ConfigDuration(
    override val key: String,
    override val defaultValue: Duration
) : ConfigBase<Duration, ConfigDuration>() {

    override var configValue: Duration = defaultValue

    override fun deserialization(serializeElement: SerializeElement) {
        setValue(Duration.deserialization(serializeElement))
    }

    override fun serialization(): SerializeElement {
        return configValue.serialization()
    }

}

