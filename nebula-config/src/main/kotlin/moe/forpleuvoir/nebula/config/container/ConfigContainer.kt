package moe.forpleuvoir.nebula.config.container

import moe.forpleuvoir.nebula.config.ConfigSerializable
import moe.forpleuvoir.nebula.serialization.DeserializationException
import moe.forpleuvoir.nebula.serialization.SerializationException
import moe.forpleuvoir.nebula.serialization.base.SerializeElement

interface ConfigContainer : ConfigSerializable {

    /**
     * 所有对配置内容的操作都应该在此函数调用之后执行
     */
    override fun init()

    /**
     * 将所有配置添加到容器中
     */
    fun loadConfigs()

    /**
     * 初始化所有配置
     */
    fun initConfigs()

    /**
     * 获取所有配置
     * @return [Collection]<[ConfigSerializable]>
     */
    fun configs(): Collection<ConfigSerializable>

    override fun matched(regex: Regex): Boolean = regex.containsMatchIn(key) || configs().any { it.matched(regex) }

    override fun isDefault(): Boolean = configs().all { it.isDefault() }

    override fun restDefault() {
        configs().forEach { it.restDefault() }
    }

    fun <C : ConfigSerializable> addConfig(config: C): C

    fun deserializationExceptionHandler(
        config: ConfigSerializable,
        serializeElement: SerializeElement,
        e: DeserializationException
    )

    fun serializationExceptionHandler(
        config: ConfigSerializable,
        e: SerializationException
    )

}