package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.item.ConfigNumberValue
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive

abstract class ConfigNumber<T>(
    override val key: String,
    final override val defaultValue: T,
    final override val minValue: T,
    final override val maxValue: T,
) : ConfigBase<T, ConfigNumber<T>>(), ConfigNumberValue<T> where T : Number, T : Comparable<T> {

    override var configValue: T = defaultValue.clamp(minValue, maxValue)

    protected val clamp: T.(T, T) -> T
        get() = { minValue, maxValue ->
            if (this > maxValue) maxValue
            else if (this < minValue) minValue
            else this
        }

    protected abstract val mapping: Number.()-> T

    override fun setValue(value: T) {
        super.setValue(value.clamp(minValue, maxValue))
    }

    override fun serialization(): SerializeElement =
        SerializePrimitive(configValue)

    override fun deserialization(serializeElement: SerializeElement) {
        configValue = serializeElement.asNumber.mapping().clamp(minValue, maxValue)
    }

}