@file:Suppress("unused")

package moe.forpleuvoir.nebula.config.item

import moe.forpleuvoir.nebula.config.ConfigGroup
import moe.forpleuvoir.nebula.config.ConfigSerde
import moe.forpleuvoir.nebula.config.config
import moe.forpleuvoir.nebula.serialization.codec.Codec
import moe.forpleuvoir.nebula.serialization.codec.default
import moe.forpleuvoir.nebula.serialization.codec.duration
import moe.forpleuvoir.nebula.serialization.codec.range
import kotlin.time.Duration

context(group: ConfigGroup)
fun duration(name: String, defaultValue: Duration) =
    config(name, defaultValue, ConfigSerde.of(Codec.duration.default(defaultValue)))

context(group: ConfigGroup)
fun finiteDuration(
    name: String,
    defaultValue: Duration,
    minDuration: Duration = Duration.ZERO,
    maxDuration: Duration = Duration.INFINITE,
) = configRange(name, defaultValue, minDuration, maxDuration, ConfigSerde.of(Codec.duration.default(defaultValue).range(minDuration, maxDuration)))
