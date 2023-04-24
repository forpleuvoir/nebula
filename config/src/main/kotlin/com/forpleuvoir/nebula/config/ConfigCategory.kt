package com.forpleuvoir.nebula.config

import com.forpleuvoir.nebula.common.api.Initializable
import com.forpleuvoir.nebula.serialization.base.SerializeElement

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