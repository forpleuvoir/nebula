package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.common.util.Time
import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.extensions.TimeDeserializer
import moe.forpleuvoir.nebula.serialization.extensions.serialization
import kotlin.time.DurationUnit

class ConfigTime(
    override val key: String,
    override val defaultValue: Time
) : ConfigBase<Time, ConfigTime>() {

    constructor(key: String, defaultValue: Double, defaultUnit: DurationUnit) : this(key, Time(defaultValue, defaultUnit))

    override var configValue: Time = defaultValue

    val duration get() = getValue().duration

    override fun setValue(value: Time) {
        super.setValue(Time(value.value, value.unit))
    }

    override fun deserialization(serializeElement: SerializeElement) {
        setValue(TimeDeserializer.deserialization(serializeElement).run {
            Time(value, unit)
        })
    }

    override fun serialization(): SerializeElement {
        return configValue.serialization()
    }

}

