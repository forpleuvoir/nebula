package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.container.ConfigContainer
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.extensions.deserialization
import moe.forpleuvoir.nebula.serialization.extensions.serialization
import kotlin.time.Duration

class ConfigDuration(
    override val key: String,
    override val defaultValue: Duration,
    val minDuration: Duration = Duration.ZERO,
    val maxDuration: Duration = Duration.INFINITE
) : ConfigBase<Duration, ConfigDuration>() {

    override var configValue: Duration = defaultValue

    override fun setValue(value: Duration) {
        super.setValue(value.coerceIn(minDuration, maxDuration))
    }

    override fun deserialization(serializeElement: SerializeElement) {
        setValue(Duration.deserialization(serializeElement))
    }

    override fun serialization(): SerializeElement {
        return configValue.serialization()
    }

}

fun ConfigContainer.duration(key: String, defaultValue: Duration) = addConfig(ConfigDuration(key, defaultValue))