package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.common.util.SerializableDuration
import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.extensions.DurationDeserializer
import moe.forpleuvoir.nebula.serialization.extensions.serialization
import kotlin.reflect.KProperty
import kotlin.time.Duration
import kotlin.time.DurationUnit

class ConfigDuration(
    override val key: String,
    override val defaultValue: SerializableDuration
) : ConfigBase<SerializableDuration, ConfigDuration>() {

    constructor(key: String, defaultValue: Double, defaultUnit: DurationUnit) : this(key, SerializableDuration(defaultValue, defaultUnit))

    constructor(key: String, defaultValue: Duration) : this(key, SerializableDuration(defaultValue.inWholeNanoseconds.toDouble(), DurationUnit.NANOSECONDS))

    override var configValue: SerializableDuration = defaultValue

    val asDuration by lazy { DurationDelegated(this) }

    fun setValue(duration: Duration) {
        setValue(SerializableDuration(duration.inWholeNanoseconds.toDouble(), DurationUnit.NANOSECONDS))
    }

    override fun setValue(value: SerializableDuration) {
        super.setValue(SerializableDuration(value.value, value.unit))
    }

    override fun deserialization(serializeElement: SerializeElement) {
        setValue(DurationDeserializer.deserialization(serializeElement).run {
            SerializableDuration(value, unit)
        })
    }

    override fun serialization(): SerializeElement {
        return configValue.serialization()
    }

    class DurationDelegated(private val configDuration: ConfigDuration) {

        operator fun getValue(thisRef: Any?, property: KProperty<*>): Duration {
            return configDuration.getValue().duration
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Duration) {
            configDuration.setValue(value)
        }

    }

}

