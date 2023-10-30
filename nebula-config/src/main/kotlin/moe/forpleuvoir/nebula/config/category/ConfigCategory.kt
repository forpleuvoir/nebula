package moe.forpleuvoir.nebula.config.category

import moe.forpleuvoir.nebula.config.ConfigSerializable
import moe.forpleuvoir.nebula.serialization.DeserializationException
import moe.forpleuvoir.nebula.serialization.base.SerializeElement

interface ConfigCategory : ConfigSerializable {

    /**
     * 所有对配置内容的操作都应该在此函数调用之后执行
     */
    override fun init()

    override val key: String

    var needSave: Boolean

    fun allConfigSerializable(): Iterable<ConfigSerializable>

    fun addConfigSerializable(configSerializable: ConfigSerializable): ConfigSerializable

    fun deserializationExceptionHandler(
        configSerializable: ConfigSerializable,
        serializeElement: SerializeElement,
        e: DeserializationException
    )

}