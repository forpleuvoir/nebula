package moe.forpleuvoir.nebula.config

import moe.forpleuvoir.nebula.common.api.Initializable
import moe.forpleuvoir.nebula.serialization.base.SerializeElement

interface ConfigCategory : Initializable, ConfigSerializable {

    override val key: String

    var needSave: Boolean

    fun configSerializes(): Iterable<ConfigSerializable>

    fun addConfigSerializable(configSerializable: ConfigSerializable): ConfigSerializable

    fun deserializationExceptionHandler(
        configSerializable: ConfigSerializable,
        serializeElement: SerializeElement,
        e: Exception
    )

}