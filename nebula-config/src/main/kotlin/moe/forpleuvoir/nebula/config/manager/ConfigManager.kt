package moe.forpleuvoir.nebula.config.manager

import moe.forpleuvoir.nebula.config.container.ConfigContainer
import moe.forpleuvoir.nebula.config.manager.plugin.ConfigManagerPlugin
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.time.Duration

interface ConfigManager : ConfigContainer {

    /**
     * 所有对配置内容的操作都应该在此函数调用之后执行
     */
    override fun init()

    fun plugin(plugin: ConfigManagerPlugin): ConfigManager

    suspend fun save()

    /**
     *  在配置保存完成后执行
     * @param callback (duration: Duration) -> Unit  duration为保存耗时
     */
    fun onSaved(callback: (duration: Duration) -> Unit)

    fun asyncSave()

    suspend fun forceSave()

    fun asyncForceSave()

    suspend fun load()

    /**
     * 在配置加载完成后执行
     * @param callback (duration: Duration) -> Unit  duration为加载耗时
     */
    fun onLoaded(callback: (duration: Duration) -> Unit)

    fun asyncLoad()

}

@OptIn(ExperimentalContracts::class)
inline fun ConfigManager.plugin(pluginProvider: ConfigManager.() -> ConfigManagerPlugin): ConfigManager {
    contract {
        callsInPlace(pluginProvider, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return plugin(pluginProvider.invoke(this))
}

@OptIn(ExperimentalContracts::class)
inline fun ConfigManager.plugins(pluginProvider: ConfigManagerPluginContext.() -> Unit): ConfigManager {
    contract {
        callsInPlace(pluginProvider, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    pluginProvider.invoke(ConfigManagerPluginContext(this))
    return this
}

class ConfigManagerPluginContext(val manager: ConfigManager) {
    fun plugin(plugin: ConfigManagerPlugin) = manager.plugin(plugin)
}