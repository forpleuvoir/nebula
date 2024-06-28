package moe.forpleuvoir.nebula.config.container

import moe.forpleuvoir.nebula.config.ConfigSerializable
import moe.forpleuvoir.nebula.serialization.DeserializationException
import moe.forpleuvoir.nebula.serialization.base.SerializeElement

interface ConfigContainer : ConfigSerializable {

    companion object {

        operator fun invoke(
            key: String,
            autoScan: Boolean = true,
            descriptionKeyMap: (String) -> String = { "_$it" }
        ): ConfigContainer {
            return ConfigContainerImpl(key, autoScan, descriptionKeyMap)
        }

    }

    /**
     * 所有对配置内容的操作都应该在此函数调用之后执行
     */
    override fun init()

    var needSave: Boolean

    fun configureSerializable()

    fun initSerializable()

    fun allConfigSerializable(): Iterable<ConfigSerializable>

    fun <C : ConfigSerializable> addConfigSerializable(configSerializable: C): C

    fun deserializationExceptionHandler(
        configSerializable: ConfigSerializable,
        serializeElement: SerializeElement,
        e: DeserializationException
    )


}