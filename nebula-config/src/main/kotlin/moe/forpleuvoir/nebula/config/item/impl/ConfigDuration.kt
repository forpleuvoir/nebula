package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.common.util.SerializableDuration
import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.extensions.TimeDeserializer
import moe.forpleuvoir.nebula.serialization.extensions.serialization
import kotlin.time.Duration
import kotlin.time.DurationUnit

class ConfigDuration(
    override val key: String,
    override val defaultValue: SerializableDuration
) : ConfigBase<SerializableDuration, ConfigDuration>() {

    constructor(key: String, defaultValue: Double, defaultUnit: DurationUnit) : this(key, SerializableDuration(defaultValue, defaultUnit))

    constructor(key: String, defaultValue: Duration) : this(key, SerializableDuration(defaultValue.inWholeNanoseconds.toDouble(), DurationUnit.NANOSECONDS))

    override var configValue: SerializableDuration = defaultValue

    val duration get() = getValue().duration

    override fun setValue(value: SerializableDuration) {
        super.setValue(SerializableDuration(value.value, value.unit))
    }

    override fun deserialization(serializeElement: SerializeElement) {
        setValue(TimeDeserializer.deserialization(serializeElement).run {
            SerializableDuration(value, unit)
        })
    }

    override fun serialization(): SerializeElement {
        return configValue.serialization()
    }

}

