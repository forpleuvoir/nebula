package moe.forpleuvoir.nebula.config.item

import moe.forpleuvoir.nebula.config.ConfigGroup
import moe.forpleuvoir.nebula.config.ConfigItem
import moe.forpleuvoir.nebula.config.ConfigSerde

class ConfigRange<T>(
    name: String,
    defaultValue: T,
    val minValue: T,
    val maxValue: T,
    serde: ConfigSerde<T>,
) : ConfigItem<T>(name, defaultValue, serde) where T : Comparable<T> {

    init {
        require(minValue <= maxValue) { "minValue[$minValue] must be <= maxValue[$maxValue]" }
        configValue = clamp(defaultValue)
    }

    private fun clamp(value: T): T = when {
        value < minValue -> minValue
        value > maxValue -> maxValue
        else             -> value
    }

    override fun setValue(value: T) {
        super.setValue(clamp(value))
    }
}

context(group: ConfigGroup)
fun <T> configRange(
    name: String,
    defaultValue: T,
    minValue: T,
    maxValue: T,
    serde: ConfigSerde<T>
): ConfigRange<T> where T : Comparable<T> =
    group.addConfig(ConfigRange(name, defaultValue, minValue, maxValue, serde))